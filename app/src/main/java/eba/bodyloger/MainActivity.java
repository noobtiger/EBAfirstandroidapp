package eba.bodyloger;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public  class MainActivity extends ActionBarActivity {
    SharedPreferences settings = null;
    DB_datasource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings=getSharedPreferences("eba.bodyloger",MODE_PRIVATE);
        datasource=new DB_datasource(this);
        datasource.open();

        ActionBar bar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyFragmentPageAdapter((getSupportFragmentManager()), MainActivity.this));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        // Center the tabs in the layout
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent i = new Intent(MainActivity.this, AddNew.class);
                startActivity(i);
                return true;
            case R.id.action_settings:
                i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
         if (settings.getBoolean("firstrun", true)) {
            Intent i=new Intent(MainActivity.this, FirstOpen.class);
            startActivity(i);
             createDefaultData();
            settings.edit().putBoolean("firstrun", false).commit();
        }


    }
    public void createDefaultData(){
        datasource.open();
        DB_Model model=new DB_Model();
        Double value;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        model.setdate(formattedDate);
        value = 0.0;
        model.setweight(value);
        value = 0.0;
        model.setwaist(value);
        value = 0.0;
        model.setneck(value);
        model.setBMI(value);
        model.setBF(value);
        model= datasource.create(model);


    }

}