package com.example.gyun_notebook.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class StDialogActivity extends AppCompatActivity {
    private WebView webView;

    private LinearLayout linearLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_st_dialog);

        webView = findViewById(R.id.webView_dialog);

        webView.loadUrl("http://192.168.0.50:8090/ThemaPark/101/fire.mjpg");

        linearLayout = findViewById(R.id.dialog_webView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);

                finish();
            }
        });
    }
}
