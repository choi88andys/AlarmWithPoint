package and.sample.alarmwithpoint.common;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class StateData {
    int imageLP;
    int imageAP;

    int LinkedPoint;
    int AutonomicPoint;
    //private String keyword;


    public void increasePoint(String keyword, String typeOfPoint, Context context) {
        SharedPreferences pref = context.getSharedPreferences("common", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        // each point will decrease depend on multiple alarm
        int valueOfPoint = pref.getInt("valueOf" + keyword, -1);
        int prevPoint = pref.getInt(typeOfPoint, -1);

        editor.putInt(typeOfPoint, prevPoint + valueOfPoint);
        editor.putBoolean("isOnTime" + keyword, false);
        editor.apply();
    }


    public int getImageLP() {
        return imageLP;
    }

    public void setImageLP(int imageLP) {
        this.imageLP = imageLP;
    }

    public int getImageAP() {
        return imageAP;
    }

    public void setImageAP(int imageAP) {
        this.imageAP = imageAP;
    }

    public int getLinkedPoint() {
        return LinkedPoint;
    }

    public void setLinkedPoint(int linkedPoint) {
        LinkedPoint = linkedPoint;
    }

    public int getAutonomicPoint() {
        return AutonomicPoint;
    }

    public void setAutonomicPoint(int autonomicPoint) {
        AutonomicPoint = autonomicPoint;
    }
}
