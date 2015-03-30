package eba.bodyloger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by c-cpinnama on 3/24/2015.
 */
public class ProfileWeight extends ActionBarActivity {
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB_datasource datasource = new DB_datasource(this);
        datasource.open();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_weight);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

        settings=getSharedPreferences("settings", Context.MODE_PRIVATE);
        final String profile_startingweight=settings.getString("currentweight","0");
        TextView weightLossDesc=(TextView)findViewById(R.id.textView1);
        TextView weightLoss=(TextView)findViewById(R.id.profile_weightloss);
        //weightLoss.setText(profile_targetweight);
        Double currentWeight=datasource.findRowWeight(datasource.maxID());

        double weightLost=Double.parseDouble(profile_startingweight)-(currentWeight);

        if(weightLost<0){
            weightLost=Math.abs(weightLost);
weightLossDesc.setText("You've Gained ");
            weightLoss.setText(String.valueOf(weightLost)+ " lbs");
        }else{
            weightLoss.setText(String.valueOf(weightLost)+ " lbs");
        }
    }
    }
