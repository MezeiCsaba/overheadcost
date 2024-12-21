package overheadcost.overheadcost.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GasChartModel {
    private String date;
    private int consumption;
}
