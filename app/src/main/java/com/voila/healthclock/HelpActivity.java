package com.voila.healthclock;

import android.content.*;
import android.net.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView hc=findViewById(R.id.hcdl);
        TextView fd=findViewById(R.id.fddl);
        TextView cd=findViewById(R.id.code);

        hc.setOnClickListener((view)-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://mc1106.cn/httpcanary.apk"))));
        fd.setOnClickListener((view)-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://mc1106.cn/fiddler.rar"))));
        cd.setOnClickListener((view)-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/voila1106/hclock"))));
    }
}