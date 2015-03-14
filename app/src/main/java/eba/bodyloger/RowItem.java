package eba.bodyloger;

/**
 * Created by c-mchennur on 3/13/2015.
 */
public class RowItem {
    private String date;
    private String weight;
    private  String weightDiff;


    public RowItem(String date, String weight, String weightDiff) {
        this.date = date;
        this.weight = weight;
        this.weightDiff = weightDiff;
    }
    public String getDate() {
        return date;
    }

    public String getWeight() {
        return weight;
    }

    public String getWeightDiff() { return weightDiff; }

    @Override
    public String toString() {
        return date ;
    }
}
