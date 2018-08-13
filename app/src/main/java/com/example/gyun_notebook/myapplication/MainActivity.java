package com.example.gyun_notebook.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView navigationView;

    private ChartFragement chartFragement ;
    private StreamingFragment streamingFragment;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermision(Contact.PERMISSIONS);
        }
        initStatusbar();
        initFragment();

        bindservice();



    }

    private void bindservice(){

        Intent intent = new Intent(this,BindService.class);

        bindService(intent,this ,Context.BIND_AUTO_CREATE);

    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "종료하시려면 한번더 눌러주세요", Toast.LENGTH_SHORT).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initStatusbar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorcccc));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermision(String[] permision) {
        requestPermissions(permision, MY_PERMISSION_REQUEST_STORAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                int cnt = permissions.length;
                for (int i = 0; i < cnt; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                    }
                }
                break;
        }
    }

    public void initFragment() {
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi_first:
                        SelectNavView("first");

                        return true;
                    case R.id.navi_second:
                        SelectNavView("second");
                        return true;

                }
                return false;
            }
        };

        chartFragement = ChartFragement.getInstance();
        streamingFragment = StreamingFragment.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment, chartFragement, "first");
        fragmentTransaction.add(R.id.fragment, streamingFragment, "second");

        fragmentTransaction.hide(streamingFragment);
        fragmentTransaction.commit();

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void SelectNavView(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.getTag().equals(tag)) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BindService.MyBinder myBinder = (BindService.MyBinder)service;

        final BindService bindService = myBinder.getService();

        bindService.movePageTwo = movePageTwo;

        Log.e("bindservie","meessage");



    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    MovePageTwo movePageTwo = new MovePageTwo() {
        @Override
        public void move_page() {

        }


    };
}
