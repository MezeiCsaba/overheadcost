package overheadcost.overheadcost.entities;

public class MonthlyConsumptionStatData {

    private int t180;
    private int t280;
    private int headOver;
    private int solar;
    private int consumption;
    private String date;

    public MonthlyConsumptionStatData() {
    };

    

    public MonthlyConsumptionStatData(int t180, int t280, int headOver, int solar, int calculatedConsumption,
            String date) {
        this.t180 = t180;
        this.t280 = t280;
        this.headOver = headOver;
        this.solar = solar;
        this.consumption = calculatedConsumption;
        this.date = date;
    }



    public int getT180() {
        return t180;
    }

    public void setT180(int t180) {
        this.t180 = t180;
    }

    public int getT280() {
        return t280;
    }

    public void setT280(int t280) {
        this.t280 = t280;
    }

    public int getHeadOver() {
        return headOver;
    }

    public void setHeadOver(int headOver) {
        this.headOver = headOver;
    }

    public int getSolar() {
        return solar;
    }

    public void setSolar(int solar) {
        this.solar = solar;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int calculatedConsumption) {
        this.consumption = calculatedConsumption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MonthlyConsumptionStatData [t180=" + t180 + ", t280=" + t280 + ", headOver=" + headOver + ", solar="
                + solar + ", calculatedConsumption=" + consumption + ", date=" + date + "]";
    }

}
