package overheadcost.overheadcost.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonthlyGasConsumptionDataChartModel {
    private String month;
    private float gasDataFirst;
    private float gasDataSecond;
}
