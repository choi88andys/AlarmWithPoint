package and.sample.alarmwithpoint.common;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import and.sample.alarmwithpoint.MainActivity;
import and.sample.alarmwithpoint.R;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String keyword = intent.getStringExtra("keyword");
        int requestCode = intent.getIntExtra("code", -1);


        if ("awp.alarm".equals(action)) {
            Log.d("send_rec", "action catch");

            int code = -1;
            switch (keyword) {
                case "water":
                    code = 1;
                    break;
                case "medic":
                    code = 2;
                    break;
                case "break":
                    code = 3;
                    break;
                case "meal":
                    code = 4;
                    break;
                case "exe":
                    code = 5;
                    break;
            } // to allowed multiple alarm type


            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("keyword", keyword);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, code, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.splash);
            builder.setTicker("LP ticker");
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("It's " + keyword + " time!");
            builder.setContentText("Click here and get LP");
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);

            Log.d("send_rec", "code: " + intent.getIntExtra("code", -1) + "");

            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("ch_id_1", "ch_name_1", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("ch_desc_1");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                notificationmanager.createNotificationChannel(notificationChannel);
                builder.setChannelId("ch_id_1");
            }
            notificationmanager.notify(code, builder.build());


            // it called every week
            PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long interval = AlarmManager.INTERVAL_DAY * 7;
            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(ALARM_TYPE, System.currentTimeMillis() + interval, pi);
            } else {
                am.setExact(ALARM_TYPE, System.currentTimeMillis() + interval, pi);
            }

        } // action == awp.alarm

    }

}

