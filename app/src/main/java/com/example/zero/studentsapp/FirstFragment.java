package com.example.zero.studentsapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        pref = getActivity().getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        TextView tv = (TextView)v.findViewById(R.id.tvUsername);
        String username = pref.getString("username", "");
        tv.setText(username);

        final WebView myWebView = (WebView)v.findViewById(R.id.webView);
        myWebView.loadUrl("http://192.168.1.12/owl_attendance/search.php");
        //for future
        //myWebView.loadUrl("http://www.example.com/owl_attendance//user="+"Usrname");

        FloatingActionButton ref = (FloatingActionButton)v.findViewById(R.id.refresh);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.reload();
            }
        });

        return  v;
    }

}
