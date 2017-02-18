package com.example.zero.studentsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.zero.studentsapp.R.id.tvUsername;
import static com.example.zero.studentsapp.R.layout.activity_main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //location
    private Button btGetLocation;
    private TextView tvLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public Double lat;
    public Double longi;


    //scanner
    private Button scan_btn;

    //
    final String TAG = this.getClass().getName();

    public String scanned;

    //sharedpreferencesss
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //json
    String Course, FistName, LastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        String username = pref.getString("username", "");
        String password = pref.getString("password", "");

        if((username.equals("") && password.equals(""))){
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }

        Log.d(TAG, pref.getString("username", ""));
        Log.d(TAG, pref.getString("password", ""));



        View z = findViewById(R.id.fab);
        z.setVisibility(View.INVISIBLE);//Or View.INVISBLE
        View zz = findViewById(R.id.tvLocation);
        zz.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getname
        TextView tv = (TextView) findViewById(tvUsername);
        final TextView tvcourse = (TextView) findViewById(R.id.tvCourse);
        final TextView tvFullname = (TextView) findViewById(R.id.tvFullname);
        tv.setText(username);

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<Post> userList = new JsonConverter<Post>().toArrayList(s, Post.class);

                String output = "";
                for(Post p: userList){

                    Course = p.course_id;
                    FistName = p.student_fname;
                    LastName = p.student_lname;
//                    output += "id: " + p.std_id;
//                    output += ", CIN: " + p.student_cin;
//                    output += ", First Name: " + p.student_fname;
//                    output += ", Last Name: " + p.student_lname;
//                    output += ", Course: " + p.course_id;
                }
                tvcourse.setText(Course);
                tvFullname.setText(LastName+", "+FistName);


            }
        });

        task.execute("http://192.168.1.22/owl_attendance/json.php?user="+username);

        //scanner
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intergrator = new IntentIntegrator(activity);
                intergrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intergrator.setPrompt("Scan");
                intergrator.setCameraId(0);
                intergrator.setBeepEnabled(false);
                intergrator.setBarcodeImageEnabled(false);
                intergrator.initiateScan();


            }
        });

        //getLocation
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvLocation.append("\n "+location.getLatitude()+" "+location.getLongitude());
                lat = location.getLatitude();
                longi = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settings);
            }
        };

        configure_button();

    }

    //location
    private void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
//        btGetLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    //scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                scanned = result.getContents().toString();
                attend();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void attend(){
        String username = pref.getString("username", "");
        HashMap data = new HashMap();
        data.put("scannedqrcode", scanned);
        data.put("username", username);

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){

                }
            }
        });
        task.execute("http://192.168.1.22/owl_attendance/attend.php");
    }

    @Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent out = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(out);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.first_fragment) {
            View b = findViewById(R.id.tvLocation);
            b.setVisibility(View.INVISIBLE);
            View c = findViewById(R.id.scan_btn);
            c.setVisibility(View.INVISIBLE);
            View z = findViewById(R.id.fab);
            z.setVisibility(View.VISIBLE);//Or View.INVISBLE
            View a = findViewById(R.id.tvCourse);
            a.setVisibility(View.VISIBLE);
            View s = findViewById(R.id.imageView2);
            s.setVisibility(View.VISIBLE);
            View d = findViewById(R.id.tvFullname);
            d.setVisibility(View.VISIBLE);
            setTitle("News Feed");
            FirstFragment fragment = new FirstFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main,fragment,"Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.second_fragment) {
            View b = findViewById(R.id.tvLocation);
            b.setVisibility(View.INVISIBLE);
            View c = findViewById(R.id.scan_btn);
            c.setVisibility(View.INVISIBLE);
            View z = findViewById(R.id.fab);
            z.setVisibility(View.VISIBLE);//Or View.INVISBLE
            View a = findViewById(R.id.tvCourse);
            a.setVisibility(View.VISIBLE);
            View s = findViewById(R.id.imageView2);
            s.setVisibility(View.VISIBLE);
            View d = findViewById(R.id.tvFullname);
            d.setVisibility(View.VISIBLE);
            setTitle("Attendance");
            SecondFragment fragment = new SecondFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main,fragment,"Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.third_fragment) {
            View b = findViewById(R.id.tvLocation);
            b.setVisibility(View.VISIBLE);
            View c = findViewById(R.id.scan_btn);
            c.setVisibility(View.INVISIBLE);
            View z = findViewById(R.id.fab);
            z.setVisibility(View.INVISIBLE);
            View a = findViewById(R.id.tvUsername);
            a.setVisibility(View.INVISIBLE);//Or View.INVISBLE
            View e = findViewById(R.id.tvCourse);
            e.setVisibility(View.INVISIBLE);
            View s = findViewById(R.id.imageView2);
            s.setVisibility(View.INVISIBLE);
            View d = findViewById(R.id.tvFullname);
            d.setVisibility(View.INVISIBLE);
            setTitle("Location log");
            ThirdFragment fragment = new ThirdFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main,fragment,"Fragment");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
