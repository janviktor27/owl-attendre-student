package com.example.zero.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    final  String TAG = this.getClass().getName();
    Button bLogin;
    EditText tfUsername, tfPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Boolean checkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = (Button)findViewById(R.id.bLogin);
        tfUsername = (EditText)findViewById(R.id.tfUsername);
        tfPassword = (EditText)findViewById(R.id.tfPassword);

        bLogin.setOnClickListener(this);

        pref = getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        String username = pref.getString("username", "");
        String password = pref.getString("password", "");

        HashMap data = new HashMap();
        data.put("etUsername", username);
        data.put("etPassword", password);

        if(!(username.equals("") && password.equals(""))){
            PostResponseAsyncTask task = new PostResponseAsyncTask(LoginActivity.this, data, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    Log.d(TAG, s);
                    if(s.contains("success")){
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("Username", tfUsername.getText().toString());
                        startActivity(i);
                    }
                }
            });
            task.execute("http://192.168.1.22/owl_attendance/stdlogin.php");
        }



    }

    @Override
    public void onClick(View v) {
        HashMap data = new HashMap();
        data.put("etUsername", tfUsername.getText().toString());
        data.put("etPassword", tfPassword.getText().toString());
        editor.putString("username", tfUsername.getText().toString());
        editor.putString("password", tfPassword.getText().toString());
        editor.apply();


        PostResponseAsyncTask task = new PostResponseAsyncTask(LoginActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("Username", tfUsername.getText().toString());
                    startActivity(i);
                }
            }
        });

        task.execute("http://192.168.1.22/owl_attendance/stdlogin.php");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkFlag = isChecked;
        Log.d(TAG, "checkFlag: " + checkFlag);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
