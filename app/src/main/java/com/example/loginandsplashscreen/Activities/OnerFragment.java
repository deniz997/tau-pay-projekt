package com.example.loginandsplashscreen.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginandsplashscreen.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnerFragment extends Fragment {


    public OnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empfehlen, container, false);
    }

}
