package com.example.gyun_notebook.myapplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BindService extends Service implements LocationListener {

    private Context mainContetx;

    public MovePageTwo movePageTwo ;

    private Geocoder geocoder = null;

    private Intent SENSSEER = new Intent(Contact.SENSSEER);

    private LocationManager locationManager;
    private MqttAndroidClient client;

    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("service", "서비스가 실행됨");

        geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);

        mqttConnect();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        locationManager();

        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        public BindService getService() {
            return BindService.this;
        }
    }

    public void getMaiContext(Context context){
        this.mainContetx = context;

    }

    private void subscribe_streaming() {
        try {
            client.subscribe("ThemaPark/101/fire", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String msg = new String(mqttMessage.getPayload(), "euc-kr");
                    if (msg.equals("0")) {
                        notificationService();
                        SENSSEER.putExtra("data", "fire/" + msg);
                        sendBroadcast(SENSSEER);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribe_censor() {

        try {
            client.subscribe("ThemaPark/101/gas", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String msg = new String(mqttMessage.getPayload(), "euc-kr");
                    if (msg.equals("0")) {
                        SENSSEER.putExtra("data", "gas/" + msg);
                        sendBroadcast(SENSSEER);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            client.subscribe("ThemaPark/101/vibrate", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String msg = new String(mqttMessage.getPayload(), "euc-kr");
                    if (msg.equals("0")) {
                        SENSSEER.putExtra("data", "vibrate/" + msg);
                        sendBroadcast(SENSSEER);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            client.subscribe("ThemaPark/101/temperature", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String msg = new String(mqttMessage.getPayload(), "euc-kr");
                    SENSSEER.putExtra("data", "temperature/" + msg);
                    sendBroadcast(SENSSEER);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            client.subscribe("ThemaPark/101/humid", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String msg = new String(mqttMessage.getPayload(), "euc-kr");
                    SENSSEER.putExtra("data", "humid/" + msg);
                    sendBroadcast(SENSSEER);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void notificationService() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence name = "aaaa";
        String description = "bbb";

        int importance = NotificationManager.IMPORTANCE_MAX;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("1", "1", importance);

            channel.setDescription(description);
            channel.enableLights(true);

            channel.setLightColor(Color.GREEN);

            channel.enableVibration(true);

            channel.setVibrationPattern(new long[]{100, 200, 300});

            notificationManager.createNotificationChannel(channel);
        }


        Intent intent;
 //           intent = new Intent(this, MainActivity.class);
        intent = new Intent(this, StDialogActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            Notification notification = new Notification.Builder(this).setContentTitle(Contact.localoca+"화재발생").setContentText("눌러서 확인").setSmallIcon(R.drawable.analytics).setChannelId("1").setContentIntent(pi).build();
            notificationManager.notify(1, notification);
        } else {
            NotificationCompat.Builder builder;
            builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.analytics)
                    .setContentTitle(Contact.localoca+"화재발생")
                    .setContentText("눌러서 확인")
                    .setDefaults(Notification.DEFAULT_VIBRATE)// 수신 알람 적용w
                    .setAutoCancel(true) //알림바에서 자동 삭제
                    .setContentIntent(pi).setPriority(Notification.VISIBILITY_PUBLIC)
            ;
            notificationManager.notify(0, builder.build());
        }

        PowerManager.WakeLock sCpuWakeLock = null;

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        sCpuWakeLock.acquire();


        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }


    }

    private boolean isAppRunning(Context context){
        if(context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            for (int i = 0; i < procInfos.size(); i++) {
                if (procInfos.get(i).processName.equals(context.getPackageName())) {
                    return true;
                }
            }
        }

        return false;
    }


    public void mqttConnect() {


        //client = new MqttAndroidClient(this, "tcp://13.125.249.130:1883", "asdasdsad"); // 데모용 id.
        client = new MqttAndroidClient(this, "tcp://192.168.0.50:1883", "asdasdsad"); // 데모용 id.
        try {
            IMqttToken token = client.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    client.setBufferOpts(getDisconnectedBufferOptions());
                    Log.e("Connect_success", "Success");

                    subscribe_streaming();
                    subscribe_censor();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("connect_fail", "Failure " + exception.toString());
                }
            });
        } catch (
                MqttException e)

        {
            e.printStackTrace();
        }
    }

    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setWill("aaa", "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("username");
        //mqttConnectOptions.setPassword("password".toCharArray());
        return mqttConnectOptions;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("location", String.valueOf(location.getLatitude()));
        Log.e("location", String.valueOf(location.getLongitude()));

        List<Address> list = null;

        double d1 = location.getLatitude();
        double d2 = location.getLongitude();

        try {
            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
            } else {
                Address address = (Address) list.get(0);
                String a = address.getAddressLine(0);
                String aa = address.getSubLocality();   // 구 알아오는 코드

                Contact.localoca = aa;
            }
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("location", "");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("gps", "gps 서비스에 연결 되엇습니다");

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void locationManager() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100f, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100f, this);
        }

    }

}
