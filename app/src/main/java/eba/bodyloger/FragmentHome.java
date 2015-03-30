package eba.bodyloger;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Admin on 1/29/2015.
 */
public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener {
    private SharedPreferences settings;
    DB_datasource datasource;
    private ListView myListView;
    private String[] strListView;
    private String[] strListViewWeight;
    String strweightDiff;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static int weight_color;
    private static int bmi_color;
    private static int bf_color;

    private static final int CAMERA_REQUEST = 1888;


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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        mPage = getArguments().getInt(ARG_PAGE);
        if(mPage==1) {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            fragamentHome(view);

            openBMIChart(view);
            openBFChart(view);
            openWeightChart(view);


            final LinearLayout layout_BMI=(LinearLayout)view.findViewById(R.id.linearLayoutBMI);
            final LinearLayout layout_weight=(LinearLayout)view.findViewById(R.id.linearLayoutWeight);
            final LinearLayout layout_BF=(LinearLayout)view.findViewById(R.id.linearLayoutBF);

            final LinearLayout chart_weight=(LinearLayout)view.findViewById(R.id.chart_weight);
            final LinearLayout chart_BMI=(LinearLayout)view.findViewById(R.id.chart_BMI);
            final LinearLayout chart_BF=(LinearLayout)view.findViewById(R.id.chart_BF);



            chart_weight.setVisibility(View.VISIBLE);
            chart_BF.setVisibility(View.INVISIBLE);
            chart_BMI.setVisibility(View.INVISIBLE);

            chart_BF.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent in = new Intent(getActivity(),ActivityFullBFChart.class);
                    startActivity(in);
                    return true;

                }
            });
            chart_BMI.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent in = new Intent(getActivity(),ActivityFullBMIChart.class);
                    startActivity(in);
                    return true;

                }
            });
            chart_weight.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent in = new Intent(getActivity(),ActivityFullWeightChart.class);
                    startActivity(in);
                    return true;

                }
            });

            layout_BMI.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    chart_weight.setVisibility(View.INVISIBLE);
                    chart_BF.setVisibility(View.INVISIBLE);
                    chart_BMI.setVisibility(View.VISIBLE);
                }
            });
            layout_weight.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    chart_weight.setVisibility(View.VISIBLE);
                    chart_BF.setVisibility(View.INVISIBLE);
                    chart_BMI.setVisibility(View.INVISIBLE);
                }
            });
            layout_BF.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    chart_weight.setVisibility(View.INVISIBLE);
                    chart_BF.setVisibility(View.VISIBLE);
                    chart_BMI.setVisibility(View.INVISIBLE);
                }
            });


        }else if(mPage==2){
            view = inflater.inflate(R.layout.fragment_logger, container, false);
            fragmentLogger(view);
        }else{
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            fragamentProfile(view);
        }



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
        Double dWeight = Double.parseDouble(weight);
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
        int weightDiff=Math.abs(Integer.parseInt(targetweight)-(dWeight.intValue()));
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
         weight_color= Color.argb(255, a, b, c);
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
         bmi_color=Color.argb(255,a,b,c);
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
         bf_color=Color.argb(255,a,b,c);
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
        String[] myArr = new String[datasource.findWeight().size()];
        strListViewWeight = datasource.findWeight().toArray(myArr);
       Double[] dstrArrayList = new Double[strListViewWeight.length];
        ArrayList abcd = new ArrayList(stockArr.length);
         for (int i = 0; i < stockArr.length; i++) {
             Double dstrWeight = Double.parseDouble(strListViewWeight[i]);
             dstrArrayList[i] = dstrWeight;
         }

        ArrayList<RowItem> rowItems = new ArrayList<RowItem>();
        for (int i = 1; i < stockArr.length; i++) {


            if(i==0) {
                strweightDiff = String.valueOf((dstrArrayList[i].intValue()) - (dstrArrayList[i].intValue()));
            }else{
                strweightDiff = String.valueOf((dstrArrayList[i].intValue()) - (dstrArrayList[i-1].intValue()));
            }
            RowItem item = new RowItem(strListView[i], strListViewWeight[i], strweightDiff);
            rowItems.add(item);
        }
        String[] rows = new String[rowItems.size()];
     Collections.reverse(rowItems);
        MyAdapter adapter = new MyAdapter(this.getActivity(), rowItems);

       // ArrayAdapter<String> objadapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,rowItems.toArray(rows));
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(this);
        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(this.getActivity(),ActivityLoggerDetail.class);
        i.putExtra("chosen", position);
        startActivity(i);
    }



    //Graph CODE
    private void openWeightChart(View v){

        Double weight;
        int maxID = datasource.maxID();
        XYSeries weightSeries = new XYSeries("Weight");

       if (maxID>10) {
            for (int k = 0; k < 17; k++) {
                // ID[k] = maxID-k;
                weight = datasource.findRowWeight(maxID - (15 - k));
                weightSeries.add(k, weight);
            }
        }else
       {
           for(int k=1;k<15;k++){
               weight=datasource.findRowWeight(k);
               weightSeries.add(k,weight);
           }
       }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(weightSeries);


        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
       // incomeRenderer.setColor(Color.rgb(78,139,245));
       incomeRenderer.setColor(weight_color);
        incomeRenderer.setFillPoints(true);
       // incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(false);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        //multiRenderer.setMarginsColor(weight_color/2);

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setDisplayValues(false);

        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setBarWidth(40f);
        multiRenderer.setBarSpacing(0.25);
       multiRenderer.setShowLegend(false);
        multiRenderer.setShowAxes(false);
     multiRenderer.setShowLabels(false);

//        multiRenderer.setChartTitle("WEIGHT");
//        multiRenderer.setChartTitleTextSize(50f);




        multiRenderer.addSeriesRenderer(incomeRenderer);
        if(v.equals(null)) {
            v = this.getView();
        }
        LinearLayout wChartContainer = (LinearLayout)v.findViewById(R.id.chart_weight);

        View weightChart = ChartFactory.getBarChartView(getActivity().getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        wChartContainer.addView(weightChart);

    }

    public void openBMIChart(View v){

        Double weight;
        int maxID = datasource.maxID();
        XYSeries weightSeries = new XYSeries("Weight");

        if (maxID>10) {
            for (int k = 0; k < 17; k++) {
                // ID[k] = maxID-k;
                weight = datasource.findRowBMI(maxID - (15 - k));
                weightSeries.add(k, weight);
            }
        }else
        {
            for(int k=1;k<15;k++){
                weight=datasource.findRowBMI(k);
                weightSeries.add(k,weight);
            }
        }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
//        weightSeries.addAnnotation("BMI",weightSeries.getMaxX(),weightSeries.getMaxY());
        dataset.addSeries(weightSeries);



        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        // incomeRenderer.setColor(Color.rgb(78,139,245));
        incomeRenderer.setColor(bmi_color);
        incomeRenderer.setFillPoints(true);

        // incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(false);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        //multiRenderer.setMarginsColor(weight_color/2);

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setDisplayValues(false);
        //multiRenderer.setYAxisMax();
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setBarWidth(40f);
        multiRenderer.setBarSpacing(0.25);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowAxes(false);
        multiRenderer.setShowLabels(false);

//        multiRenderer.setChartTitle("WEIGHT");
//        multiRenderer.setChartTitleTextSize(50f);




        multiRenderer.addSeriesRenderer(incomeRenderer);
        if(v.equals(null)) {
            v = this.getView();
        }
        LinearLayout wChartContainer = (LinearLayout)v.findViewById(R.id.chart_BMI);

        View weightChart = ChartFactory.getBarChartView(getActivity().getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        wChartContainer.addView(weightChart);

    }

    private void openBFChart(View v){

        Double weight;
        int maxID = datasource.maxID();
        XYSeries weightSeries = new XYSeries("Weight");

        if (maxID>10) {
            for (int k = 0; k < 17; k++) {
                // ID[k] = maxID-k;
                weight = datasource.findRowBF(maxID - (15 - k));
                weightSeries.add(k, weight);
            }
        }else
        {
            for(int k=1;k<15;k++){
                weight=datasource.findRowBF(k);
                weightSeries.add(k,weight);
            }
        }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(weightSeries);


        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        // incomeRenderer.setColor(Color.rgb(78,139,245));
        incomeRenderer.setColor(bf_color);
        incomeRenderer.setFillPoints(true);
        // incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(false);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        //multiRenderer.setMarginsColor(weight_color/2);

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setDisplayValues(false);
        //multiRenderer.setYAxisMax();
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setBarWidth(40f);
        multiRenderer.setBarSpacing(0.25);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowAxes(false);
        multiRenderer.setShowLabels(false);

//        multiRenderer.setChartTitle("WEIGHT");
//        multiRenderer.setChartTitleTextSize(50f);




        multiRenderer.addSeriesRenderer(incomeRenderer);
        if(v.equals(null)) {
            v = this.getView();
        }
        LinearLayout wChartContainer;
        wChartContainer = (LinearLayout)v.findViewById(R.id.chart_BF);
        View weightChart = ChartFactory.getBarChartView(getActivity().getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        wChartContainer.addView(weightChart);

    }

    //Fragment Profile CODE - Chait
    public View fragamentProfile(View view) {

        //Setting the name at the top of the page from the shared preferences
        TextView profileName=(TextView)view.findViewById(R.id.profile_name);
        settings=getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        profileName.setText(settings.getString("name","0"));

        //Setting onclickevents for buttons
        Button profileWeight=(Button)view.findViewById(R.id.profileButtonWeight);
        Button profileBMI=(Button)view.findViewById(R.id.profileButtonBMI);
        Button profileBF=(Button)view.findViewById(R.id.profileButtonBF);

        profileWeight.setBackgroundColor(weight_color);
        profileBF.setBackgroundColor(bmi_color);
        profileBMI.setBackgroundColor(bf_color);

        profileBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),ProfileBMI.class);
                startActivity(in);
            }
        });

        profileBF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),ProfileBF.class);
                startActivity(in);
            }
        });

        profileWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),ProfileWeight.class);
                startActivity(in);
            }
            });

            //Camera Code
            openCamera(view);
    return view;
    }
    private void openCamera(View view){
        ImageView imageBefore=(ImageView)view.findViewById(R.id.imageBefore);
        ImageView imageAfter=(ImageView)view.findViewById(R.id.imageAfter);
        imageBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(getActivity(),ProfileWeight.class);
//                startActivity(in);
            }
        });
        imageAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(getActivity(),ProfileWeight.class);
//                startActivity(in);
            }
        });

    }
}

