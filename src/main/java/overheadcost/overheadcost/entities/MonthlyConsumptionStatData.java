package overheadcost.overheadcost.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonthlyConsumptionStatData {
    private int t180;
    private int t280;
    private int headOver;
    private int solar;
    private int consumption;
    private String date;
}
