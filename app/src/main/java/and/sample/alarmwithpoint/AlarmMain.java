package and.sample.alarmwithpoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import and.sample.alarmwithpoint.common.AlarmHandler;
import and.sample.alarmwithpoint.common.AnAlarm;

public class AlarmMain extends AppCompatActivity {
    private final String[] dayOfWeek = new String[]{"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    Button btn1, btn2, btn3;
    ListView listView;
    ArrayAdapter arrayAdapter;
    TextView textView;

    AlarmHandler alarmHandler;

    String serverName;
    String localName;
    String keyword = "";
    ArrayList<Integer> requestCode = new ArrayList<>();


    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        serverName = getString(R.string.app_name);
        SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        localName = pref.getString("localName", null);
        alarmHandler = new AlarmHandler(serverName, localName);


        textView = findViewById(R.id.text_2);
        listView = findViewById(R.id.list_1);
        arrayAdapter = new ArrayAdapter<>(AlarmMain.this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


        btn1 = findViewById(R.id.btn_list);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmMain.this);

                final String[] items = getItems();
                builder.setTitle("Select keyword");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keyword = items[which] + "";
                        showList_1(keyword); // to user

                        String temp = (keyword + " alarm list");
                        textView.setText(temp);

                        alarmHandler.setRequestCode(keyword, requestCode);
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();

            }
        });

        btn2 = findViewById(R.id.btn_register);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmMain.this, RegisterAlarm.class);
                startActivity(intent);
            }
        });

        btn3 = findViewById(R.id.btn_delete);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyword.equals("")) {
                    Toast.makeText(AlarmMain.this, "empty keyword", Toast.LENGTH_SHORT).show();
                    return;
                }

                alarmHandler.cancelAlarm(AlarmMain.this, requestCode);
                alarmHandler.removeData(keyword);
            }
        });
    }


    public String[] getItems() {
        String[] items = {getString(R.string.keyword_water),
                getString(R.string.keyword_medic),
                getString(R.string.keyword_exe),
                getString(R.string.keyword_meal),
                getString(R.string.keyword_break)};
        return items;
    }


    void addTime(DataSnapshot dataSnapshot, ArrayAdapter<String> arrayAdapter) {
        AnAlarm anAlarm = dataSnapshot.getValue(AnAlarm.class);
        if (anAlarm != null) {

            int time = anAlarm.value;
            //int time = Integer.parseInt(temp);

            int day = time / 1440;
            int hour = (time % 1440) / 60;
            int minute = (time % 1440) % 60;

            arrayAdapter.add(dayOfWeek[day] + ": " + hour + "h " + minute + "m");
        }
    }

    void removeTime(DataSnapshot dataSnapshot, ArrayAdapter<String> arrayAdapter) {
        AnAlarm anAlarm = dataSnapshot.getValue(AnAlarm.class);
        if (anAlarm != null) {

            int time = anAlarm.value;
            //int time = Integer.parseInt(temp);

            int day = time / 1440;
            int hour = (time % 1440) / 60;
            int minute = (time % 1440) % 60;

            arrayAdapter.remove(dayOfWeek[day] + ": " + hour + "h " + minute + "m");
        }
    }


    void showList_1(final String keyword) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(arrayAdapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addTime(dataSnapshot, arrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                removeTime(dataSnapshot, arrayAdapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(serverName).child(localName).child("alarm_list").child(keyword).addChildEventListener(childEventListener);
    }


}
