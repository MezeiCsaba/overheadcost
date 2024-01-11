package overheadcost.overheadcost.entities;

public class GasChartModel {

    String date;
    int gas;
    public GasChartModel() {
    }
    public GasChartModel(String date, int gas) {
        this.date = date;
        this.gas = gas;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getGas() {
        return gas;
    }
    public void setGas(int gas) {
        this.gas = gas;
    }
    @Override
    public String toString() {
        return "GasChartModel [date=" + date + ", gas=" + gas + "]";
    }

    

}
