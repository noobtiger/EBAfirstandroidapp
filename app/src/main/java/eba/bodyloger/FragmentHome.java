package eba.bodyloger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Admin on 1/29/2015.
 */
public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener{
    private SharedPreferences settings;
    DB_datasource datasource;
    private ListView myListView;
    private String[] strListView;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static FragmentHome newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        mPage = getArguments().getInt(ARG_PAGE);
        if(mPage==1) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            fragamentHome(view);
        }else if(mPage==2){
             view = inflater.inflate(R.layout.fragment_logger, container, false);
            fragmentLogger(view);
        }else{
             view = inflater.inflate(R.layout.fragment_profile, container, false);
        }
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }
    public double calculateBF(String wt,String wis, String sex) {
        float weight=((Float.parseFloat(wt)) != 0) ? (Float.parseFloat(wt)) : 1;
        float waist = Float.parseFloat(wis);

        double BF=0.00;
        if (sex.equals("female")) {
            BF=(double) ((-76.76 + (4.15 * waist) - (.082 * weight)) / weight) * 100;

        } else {
            BF = (double) ((-98.42 + (4.15 * waist) - (.082 * weight)) / weight) * 100;

        }
        return BF;

    }

    public double calculateBF(String wt,String wis, String sex, String unit) {
        double weight=((Float.parseFloat(wt)) != 0) ? (Float.parseFloat(wt)) : 1;
        double waist = Float.parseFloat(wis);

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

    public View fragamentHome(View view){
        datasource=new DB_datasource(this.getActivity());
        datasource.open();
        settings=getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        final String height=settings.getString("height","0");
        String weight=datasource.findLastWeight();
        final TextView input_weight   = (TextView) view.findViewById(R.id.output_weight);
        input_weight.setText(weight);

        int height_int=((Integer.parseInt(height)) != 0) ? (Integer.parseInt(height)) : 1;
        final TextView input_BMI=(TextView)view.findViewById(R.id.output_BMI);
        String unit=settings.getString("pref_units","male");
        float BMI;
        if(unit.equals("std")){
           Float height_meter=height_int*0.0254f;
            BMI=(Float.parseFloat(weight))/(height_meter*height_meter);
        }else{
            BMI=(float)(((Float.parseFloat(weight)))*703)/(height_int*height_int);
        }
       //float BMI=(float)((Integer.parseInt(weight))*703)/(height_int*height_int);
        //String BMI=datasource.findLastBMI();
        input_BMI.setText(String.format("%.2f", BMI));

        final TextView input_BF=(TextView)view.findViewById(R.id.output_BF);
       final String waist=datasource.findLastWaist();
        String sex=settings.getString("pref_sex","male");
        double BF=calculateBF(weight,waist,sex,unit);

        input_BF.setText(String.format("%.2f",BF));

        int a=0;
        int b=0;
        int c=0;
        final String targetweight=settings.getString("targetweight","0");
        int weightDiff=Math.abs(Integer.parseInt(targetweight)-Integer.parseInt(weight));
        if(weightDiff<5) {
            a=255-(200-(weightDiff*20));
            b=255;
        }
        else if (weightDiff<10) {
            a=255-(150-(weightDiff*10));
            b=150;
        }
        else if (weightDiff<25){
            a=180;

        }
        int weight_color= Color.argb(255, a, b, c);
        LinearLayout weight_layout=(LinearLayout)view.findViewById(R.id.linearLayoutWeight);
        GradientDrawable temp=(GradientDrawable) weight_layout.getBackground();
        temp.setStroke(20, weight_color);


        //BMI COLOR

        if (BMI<18.5  ){
            a=180;
            b=0;
            c=0;

        } else if (BMI>29.9){
            a=180;
            b=0;
            c=0;

        }else if (BMI <=29.9 && (BMI) >=25.0){
            a=225;
            b=113;
            c=15;
        }
        else if ((BMI)<25 && (BMI)>=18.5){
            a=0;
            b=255;
            c=0;
        }
        int bmi_color=Color.argb(255,a,b,c);
        LinearLayout bmi_layout=(LinearLayout)view.findViewById(R.id.linearLayoutBMI);
        temp=(GradientDrawable) bmi_layout.getBackground();
        temp.setStroke(20, bmi_color);

        //BF color
        if(sex.equals("male")){
            if((BF)<=17 ){
                a=0; b=255; c=0;
            }else if ((BF)>17 && (BF)<=24){
                a=225;
                b=113;
                c=15;
            }else{
                a=180;
                b=0;
                c=0;
            }

        }else if (sex.equals("female")){
            if ((BF)<=24 ){
                a=0; b=255; c=0;
            }else if ((BF)>24 && (BF)<=31){
                a=225;
                b=113;
                c=15;
            }else{
                a=180;
                b=0;
                c=0;
            }
        }
        int bf_color=Color.argb(255,a,b,c);
        LinearLayout bf_layout=(LinearLayout)view.findViewById(R.id.linearLayoutBF);
        temp=(GradientDrawable) bf_layout.getBackground();
        temp.setStroke(20, bf_color);
        return view;
    }
    public View fragmentLogger(View view){

        datasource=new DB_datasource(this.getActivity());
        datasource.open();
        myListView=(ListView) view.findViewById(R.id.mylist);
        String[] stockArr = new String[datasource.findDate().size()];
        strListView = datasource.findDate().toArray(stockArr);
         ArrayAdapter<String> objadapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,strListView);
          myListView.setAdapter(objadapter);
        myListView.setOnItemClickListener(this);
        return view;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(this.getActivity(),ActivityLoggerDetail.class);
        i.putExtra("chosen", position);
        startActivity(i);
    }


    public void onClick(View v) {

    }
}

