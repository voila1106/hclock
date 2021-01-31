package com.voila.healthclock;

import android.app.*;

import java.io.*;
import java.util.*;

public class init extends Application
{
	public static Properties p=new Properties();

	@Override
	public void onCreate()
	{
		super.onCreate();
		try
		{
			p.load(new FileInputStream(getFilesDir().getAbsolutePath() + "/prop.txt"));
		}catch(IOException e)
		{
			toast.makeText(this,"加载失败",0).show();
			e.printStackTrace();
		}
	}
}
