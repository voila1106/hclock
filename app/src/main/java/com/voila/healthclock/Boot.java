package com.voila.healthclock;

import android.content.*;

import java.io.*;
import java.util.*;

public class Boot extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Properties p=init.p;
		if(p.getProperty("co","").equals(""))
		{
			context.startService(new Intent(context,ClockService.class));
		}
	}
}
