package com.example.gyun_notebook.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

public class StreamingFragment extends Fragment {


    public StreamingFragment() {

    }


    public static StreamingFragment getInstance() {
        Bundle args = new Bundle();
        StreamingFragment fragment = new StreamingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_streaming,container,false);

        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("http://192.168.0.50:8090/ThemaPark/101/fire.mjpg");

        return view;
    }
}
