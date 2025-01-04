package overheadcost.overheadcost.services;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.Electricity;
import overheadcost.overheadcost.entities.MonthlyConsumptionStatData;
import overheadcost.overheadcost.repository.ElectricityRepository;

@Service
public class ElectricityService {

    private ElectricityRepository electricityRepo;

    @Autowired
    public void setElectricityRepo(ElectricityRepository electricityRepo) {
        this.electricityRepo = electricityRepo;
    }

    public Electricity getLastElectricity(LocalDate invDate) {
        return electricityRepo.findAll().stream()
                .max(Comparator.comparing(Electricity::getDate))
                .orElse(null);

    }

    public String getSellPercentage() {
        float produced = 0;
        float sold = 0;
        var electList = findAll();
        for (int i = electList.size() - 1; i > 0; i--) {
            if (electList.get(i).getT280() > 0 && electList.get(i - 1).getT280() > 0
                    && electList.get(i).getSolar() > 0) {
                sold += electList.get(i).getT280() - electList.get(i - 1).getT280();
                produced += electList.get(i).getSolar();
            }
        }
        ;

        DecimalFormat df = new DecimalFormat("##%");
        String resultString = df.format((sold * produced) > 0 ? sold / produced : 0);

        return resultString;

    }

    public void save(Electricity electricity) {
        if (electricity != null)
            electricityRepo.save(electricity);
    }

    public List<LocalDate> getAllLocalDateFrom() {
        return findAll().stream().map(Electricity::getDate).toList();
    }

    public List<Electricity> findAll() {
        List<Electricity> resultList = electricityRepo.findAll();
        Collections.sort(resultList, Comparator.comparing(Electricity::getDate));
        return resultList;
    }

    public List<MonthlyConsumptionStatData> getChartData(Boolean isDayType) {
        List<MonthlyConsumptionStatData> chartDataList = new ArrayList<>();
        var electricities = findAll();
        int maxSize = calculateMaxSize(isDayType, electricities.size());
        int startIndex = electricities.size() - maxSize;

        for (int i = startIndex; i < electricities.size(); i++) {
            Electricity currentElectricity = electricities.get(i);
            Electricity previousElectricity = (i > 0) ? electricities.get(i - 1) : null;

            String date = extractDate(currentElectricity);
            int sell = calculateSell(currentElectricity, previousElectricity);
            int buy = calculateBuy(currentElectricity, previousElectricity);
            float calculatedConsumption = calculateConsumption(currentElectricity, sell, buy, isDayType);
            int solar = calculateSolar(currentElectricity, sell, isDayType);

            chartDataList.add(new MonthlyConsumptionStatData(buy, sell, currentElectricity.getDifference(),
                    solar, calculatedConsumption, date));
        }

        return chartDataList;
    }

    private int calculateMaxSize(Boolean isDayType, int size) {
        return isDayType ? Math.min(33, size - 1) : Math.min(CommonService.MAX_CHART_MONTHS, size);
    }

    private String extractDate(Electricity electricity) {
        return electricity.getDate().toString().substring(2, 7);
    }

    private int calculateSell(Electricity current, Electricity previous) {
        return (previous != null) ? current.getT280() - previous.getT280() : 0;
    }

    private int calculateBuy(Electricity current, Electricity previous) {
        return (previous != null) ? current.getT180() - previous.getT180() : 0;
    }

    private float calculateConsumption(Electricity electricity, int sell, int buy, Boolean isDayType) {
        LocalDate actualDate = electricity.getDate();
        int numberOfDaysInMonth = isDayType
                ? YearMonth.of(actualDate.getYear(), actualDate.getMonthValue()).lengthOfMonth()
                : 1;
        float calculatedConsumption = (float)(electricity.getSolar() - sell + buy) / numberOfDaysInMonth;
        return Math.max(calculatedConsumption, 0);
    }

    private int calculateSolar(Electricity electricity, int sell, Boolean isDayType) {
        return isDayType ? (100 * sell / electricity.getSolar()) : electricity.getSolar();
    }

    public Map<String, Integer> getLast12MonthsData() {
        Map<String, Integer> yearlyData = new LinkedHashMap<>();
        var electricities = findAll();
        if (electricities.isEmpty()) {
            return yearlyData;
        }
        int totalSold = 0;
        int totalBought = 0;
        int totalSolar = 0;
        int totalConsumption = 0;

        LocalDate endDate = (electricities.get(electricities.size() - 1).getDate());
        LocalDate startDate = endDate.minusMonths(11);

        for (int i = 0; i < electricities.size(); i++) {
            Electricity electricity = electricities.get(i);
            if (!electricity.getDate().isBefore(startDate) && !electricity.getDate().isAfter(endDate)) {
                System.out.println(electricity.getDate());
                Electricity currentElectricity = electricities.get(i);
                Electricity previousElectricity = (i > 0) ? electricities.get(i - 1) : null;

                int sell = calculateSell(currentElectricity, previousElectricity);
                int buy = calculateBuy(currentElectricity, previousElectricity);
                float calculatedConsumption = calculateConsumption(currentElectricity, sell, buy, false);
                int solar = calculateSolar(currentElectricity, sell, false);
                totalSold += sell;
                totalBought += buy;
                totalSolar += solar;
                totalConsumption += calculatedConsumption;
            }
        }
        yearlyData.put("Purchased energy", totalBought);
        yearlyData.put("Solar energy sold", totalSold);
        yearlyData.put("Solar production", totalSolar);
        yearlyData.put("Consumption", totalConsumption);

        return yearlyData;
    }

    // @PostConstruct
    public void init() {

        electricityRepo.save(new Electricity(4751, 5575, 1875, 0, LocalDate.of(2023, 7, 30)));
        electricityRepo.save(new Electricity(4937, 7071, 1722, 5498, LocalDate.of(2023, 8, 30)));
        electricityRepo.save(new Electricity(5203, 8340, 1420, 1145, LocalDate.of(2023, 9, 30)));
        electricityRepo.save(new Electricity(5489, 9123, 936, 1642, LocalDate.of(2023, 10, 31)));
        electricityRepo.save(new Electricity(6009, 9592, 571, 1591, LocalDate.of(2023, 11, 30)));
        electricityRepo.save(new Electricity(6724, 9865, 340, 1149, LocalDate.of(2023, 12, 31)));

    }
}
