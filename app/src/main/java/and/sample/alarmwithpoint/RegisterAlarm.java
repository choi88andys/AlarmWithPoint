package and.sample.alarmwithpoint;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

import and.sample.alarmwithpoint.common.AlarmHandler;

public class RegisterAlarm extends AppCompatActivity {
    Calendar myCalender = Calendar.getInstance();
    private final int hourValue = 60;
    private final int dayValue = 1440;
    private final int weekValue = 10080;


    RadioGroup radioGroup1, radioGroup2;
    Button btn1, btn2, btn3;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    int radioID_1 = R.id.radio_1;
    int radioID_2 = R.id.radio_water;

    String serverName;
    String localName;
    String keyword;
    AlarmHandler alarmHandler;

    ArrayList<Integer> tempData = new ArrayList<>();
    ArrayList<Integer> tempCode = new ArrayList<>();
    ArrayList<Integer> requestCode = new ArrayList<>();
    // ArrayList<Integer> values = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alarm);

        serverName = getString(R.string.app_name);
        SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        localName = pref.getString("localName", null);
        setKeyword(radioID_2);
        alarmHandler = new AlarmHandler(serverName, localName);
        alarmHandler.setRequestCode(keyword, requestCode);


        listView = findViewById(R.id.list_1);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


        radioGroup1 = findViewById(R.id.rg_1);
        radioGroup1.check(R.id.radio_1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioID_1 = checkedId;
            }
        });


        // change keyword
        radioGroup2 = findViewById(R.id.rg_2);
        radioGroup2.check(R.id.radio_water);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioID_2 = checkedId;
                setKeyword(radioID_2);
                alarmHandler.setRequestCode(keyword, requestCode);

                arrayAdapter.clear();
                tempData.clear();
                tempCode.clear();
            }
        });


        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarm();
            }
        });

        // register button
        btn2 = findViewById(R.id.btn_register);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempData.size() == 0) {
                    return;
                }
                //Collections.sort(tempData);

                // delete
                alarmHandler.cancelAlarm(RegisterAlarm.this, requestCode);
                alarmHandler.removeData(keyword);


                // register
                alarmHandler.setAlarm(RegisterAlarm.this, tempData, tempCode, keyword);
                alarmHandler.registerData(keyword, tempData, tempCode);


                SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();


                int tempPoint = setPoint(tempData.size());
                editor.putInt("valueOf" + keyword, tempPoint);
                editor.apply();


                arrayAdapter.clear();
                tempData.clear();
                tempCode.clear();
                Toast.makeText(RegisterAlarm.this, "Register completed", Toast.LENGTH_SHORT).show();
            }
        });

        btn3 = findViewById(R.id.btn_reset);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    int setPoint(int size) {
        final int basePoint = getResources().getInteger(R.integer.base_point);
        int tempPoint = (int) (basePoint / Math.sqrt(size));
        if (tempPoint < 1) {
            tempPoint = 1;
        }

        return tempPoint;
    }


    void setKeyword(int id) {
        switch (id) {
            case R.id.radio_water:
                keyword = getString(R.string.keyword_water);
                break;
            case R.id.radio_break:
                keyword = getString(R.string.keyword_break);
                break;
            case R.id.radio_meal:
                keyword = getString(R.string.keyword_meal);
                break;
            case R.id.radio_medic:
                keyword = getString(R.string.keyword_medic);
                break;
            case R.id.radio_exe:
                keyword = getString(R.string.keyword_exe);
                break;
        }
    }


    public void reset() {
        arrayAdapter.clear();
        tempData.clear();
        tempCode.clear();
    }

    public void showAlarm() {
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);

                    int day;
                    switch (radioID_1) {
                        case R.id.radio_1:
                            day = 0;
                            break;
                        case R.id.radio_2:
                            day = 1;
                            break;
                        case R.id.radio_3:
                            day = 2;
                            break;
                        case R.id.radio_4:
                            day = 3;
                            break;
                        case R.id.radio_5:
                            day = 4;
                            break;
                        case R.id.radio_6:
                            day = 5;
                            break;
                        case R.id.radio_7:
                            day = 6;
                            break;
                        default:
                            Log.e("on_time", "error");
                            return;
                    }
                    int alarmTime = (day * dayValue) + (myCalender.get(Calendar.HOUR_OF_DAY) * hourValue) + myCalender.get(Calendar.MINUTE);


                    // prevent abuse, need change
                    if (!timeValidate(alarmTime)) {
                        Toast.makeText(RegisterAlarm.this, "Short interval", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    int requestCode = pref.getInt("requestCode", -1);
                    if (requestCode == -1) {
                        Log.e("on_time", "no request code");
                        return;
                    }
                    tempData.add(alarmTime);
                    tempCode.add(requestCode);
                    editor.putInt("requestCode", requestCode + 1);
                    editor.apply();


                    RadioButton btn = findViewById(radioGroup1.getCheckedRadioButtonId());
                    String temp =btn.getText().toString() + ": " + myCalender.get(Calendar.HOUR_OF_DAY) + "h " + myCalender.get(Calendar.MINUTE) + "m";
                    arrayAdapter.add(temp);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Choose Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public boolean timeValidate(int time) {
        int minInterval;
        boolean validate = true;

        switch (radioID_2) {
            case R.id.radio_water:
                minInterval = getResources().getInteger(R.integer.interval_water);
                break;
            case R.id.radio_break:
                minInterval = getResources().getInteger(R.integer.interval_break);
                break;
            case R.id.radio_meal:
                minInterval = getResources().getInteger(R.integer.interval_meal);
                break;
            case R.id.radio_medic:
                minInterval = getResources().getInteger(R.integer.interval_medic);
                break;
            case R.id.radio_exe:
                minInterval = getResources().getInteger(R.integer.interval_exe);
                break;
            default:
                minInterval = getResources().getInteger(R.integer.interval_default);
        }

        for (int i = 0; i < tempData.size(); i++) {
            int diff = Math.abs(tempData.get(i) - time);
            if (diff < minInterval) {
                validate = false;
                break;
            }
        }

        return validate;
        //return true;
    }


}
