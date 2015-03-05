package eba.bodyloger;

public class DB_Model {

    private long id;
    private String date;
    private double weight;
    private double waist;
    private double neck;
    private double BF;
    private double BMI;

    public DB_Model(){

    }
    public DB_Model(String date,double weight){
        this.date=date;
        this.weight=weight;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getdate() {
        return date;
    }
    public void setdate(String date) {
        this.date = date;
    }

    public double getweight() {
        return weight;
    }
    public void setweight(double weight) {
        this.weight = weight;
    }

    public double getwaist() {
        return waist;
    }
    public void setwaist(double waist) {
        this.waist = waist;
    }

    public double getneck() {
        return neck;
    }
    public void setneck(double neck) {
        this.neck = neck;
    }
    public double getBF() {
        return BF;
    }
    public void setBF(double BF) {
        this.BF = BF;
    }
    public double getBMI() {
        return BMI;
    }
    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

}
