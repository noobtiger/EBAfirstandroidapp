package eba.bodyloger;

/**
 * Created by c-mchennur on 3/13/2015.
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<RowItem> {

    private final Context context;
    private final ArrayList<RowItem> itemsArrayList;

    public MyAdapter(Context context, ArrayList<RowItem> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView dateView = (TextView) rowView.findViewById(R.id.label);
        TextView weightView = (TextView) rowView.findViewById(R.id.value);
        TextView weightDiffView = (TextView) rowView.findViewById(R.id.valueDiff);

        // 4. Set the text for textView
        dateView.setText(itemsArrayList.get(position).getDate());
        weightView.setText("Weight: " + itemsArrayList.get(position).getWeight());
        weightDiffView.setText(itemsArrayList.get(position).getWeightDiff());

        if(Integer.parseInt(itemsArrayList.get(position).getWeightDiff())<0){
            weightDiffView.setTextColor(Color.rgb(38,124,4));
        }else if(Integer.parseInt(itemsArrayList.get(position).getWeightDiff())>0){
            weightDiffView.setTextColor(Color.rgb(166,8,8));
        }

        // 5. retrn rowView
        return rowView;
    }
}
