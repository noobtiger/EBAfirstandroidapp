package eba.bodyloger;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;

/**
 * Created by c-mchennur on 3/19/2015.
 */
public class ActivityFullWeightChart extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);
        TextView title = (TextView) findViewById(R.id.fullchartTitle);
        title.setText("Weight");
        DB_datasource datasource = new DB_datasource(this);
        datasource.open();
        Double weight;
        int maxID = datasource.maxID();
        XYSeries weightSeries = new XYSeries("Weight");
        DecimalFormat newFormat = new DecimalFormat("#.##");
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
        incomeRenderer.setColor(Color.rgb(78,139,245));
        incomeRenderer.setFillPoints(true);
        // incomeRenderer.setLineWidth(2);
        incomeRenderer.setChartValuesTextSize(25);
        incomeRenderer.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        //multiRenderer.setMarginsColor(weight_color/2);

        multiRenderer.setZoomButtonsVisible(false);
        Double maxweight = weightSeries.getMaxY();
        multiRenderer.setYAxisMax(maxweight+50);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setBarWidth(60f);
        multiRenderer.setBarSpacing(0.25);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowAxes(true);
        multiRenderer.setShowGrid(true);
        multiRenderer.setGridColor(Color.rgb(41,40,40));
        multiRenderer.setAxesColor(Color.rgb(41,40,40));

//        multiRenderer.setChartTitle("WEIGHT");
//        multiRenderer.setChartTitleTextSize(50f);




        multiRenderer.addSeriesRenderer(incomeRenderer);

        LinearLayout wChartContainer = (LinearLayout)findViewById(R.id.chart_weight);

        View weightChart = ChartFactory.getBarChartView(this.getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        wChartContainer.addView(weightChart);

    }
}
