package and.sample.alarmwithpoint.common;

import com.google.firebase.database.Exclude;

public class AnAlarm {

    public int requestCode;
    public int value;
    @Exclude
    public String keyword;


    public AnAlarm() {
    }

    public AnAlarm(String keyword, int value, int requestCode) {
        this.keyword = keyword;
        this.requestCode = requestCode;
        this.value = value;
    }


}
