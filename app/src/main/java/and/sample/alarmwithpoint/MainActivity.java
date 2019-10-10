package and.sample.alarmwithpoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        editor.putString("localName", "localNameDef");
        if (pref.getInt("requestCode", -1) == -1) {
            editor.putInt("requestCode", 3);
            editor.putInt("LP", 100);
            editor.putInt("AP", 100);
        }
        editor.apply();

        //SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();
        Intent point_intent = getIntent();
        String keyword = point_intent.getStringExtra("keyword");
        editor.putBoolean("isOnTime" + keyword, true);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, FirstState.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        SharedPreferences pref = getSharedPreferences("common", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String keyword = intent.getStringExtra("keyword");
        editor.putBoolean("isOnTime" + keyword, true);
        editor.apply();

        Intent i = new Intent(MainActivity.this, FirstState.class);
        startActivity(i);
    }
}
