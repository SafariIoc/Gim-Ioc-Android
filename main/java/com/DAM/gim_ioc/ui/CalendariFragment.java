package com.DAM.gim_ioc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.DAM.gim_ioc.R;

import java.util.Date;

import io.realm.Realm;


public class CalendariFragment extends Fragment {

    public CalendariFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendari, container, false);



        return view;
    }
}