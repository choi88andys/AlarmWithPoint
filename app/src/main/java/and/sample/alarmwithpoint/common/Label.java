package and.sample.alarmwithpoint.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import and.sample.alarmwithpoint.R;

public class Label extends FrameLayout {

    public Label(Context context) {
        super(context);
        init();
    }


    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    ImageView image1, image2;
    TextView textLP, textAP;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.label_layout, this);
        image1=findViewById(R.id.image_1);
        image2=findViewById(R.id.image_2);

        textLP=findViewById(R.id.text_1);
        textAP=findViewById(R.id.text_2);
    }


    public void setItemData(StateData data) {
        image1.setImageResource(data.imageLP);
        image2.setImageResource(data.imageAP);

        String str1 = "LP: " + data.LinkedPoint;
        String str2 = "AP: " + data.AutonomicPoint;

        textLP.setText(str1);
        textAP.setText(str2);
    }


}
