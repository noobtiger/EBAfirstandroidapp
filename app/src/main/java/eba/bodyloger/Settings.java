package eba.bodyloger;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Settings extends ActionBarActivity{
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

        settings=getSharedPreferences("settings",MODE_PRIVATE);

        final Button button = (Button) findViewById(R.id.saveButton);
        final EditText edittext_name   = (EditText)findViewById(R.id.editTextName_settings);
        final EditText edittext_age=(EditText)findViewById(R.id.editTextAge_settings);
        final EditText edittext_heightFeet=(EditText)findViewById(R.id.editTextHeightFeet_settings);
        final EditText edittext_heightInches=(EditText)findViewById(R.id.editTextHeightFeetInch_settings);
        final EditText edittext_weightGoal=(EditText)findViewById(R.id.editTexttargetweight_settings);
        final TextView textView_units=(TextView)findViewById(R.id.textWeightUnit);

        if (new String(settings.getString("pref_units","us")).equals("std")){
            textView_units.setText("kgs");
                    }
        final String name = settings.getString("name","") ;
        edittext_name.setText(name);
        final String age=settings.getString("age","");
        edittext_age.setText(age);
        final String height_feet=settings.getString("height_feet","");
        edittext_heightFeet.setText(height_feet);
        final String height_inch=settings.getString("height_inch","");
        edittext_heightInches.setText(height_inch);
        final String Targetweight=settings.getString("targetweight","");
        edittext_weightGoal.setText(Targetweight);



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String heightFeet=edittext_heightFeet.getText().toString();
                String heightInch=edittext_heightInches.getText().toString();
                final int height=((Integer.parseInt(heightFeet))*12)+(Integer.parseInt(heightInch));

                SavePreferences("targetweight",edittext_weightGoal.getText().toString());
                SavePreferences("height_feet",heightFeet);
                SavePreferences("height_inch",heightInch);
                SavePreferences("height",Integer.toString(height));
                SavePreferences("age",edittext_age.getText().toString());
                SavePreferences("name",edittext_name.getText().toString());
                Intent i = new Intent(Settings.this, MainActivity.class);
                startActivity(i);

            }
        });
    }

    private void SavePreferences(String key, String value){
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();

    }
}
