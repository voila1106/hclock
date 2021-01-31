package com.voila.healthclock;

import android.content.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import android.os.Bundle;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainActivity extends AppCompatActivity
{

	EditText jgprov, jgcity, homeprov, homecity, homeaddr, nowprov, nowcity, nowaddr, phone, cookie, acid;
	TextView res;
	String path;
	Properties p = init.p;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//View
		jgprov = findViewById(R.id.jgprov);
		jgcity = findViewById(R.id.jgcity);
		homeprov = findViewById(R.id.homeprov);
		homecity = findViewById(R.id.homecity);
		homeaddr = findViewById(R.id.homeadadr);
		nowprov = findViewById(R.id.nowprov);
		nowcity = findViewById(R.id.nowcity);
		nowaddr = findViewById(R.id.nowadadr);
		phone = findViewById(R.id.phone);
		cookie = findViewById(R.id.cookie);
		acid = findViewById(R.id.acid);
		res = findViewById(R.id.result);

		//var
		path = getFilesDir().getAbsolutePath() + "/prop.txt";
		try
		{
			Runtime.getRuntime().exec("touch " + path);
		}catch(IOException e)
		{
			e.printStackTrace();
			toast.makeText(this, "写入失败：\n" + e.toString(), 1).show();
		}

		//first use
		if(p.getProperty("first", "").equals(""))
		{
			p.setProperty("first", "yes");
			try
			{
				p.store(new FileWriter(path), "");
			}catch(IOException e)
			{
				e.printStackTrace();
				toast.makeText(this, "写入失败：\n" + e.toString(), 1).show();
			}
			new AlertDialog.Builder(this)
				.setTitle("自动健康上报")
				.setMessage("使用方法略为复杂，建议先看看使用说明")
				.setPositiveButton("看看", (dialog, which) -> startActivity(new Intent(MainActivity.this, HelpActivity.class)))
				.setNegativeButton("不了", (dialog, which) -> toast.makeText(MainActivity.this, "点击右上角可以打开使用说明", 0).show())
				.show();
		}

		try
		{
			cookie.setText(p.getProperty("co", ""));
			jgprov.setText(URLDecoder.decode(p.getProperty("jp", ""),"utf-8"));
			jgcity.setText(URLDecoder.decode(p.getProperty("jc", ""),"utf-8"));
			homeprov.setText(URLDecoder.decode(p.getProperty("hp", ""),"utf-8"));
			homecity.setText(URLDecoder.decode(p.getProperty("hc", ""),"utf-8"));
			homeaddr.setText(URLDecoder.decode(p.getProperty("ha", ""),"utf-8"));
			nowprov.setText(URLDecoder.decode(p.getProperty("np", ""),"utf-8"));
			nowcity.setText(URLDecoder.decode(p.getProperty("nc", ""),"utf-8"));
			nowaddr.setText(URLDecoder.decode(p.getProperty("na", ""),"utf-8"));
			phone.setText(URLDecoder.decode(p.getProperty("ph", ""),"utf-8"));
			acid.setText(URLDecoder.decode(p.getProperty("ac", ""),"utf-8"));

		}catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}


		if(!p.getProperty("co", "").equals(""))
		{
			startService(new Intent(this, ClockService.class));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.mnu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		if(item.getItemId() == R.id.help)
		{
			startActivity(new Intent(this, HelpActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onSubmitClick(View v)
	{
		String jp = jgprov.getText().toString(),
			jc = jgcity.getText().toString(),
			hp = homeprov.getText().toString(),
			hc = homecity.getText().toString(),
			ha = homeaddr.getText().toString(),
			np = nowprov.getText().toString(),
			nc = nowcity.getText().toString(),
			na = nowaddr.getText().toString(),
			co = cookie.getText().toString(),
			ac = acid.getText().toString(),
			ph = phone.getText().toString();
		if(jp.isEmpty() || jc.isEmpty() || hp.isEmpty() || hc.isEmpty() || ha.isEmpty() || np.isEmpty() ||
			nc.isEmpty() || na.isEmpty() || ph.isEmpty() || co.isEmpty() || ac.isEmpty())
		{
			toast.makeText(this, "请填写全部内容", 0).show();
			return;
		}

		try
		{
			p.setProperty("jp", URLEncoder.encode(jp,"utf-8"));
			p.setProperty("jc", URLEncoder.encode(jc,"utf-8"));
			p.setProperty("hp", URLEncoder.encode(hp,"utf-8"));
			p.setProperty("hc", URLEncoder.encode(hc,"utf-8"));
			p.setProperty("ha", URLEncoder.encode(ha,"utf-8"));
			p.setProperty("np", URLEncoder.encode(np,"utf-8"));
			p.setProperty("nc", URLEncoder.encode(nc,"utf-8"));
			p.setProperty("na", URLEncoder.encode(na,"utf-8"));
			p.setProperty("ph", URLEncoder.encode(ph,"utf-8"));
			p.setProperty("co", co);
			p.setProperty("ac", URLEncoder.encode(ac,"utf-8"));
		}catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		try
		{
			p.store(new FileWriter(path), "");
			co.substring(22, 54);
		}catch(IOException e)
		{
			e.printStackTrace();
			toast.makeText(this, "写入失败：\n" + e.toString(), 1).show();
			return;
		}catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			toast.makeText(this, "cookie解析失败：\n" + e.toString(), 1).show();
			return;
		}
		startService(new Intent(this, ClockService.class));
		toast.makeText(this,"数据已更新",0).show();
	}
}