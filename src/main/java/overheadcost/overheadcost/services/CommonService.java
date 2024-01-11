package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.Electricity;
import overheadcost.overheadcost.entities.LastElectricityRead;
import overheadcost.overheadcost.entities.MonthlyConsumptionStatData;

@Service
public class CommonService {

    /**
     *
     */
    private static final int MAX_CHART_MONTHS = 5;

    public static boolean containsSameMonthYear(List<LocalDate> localDateList, LocalDate actualDate) {

        if (actualDate == null)
            return true;
        int actualYear = actualDate.getYear();
        int actualMonth = actualDate.getMonthValue();

        return localDateList.stream()
                .anyMatch(currentDate -> currentDate.getYear() == actualYear
                        && currentDate.getMonthValue() == actualMonth);
    }

    public static List<MonthlyConsumptionStatData> getChartData(List<Electricity> electricities,
            LastElectricityRead lastElectricityRead) {
        List<MonthlyConsumptionStatData> chartDataList = new ArrayList<>();

        Collections.sort(electricities, Comparator.comparing(Electricity::getActualDate));
        int maxSize = Math.min(MAX_CHART_MONTHS, electricities.size());
        int startIndex = electricities.size() - maxSize;
        int buy = 0;
        int sell = 0;

        for (int i = startIndex; i < electricities.size(); i++) {
            Electricity currentElectricity = electricities.get(i);
            Electricity previousElectricity = (i > 0) ? electricities.get(i - 1) : null;

            String date = currentElectricity.getActualDate().toString().substring(2, 7);

            if (previousElectricity != null) {
                sell = currentElectricity.getT280() - previousElectricity.getT280();
                buy = currentElectricity.getT180() - previousElectricity.getT180();
            }

            int calculatedConsumption = currentElectricity.getSolar() - sell + buy;
            calculatedConsumption = (calculatedConsumption < 0) ? 0 : calculatedConsumption;

            chartDataList.add(new MonthlyConsumptionStatData(buy, sell, currentElectricity.getDifference(),
                    currentElectricity.getSolar(), calculatedConsumption, date));
        }

        return chartDataList;
    }

    

}
