package overheadcost.overheadcost.entities;

public class GasChartModel {

    String date;
    int consumption;
    public GasChartModel() {
    }
    public GasChartModel(String date, int gas) {
        this.date = date;
        this.consumption = gas;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getConsumption() {
        return consumption;
    }
    public void setConsumption(int gas) {
        this.consumption = gas;
    }
    @Override
    public String toString() {
        return "GasChartModel [date=" + date + ", gas=" + consumption + "]";
    }

    

}
