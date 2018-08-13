package com.example.gyun_notebook.myapplication;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChartFragement extends Fragment {


    private PieChart pieChart;

    private TextView temperature ;
    private ImageView gasImage;
    private ImageView fireimage;
    private ImageView vibrateimage;
    public ChartFragement() {

    }


    public static ChartFragement getInstance() {
        Bundle args = new Bundle();
        ChartFragement fragment = new ChartFragement();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chartfragment, container, false);

        registerBroadCast();

        temperature = view.findViewById(R.id.chart_fragment_textView_temperature);
        gasImage = view.findViewById(R.id.chart_fragment_imageView_gas);
        fireimage = view.findViewById(R.id.chart_fragment_imageView_fire);
        vibrateimage = view.findViewById(R.id.chart_fragment_imageView_viberate);

        pieChart = view.findViewById(R.id.PieChart);

        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDragDecelerationEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(34f,""));
        entries.add(new PieEntry(78f,""));


        final int[] MY_COLORS = {Color.rgb(206,242,121),Color.rgb(255,255,255) };
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);


        PieDataSet dataSet = new PieDataSet(entries,"choi");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);


        return view;
    }

    private void registerBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.SENSSEER);
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.SENSSEER)) {
                String a = intent.getStringExtra("data");
                String[] splite = a.split("/");
                if(splite[0].equals("fire")){
                    Log.e("fire",splite[1]);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(2);
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(3);
                        }
                    }).start();
                }else if(splite[0].equals("gas")){
                    Log.e("gas",splite[1]);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(1);
                        }
                    }).start();

                }else if(splite[0].equals("vibrate")){
                    Log.e("vibrate",splite[1]);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(4);
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(5);
                        }
                    }).start();
                }else if(splite[0].equals("temperature")){
                    Log.e("temperature",splite[1]);

                    temperature.setText(splite[1]+"℃");

                }else if(splite[0].equals("humid")){
                    Log.e("humid",splite[1]);

                    int aa = (int)Double.parseDouble(splite[1]);
                    int bb = 100 - aa;

                    ArrayList<PieEntry> entries = new ArrayList<>();

                    entries.add(new PieEntry(aa,"humid"));
                    entries.add(new PieEntry(bb,""));

                    final int[] MY_COLORS = {Color.rgb(206,242,121),Color.rgb(255,255,255) };
                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for(int c: MY_COLORS) colors.add(c);

                    PieDataSet dataSet = new PieDataSet(entries,"choi");
                    /*dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);*/
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setData(data);

                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();


                }

            }
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                gasImage.setImageResource(R.drawable.gas);
            }else if(msg.what == 1){
                gasImage.setImageResource(R.drawable.empty_gas);
            }else if(msg.what == 2){
                fireimage.setImageResource(R.drawable.flame);
            }else if(msg.what == 3){
                fireimage.setImageResource(R.drawable.empty_flame);
            }else if(msg.what == 4){
                vibrateimage.setImageResource(R.drawable.vibration);
            }else if(msg.what == 5){
                vibrateimage.setImageResource(R.drawable.empty_vibration);
            }
        }
    };


}

















