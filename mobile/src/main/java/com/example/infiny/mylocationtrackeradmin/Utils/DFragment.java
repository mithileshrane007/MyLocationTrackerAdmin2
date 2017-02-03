package com.example.infiny.mylocationtrackeradmin.Utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infiny.mylocationtrackeradmin.R;

/**
 * Created by infiny on 3/2/17.
 */

public class DFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_frag_layout_txtview, container,
                false);
        // Do something else
        return rootView;
    }
}