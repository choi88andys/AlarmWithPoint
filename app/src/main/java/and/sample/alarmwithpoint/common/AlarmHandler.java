package and.sample.alarmwithpoint.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmHandler {
    private String serverName;
    private String localName;

    public AlarmHandler(String serverName, String localName) {
        this.serverName = serverName;
        this.localName = localName;
    }

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    public void removeData(final String keyword) {
        databaseReference.child(serverName).child(localName).child("alarm_list").child(keyword).removeValue();
    }


    private void addData(AnAlarm anAlarm) {
        databaseReference.child(serverName).child(localName).child("alarm_list").child(anAlarm.keyword).push().setValue(anAlarm);
    }


    public void registerData(String keyword, ArrayList<Integer> tempData, ArrayList<Integer> tempCode) {
             for (int i = 0; i < tempCode.size(); i++) {
            AnAlarm anAlarm = new AnAlarm(keyword, tempData.get(i), tempCode.get(i));
            addData(anAlarm);
        }
    }


    public void setAlarm(Context context, ArrayList<Integer> tempData, ArrayList<Integer> tempCode, String keyword) {
        long nowTime = System.currentTimeMillis();
        for (int i = 0; i < tempData.size(); i++) {

            int tempTime = tempData.get(i);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, (tempTime / 1440) + 1);
            calendar.set(Calendar.HOUR_OF_DAY, (tempTime % 1440) / 60);
            calendar.set(Calendar.MINUTE, ((tempTime) % 1440) % 60);
            calendar.set(Calendar.SECOND, 0);


            long alarmTime = calendar.getTimeInMillis();
            if (alarmTime < nowTime) {
                alarmTime += AlarmManager.INTERVAL_DAY * 7;
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("awp.alarm");
            intent.putExtra("keyword", keyword);
            intent.putExtra("code", tempCode.get(i));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tempCode.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(ALARM_TYPE, alarmTime, pendingIntent);
            } else {
                am.setExact(ALARM_TYPE, alarmTime, pendingIntent);
            }
            // need BootReceiver

        }

    }


    public void cancelAlarm(Context context, ArrayList<Integer> requestCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("awp.alarm");


        for (int i = 0; i < requestCode.size(); i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (pendingIntent != null) {
                am.cancel(pendingIntent);
                pendingIntent.cancel();
            } else {
                Toast.makeText(context, "error: not exist", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode.size() > 0) {
            Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();
        }


    }


    public void setRequestCode(final String keyword, final ArrayList<Integer> requestCode) {
        requestCode.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                AnAlarm anAlarm = dataSnapshot.getValue(AnAlarm.class);
                requestCode.add(anAlarm.requestCode);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(serverName).child(localName).child("alarm_list").child(keyword).addChildEventListener(childEventListener);
    }

}

