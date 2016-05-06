package com.afa.fizicianu.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afa.fizicianu.R;



/**
 * Created by filip on 4/26/2016.
 */
public class LectiiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.lectii_fragment,container,false);
    }


}
