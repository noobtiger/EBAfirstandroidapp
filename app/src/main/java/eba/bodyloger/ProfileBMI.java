package eba.bodyloger;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by c-cpinnama on 3/24/2015.
 */
public class ProfileBMI extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_bmi);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.ColorPrimary));
        bar.setBackgroundDrawable(cd);

    }
}
