package com.example.loginandsplashscreen.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginandsplashscreen.Handlers.NetworkHandling;
import com.example.loginandsplashscreen.Handlers.QRCodeHandler;
import com.example.loginandsplashscreen.R;


public class LoginActivity extends AppCompatActivity {
    public static String token = "";
    private AutoCompleteTextView mIDView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        token = null;
        SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("var1", null);
        editor.commit();
        mIDView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mPwvergessen = findViewById(R.id.bu_forgotpassword);
        mPwvergessen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new NetworkHandling().execute("forgotPassword",mIDView.getText().toString()).get();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }


    private void attemptLogin() {
        mIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = mIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(id)) {
            mIDView.setError("Ögrenci numaranizi girin!");
            focusView = mIDView;
            cancel = true;
        }
        NetworkHandling h = new NetworkHandling();
        try {
            //with .execute(firstparameter is instruction)
            token = h.execute("login",id,password).get();
            SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("var1", token);
            editor.commit();

        } catch (Exception e) {
            System.out.println(e);
        }


        System.out.println(token);
        // Check for a valid id address.
        if (isInfoValid(token)) {
            mIDView.setError("Ögrenci numarasi veya sifre yanlis!");
            focusView = mIDView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String k="";
            NetworkHandling h1 = new NetworkHandling();
            try {
                k = h1.execute("getInfo",token).get();
            } catch (Exception e) {
                System.out.println(e);
            }
            NetworkHandling h2 = new NetworkHandling();
            try {
                String t = h2.execute("requestQRCode","170503101").get();
                String f = new NetworkHandling().execute("getInfo",token).get();
                System.out.println(f);
                ImageView qr_image = (ImageView) findViewById(R.id.qrcode);
                qr_image.setImageBitmap(QRCodeHandler.generateQRCodeImage(t,200,200));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            } catch (Exception e) {
                System.out.println("java exception" + e);
            }

        }
    }

    private boolean isInfoValid(String token) {
        try {
            return token==null || !token.substring(0,6).equals("Bearer");
        } catch (Exception e) {
            System.out.println("Exception in isInfoValid: " + e);
            return true;
        }
    }
}

