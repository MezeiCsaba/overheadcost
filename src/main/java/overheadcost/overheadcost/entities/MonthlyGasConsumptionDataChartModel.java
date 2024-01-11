package overheadcost.overheadcost.entities;

public class MonthlyGasConsumptionDataChartModel {

    String month;
    int gasDataFirst;
    int gasDataSecond;

    public MonthlyGasConsumptionDataChartModel() {
    }

    public MonthlyGasConsumptionDataChartModel(String month, int gasDataFirst, int gasDataSecond) {
        this.month = month;
        this.gasDataFirst = gasDataFirst;
        this.gasDataSecond = gasDataSecond;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getGasDataFirst() {
        return gasDataFirst;
    }

    public void setGasDataFirst(int gasDataFirst) {
        this.gasDataFirst = gasDataFirst;
    }

    public int getGasDataSecond() {
        return gasDataSecond;
    }

    public void setGasDataSecond(int gasDataSecond) {
        this.gasDataSecond = gasDataSecond;
    }

    @Override
    public String toString() {
        return "MonthlyGasConsumptionDataChartModel [month=" + month + ", gasDataFirst=" + gasDataFirst
                + ", gasDataSecond=" + gasDataSecond + "]";
    }

}
