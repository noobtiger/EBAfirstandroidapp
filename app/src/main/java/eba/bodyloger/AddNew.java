package eba.bodyloger;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by c-cpinnama on 2/12/2015.
 */
public class AddNew extends ActionBarActivity{
    DB_datasource datasource;
    private SharedPreferences settings;
    EditText edit_weight ;
    EditText edit_waist ;
    EditText edit_neck ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        settings=getSharedPreferences("settings", Context.MODE_PRIVATE);
        final String units=settings.getString("pref_units","us");
        Log.i("DEBUG",units);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

        if (units.equals("std")) {
            final TextView unit_Weight=(TextView)findViewById(R.id.ADDtextWeightUnit);
            final TextView unit_Waist=(TextView)findViewById(R.id.ADDtextWaistUnit);
            final TextView unit_Neck=(TextView)findViewById(R.id.ADDtextNeckUnit);
            unit_Weight.setText("Kgs");
            unit_Neck.setText("cms");
            unit_Waist.setText("cms");
        }

        datasource=new DB_datasource(this);
        datasource.open();

        edit_weight = (EditText) findViewById(R.id.editTextWeight);
        edit_waist = (EditText) findViewById(R.id.editTextWaist);
        edit_neck= (EditText) findViewById(R.id.editTextNeck);
        String waist=datasource.findLastWaist();
        edit_waist.setText(waist);
        String weight=datasource.findLastWeight();
        edit_weight.setText(weight);
        String neck=datasource.findLastNeck();
        edit_neck.setText(neck);

        final Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                createData(units);

                Intent i = new Intent(AddNew.this, MainActivity.class);
                startActivity(i);

                datasource.close();
            }
        });
    }

    public void createData(String unit){
        final String height=settings.getString("height","0");
        int height_int=((Integer.parseInt(height)) != 0) ? (Integer.parseInt(height)) : 1;

        DB_Model model=new DB_Model();
        Double value;
        Double valueWeight;
        Double valueWaist;
        Double valueNeck;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        model.setdate(formattedDate);

        valueWeight = Double.parseDouble(edit_weight.getText().toString());
        model.setweight(valueWeight);
        valueWaist = Double.parseDouble(edit_waist.getText().toString());
        model.setwaist(valueWaist);
        valueNeck = Double.parseDouble(edit_neck.getText().toString());
        model.setneck(valueNeck);
        value=0.0;
        double valueBMI;
        if(unit.equals("std")){
            valueBMI=(valueWeight)/(height_int*height_int);
        }else{
            valueBMI=(float)(((valueWeight))*703)/(height_int*height_int);
        }
        model.setBMI(valueBMI);

        settings=getSharedPreferences("settings", Context.MODE_PRIVATE);
        final String units=settings.getString("pref_units","us");
        final String sex=settings.getString("pref_sex","male");
        value=calculateBF(valueWeight,valueWaist,sex, units);
        model.setBF(value);



        model= datasource.create(model);
        Log.i("TEST", "Tour create with data: " + model.getId());

    }

    public double calculateBF(Double wt,Double wis, String sex, String unit) {
        Double weight=((wt) != 0) ? (wt) : 1;
        Double waist = wis;

        if(unit.equals("std")){
            weight=weight*2.20462;
            waist=waist*0.393701;
        }

        double BF=0.00;
        if (sex.equals("female")) {
            BF=(double) ((-76.76 + (4.15 * waist) - (.082 * weight)) / weight) * 100;

        } else {
            BF = (double) ((-98.42 + (4.15 * waist) - (.082 * weight)) / weight) * 100;

        }
        return BF;

    }
}
