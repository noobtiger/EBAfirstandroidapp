package eba.bodyloger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by c-cpinnama on 2/25/2015.
 */
public class ActivityLoggerDetail extends ActionBarActivity{
    private SharedPreferences settings;
    DB_datasource datasource;
    DB_Model model;
    protected void onCreate(Bundle savedInstanceState) {

        settings=getSharedPreferences("settings", Context.MODE_PRIVATE);
        final String units=settings.getString("pref_units","us");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggerdetail);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

        if (units.equals("std")) {
            final TextView unit_Weight=(TextView)findViewById(R.id.textWeightUnit);
            final TextView unit_Waist=(TextView)findViewById(R.id.textWaistUnit);
            final TextView unit_Neck=(TextView)findViewById(R.id.textNeckUnit);
            unit_Weight.setText("Kgs");
            unit_Neck.setText("cms");
            unit_Waist.setText("cms");
        }

        TextView date=(TextView)findViewById(R.id.textDateValue);
        final EditText TextWeight=(EditText)findViewById(R.id.editTextWeightUpdate);
        final EditText TextWaist=(EditText) findViewById(R.id.editTextWaistUpdate);
        final EditText TextNeck=(EditText) findViewById(R.id.editNeckTextUpdate);
        int chosenPosition=0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chosenPosition= (int)extras.getInt("chosen");
            datasource=new DB_datasource(this);
            datasource.open();
            int total=datasource.findTotalRows();
            //chosenPosition=total-chosenPosition;
            String datesel=datasource.findRowDate(chosenPosition+2);
                TextWeight.setText(Double.toString(datasource.findRowWeight(chosenPosition+2)));
                TextWaist.setText(Double.toString(datasource.findRowWaist(chosenPosition+2)));
                TextNeck.setText(Double.toString(datasource.findRowNeck(chosenPosition+2)));
                date.setText(datesel);

        }

        final int rowID=chosenPosition;
        final Button button = (Button) findViewById(R.id.updateButton);
               button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String weight=TextWeight.getText().toString();
                String waist=TextWaist.getText().toString();
                String neck=TextNeck.getText().toString();
                datasource.updateData(rowID,weight,waist,neck);
                Intent i = new Intent(ActivityLoggerDetail.this, MainActivity.class);
                startActivity(i);
                           datasource.close();
            }
        });

    }
}
