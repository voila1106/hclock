package com.voila.healthclock;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import androidx.core.app.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClockService extends Service
{
	String jp, jc, hp, hc, ha, np, nc, na, ph, co, ac;
	String path;

	@Override
	public void onCreate()
	{
		Timer t = new Timer();
		path=getFilesDir().getAbsolutePath() + "/prop.txt";
		Properties p = init.p;

		Notification n = new NotificationCompat.Builder(this, "id")
			.setContentTitle(p.getProperty("date", "-1"))
			.setSmallIcon(android.R.drawable.ic_delete)
			.build();
		final NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		NotificationChannel noc = new NotificationChannel("id", "上报", NotificationManager.IMPORTANCE_HIGH);
		nm.createNotificationChannel(noc);
		startForeground(1, n);

		TimerTask tt = new TimerTask()
		{
			@Override
			public void run()
			{
				try
				{
					p.load(new FileInputStream(getFilesDir().getAbsolutePath() + "/prop.txt"));
				}catch(IOException e)
				{
					e.printStackTrace();
				}
				String SubmitDay = p.getProperty("date", "-1").trim();
				String today = new Date().getDate() + "";
				if(!SubmitDay.equals(today))
				{
					try
					{
						jp = p.getProperty("jp", "");
						jc = p.getProperty("jc", "");
						hp = p.getProperty("hp", "");
						hc = p.getProperty("hc", "");
						ha = p.getProperty("ha", "");
						np = p.getProperty("np", "");
						nc = p.getProperty("nc", "");
						na = p.getProperty("na", "");
						ph = p.getProperty("ph", "");
						co = p.getProperty("co", "");
						ac = p.getProperty("ac", "");

						String uaid = co.substring(22, 54);
						HttpRequest hr = new HttpRequest("http://srv.zsc.edu.cn/f/_jiankangSave", "POST");
						hr.setContent(("uaid=" + uaid + //学生代码
							"&provinceJg=" + jp + //籍贯
							"&cityJg=" + jc + //籍贯
							"&provinceHome=" + hp + //家乡所在地
							"&cityHome=" + hc + //家乡所在地
							"&addressHome=" + ha + //家乡所在地
							"&province=" + np +
							"&city=" + nc +
							"&address=" + na +
							"&mobile=" + ph + //手机号
							"&touchZhongGaoFlag=%E6%B2%A1%E6%9C%89" + //中高风险
							"&touchZhongGaoAddress=" + //中高风险
							"&huBeiManFlag2=%E5%90%A6" + //接触病例
							"&touchDate2=" + //接触病例
							"&touchDays2=--%E8%AF%B7%E9%80%89%E6%8B%A9--" + //接触病例
							"&touchProvince=" +
							"&touchCity=" +
							"&touchMember2=" +
							"&sheQuTouchFlag=%E5%90%A6" + //社区接触
							"&sheQuTouchDate=" +
							"&sheQuTouchDays=--%E8%AF%B7%E9%80%89%E6%8B%A9--" +
							"&sheQuTouchProvince=" +
							"&sheQuTouchCity=" +
							"&sheQuTouchMember=" +
							"&jinWaiTouchFlag=%E5%90%A6" + //接触境外
							"&jinWaiTouchDate=" +
							"&jinWaiTouchDays=--%E8%AF%B7%E9%80%89%E6%8B%A9--" +
							"&jinWaiTouchProvince=" +
							"&jinWaiTouchCity=" +
							"&jinWaiTouchMember=" +
							"&huCheckFlag=" + ac +
							"&health=%E6%AD%A3%E5%B8%B8" + //健康状态
							"&exceptionDate=" +
							"&dealMethod=" +
							"&dealResule=" +
							"&memberHealth=%E6%AD%A3%E5%B8%B8" + //家庭成员健康状态
							"&memberHealthDes=" +
							"&suikangCode=%E7%BB%BF%E7%A0%81" + //健康码
							"&zgfxAreaFlag=%E5%90%A6" //中高风险
						).getBytes());
						hr.setCookie(co);
						String ret = "";

						ret = hr.require();
						Log.d("fuckret", ret);

						if(ret.contains("成功"))
						{
							Notification state = new NotificationCompat.Builder(ClockService.this, "id")
								.setContentTitle("健康上报")
								.setContentText(today + "日已提交 " + URLDecoder.decode(na,"utf-8") + " " + URLDecoder.decode(ac,"utf-8"))
								.setSmallIcon(android.R.drawable.presence_away)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.presence_online))
								.build();
							nm.notify(2, state);
							p.setProperty("date", today);
							p.store(new FileWriter(path), "");
						}else
						{
							Notification state = new NotificationCompat.Builder(ClockService.this, "id")
								.setContentTitle("健康上报")
								.setContentText("提交失败\n" + ret.replaceAll("\n",""))
								.setSmallIcon(android.R.drawable.presence_away)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_delete))
								.build();
							nm.notify(2, state);
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}else
				{
					Log.d("fuck", "提交过了");
				}
				Notification in = new NotificationCompat.Builder(ClockService.this, "id")
					.setContentTitle(p.getProperty("date", "-1")+"  ✓")
					.setSmallIcon(android.R.drawable.presence_away)
					.build();
				startForeground(1, in);
			}
		};
		t.schedule(tt, 0, 30 * 1000);

	}


	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
