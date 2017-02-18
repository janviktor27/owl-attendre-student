package com.example.zero.studentsapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);

        pref = getActivity().getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        TextView tv = (TextView)v.findViewById(R.id.tvUsername);
        String username = pref.getString("username", "");
        tv.setText(username);

        return v;
    }

}
