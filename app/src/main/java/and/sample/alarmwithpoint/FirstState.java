package and.sample.alarmwithpoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import and.sample.alarmwithpoint.common.Label;
import and.sample.alarmwithpoint.common.StateData;

public class FirstState extends AppCompatActivity implements View.OnClickListener {
    Button btn_water, btn_medic, btn_meal, btn_break, btn_exe;
    Button btn_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_state);

        btn_alarm = findViewById(R.id.btn_alarm);
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstState.this, AlarmMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        Label label = findViewById(R.id.lable_1);
        StateData stateData = new StateData();
        stateData.setImageLP(R.drawable.ic_launcher);
        stateData.setImageAP(R.drawable.ic_launcher);
        stateData.setLinkedPoint(pref.getInt("LP", -1));
        stateData.setAutonomicPoint(pref.getInt("AP", -1));
        label.setItemData(stateData);


        btn_water = findViewById(R.id.btn_water);
        btn_water.setOnClickListener(this);
        btn_medic = findViewById(R.id.btn_medic);
        btn_medic.setOnClickListener(this);
        btn_meal = findViewById(R.id.btn_meal);
        btn_meal.setOnClickListener(this);
        btn_exe = findViewById(R.id.btn_exe);
        btn_exe.setOnClickListener(this);
        btn_break = findViewById(R.id.btn_break);
        btn_break.setOnClickListener(this);


        if (pref.getBoolean("isOnTime" + "water", false)) {
            btn_water.setVisibility(View.VISIBLE);
        }

        if (pref.getBoolean("isOnTime" + "medic", false)) {
            btn_medic.setVisibility(View.VISIBLE);
        }

        if (pref.getBoolean("isOnTime" + "break", false)) {
            btn_break.setVisibility(View.VISIBLE);
        }

        if (pref.getBoolean("isOnTime" + "meal", false)) {
            btn_meal.setVisibility(View.VISIBLE);
        }

        if (pref.getBoolean("isOnTime" + "exe", false)) {
            btn_exe.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        StateData stateData = new StateData();

        switch (v.getId()) {
            case R.id.btn_water:
                btn_water.setVisibility(View.INVISIBLE);
                stateData.increasePoint("water", "LP", FirstState.this);
                onResume();
                break;
            case R.id.btn_medic:
                btn_medic.setVisibility(View.INVISIBLE);
                stateData.increasePoint("medic", "LP", FirstState.this);
                onResume();
                break;
            case R.id.btn_meal:
                btn_meal.setVisibility(View.INVISIBLE);
                stateData.increasePoint("meal", "LP", FirstState.this);
                onResume();
                break;
            case R.id.btn_exe:
                btn_exe.setVisibility(View.INVISIBLE);
                stateData.increasePoint("exe", "LP", FirstState.this);
                onResume();
                break;
            case R.id.btn_break:
                btn_break.setVisibility(View.INVISIBLE);
                stateData.increasePoint("break", "LP", FirstState.this);
                onResume();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstState.this);
        builder.setMessage("Quit?");
        builder.setTitle("QuitDialog").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finishAffinity();
                        ActivityCompat.finishAffinity(FirstState.this);
                        System.runFinalization();
                        System.exit(0);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

}
