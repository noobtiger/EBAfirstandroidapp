package eba.bodyloger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by c-cpinnama on 2/12/2015.
 */
public class FirstOpen extends ActionBarActivity{
    private SharedPreferences settings;
//    private SharedPreferences savesettings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstopen_basic);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);
        settings=getSharedPreferences("settings",MODE_PRIVATE);
//        savesettings=getSharedPreferences("eba.bodyloger",MODE_PRIVATE);

        final Button buttonNext =(Button) findViewById(R.id.firstOpenNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstopenBasic();
            }
        });


    }

    private void firstopenBasic(){
        final RadioButton radiobutton_sexMale= (RadioButton) findViewById(R.id.radioButton_male);
        final RadioButton radiobutton_sexFemale= (RadioButton) findViewById(R.id.radioButton_female);
        final EditText edittext_name   = (EditText)findViewById(R.id.editTextName);
        final EditText edittext_age=(EditText)findViewById(R.id.editTextAge);
        final RadioButton radiobutton_standard=(RadioButton) findViewById(R.id.radioButton_metric);
        final RadioButton radiobutton_us=(RadioButton) findViewById(R.id.radioButton_imperial);

        if (radiobutton_sexFemale.isChecked()) {
            SavePreferences("pref_sex","female");
        }
       else {
            SavePreferences("pref_sex","male");
        }
        if (radiobutton_standard.isChecked()) {
            SavePreferences("pref_units","std");
        }
        else {
            SavePreferences("pref_units","us");
        }
        SavePreferences("age",edittext_age.getText().toString());
        SavePreferences("name",edittext_name.getText().toString());

        setContentView(R.layout.firstopen_goal);

        final Button buttonGoal =(Button) findViewById(R.id.buttonGoalNext);
        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstopenGoal();
            }
        });
    }
    public void firstopenGoal(){
        final RadioButton radiobutton_lose=(RadioButton)findViewById(R.id.radioButton1);
        final RadioButton radiobutton_gain=(RadioButton)findViewById(R.id.radioButton2);
        final RadioButton radiobutton_maintain=(RadioButton)findViewById(R.id.radioButton3);
        final Button buttonGoalNext =(Button) findViewById(R.id.buttonGoalNext);

                        if (radiobutton_gain.isChecked()) {
                    SavePreferences("pref_goal","gain");
                }else if(radiobutton_maintain.isChecked()){
                    SavePreferences("pref_goal","maintain");
                }else{
                    SavePreferences("pref_goal","lose");
                }


        setContentView(R.layout.firstopen_measure);
        final Button buttonMeasure =(Button) findViewById(R.id.buttonNextMeasure);
        buttonMeasure.setEnabled(false);

        //Updating units based on preference
        final TextView textview_units=(TextView)findViewById(R.id.unitlbs);
        final TextView textview_units2=(TextView)findViewById(R.id.unitlbs2);
        if (new String(settings.getString("pref_units","us")).equals("std")){
            textview_units.setText("kgs");
            textview_units2.setText("kgs");
        }

        //Updating height between units
        final EditText editText_heightfeet   = (EditText)findViewById(R.id.editTextheightfeet);
        final EditText editText_heightInches   = (EditText)findViewById(R.id.editTextheightinches);
        final EditText editText_heightmeter   = (EditText)findViewById(R.id.editText3);


        editText_heightmeter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double height = Double.parseDouble(s.toString());
                height=3.28084*height;
                int heightFeet=height.intValue();
                double heightInches=height-heightFeet;
                editText_heightfeet.setText(Integer.toString(heightFeet));
                int heightInch=(int)(heightInches*12);
                editText_heightInches.setText(Integer.toString(heightInch));
            }
        });
        editText_heightfeet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonMeasure.setEnabled(true);
            }
        });


        buttonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Please input your initial measurements",
                        Toast.LENGTH_LONG).show();

                //Save height and target weight
                final EditText editText_targetweight   = (EditText)findViewById(R.id.editText2);
                final EditText editText_heightfeet=(EditText)findViewById(R.id.editTextheightfeet);
                final EditText editText_heightinches=(EditText)findViewById(R.id.editTextheightinches);
                final EditText editText_currentweight = (EditText)findViewById(R.id.editTextcurrWeight);
                SavePreferences("currentweight",editText_currentweight.getText().toString());
                SavePreferences("targetweight", editText_targetweight.getText().toString());
                String heightFeet=editText_heightfeet.getText().toString();
                String heightInch=editText_heightinches.getText().toString();
                final int height=((Integer.parseInt(heightFeet))*12)+(Integer.parseInt(heightInch));
                SavePreferences("height",Integer.toString(height));
                SavePreferences("height_feet",heightFeet);
                SavePreferences("height_inch",heightInch);

//               savesettings.edit().putString("firstopenrun","0");
                Intent i=new Intent(FirstOpen.this, AddNew.class);
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
