package com.example.loginandsplashscreen.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.app.Activity;

import com.example.loginandsplashscreen.Handlers.NetworkHandling;
import com.example.loginandsplashscreen.R;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.sql.SQLOutput;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class FeedbackActivity extends AppCompatActivity {
    private SimpleRatingBar srb;
    private EditText commentField;
    private SegmentedButtonGroup sbg;
    private Toolbar mToolbar;
    private Button sendFeedback;
    final String[] type = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24px);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        srb = (SimpleRatingBar) findViewById(R.id.rb_feedback_stars);
        srb.setRating(1);
        srb.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                if(rating == 0){
                    srb.setRating(1);
                }
            }
        });
        sbg = (SegmentedButtonGroup) findViewById(R.id.sbg_feedback_type);
        sbg.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                if(position == 0){
                    type[0] = "mensa";
                }
                else if(position == 1){
                    type[0] = "shuttle";
                }
            }
        });

        type[0] = null;
        sendFeedback = (Button) findViewById(R.id.bu_feedback_send);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v);
            }
        });
    }

    public void showAlertDialog(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.feedback));
            alert.setMessage(getString(R.string.degerlendirmeyi_g??ndermek_istiyor_musunuz));

            alert.setNegativeButton(getString(R.string.hayir), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FeedbackActivity.this,getString(R.string.degerlendirme_gonderilmedi), Toast.LENGTH_SHORT).show();
                }
            });

            alert.setPositiveButton(R.string.evet, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    commentField = (EditText) findViewById(R.id.tf_feedback_text);

                    String star = String.valueOf(srb.getRating());
                    star = star.substring(0,1);

                    String text = commentField.getText().toString();
                    System.out.println(text);
                    if (type[0]==null)
                        type[0] = "mensa";
                    System.out.println(type[0]);

                    try {
                        String f = new NetworkHandling().execute("feedback",star, type[0],text,LoginActivity.token).get();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Toast.makeText(FeedbackActivity.this,getString(R.string.degerlendirme_gonderildi), Toast.LENGTH_SHORT).show();
                }
            });
            alert.create().show();
    }

    public void onBackPressed(){
        Intent i = new Intent(FeedbackActivity.this,MainActivity.class);
        finish();
        startActivity(i);
    }
}
