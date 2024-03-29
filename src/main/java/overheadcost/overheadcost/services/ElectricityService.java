package overheadcost.overheadcost.services;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.Electricity;
import overheadcost.overheadcost.entities.LastElectricityRead;
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
                .max(Comparator.comparing(Electricity::getActualDate))
                .orElse(null);

    }

    public String getSellPercentage() {
        float produced = 0;
        float sold = 0;
        var electList = electricityRepo.findAll();
        Collections.sort(electList, Comparator.comparing(Electricity::getActualDate));
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
        return findAll().stream().map(Electricity::getActualDate).toList();
    }

    public List<Electricity> findAll() {
        List<Electricity> resultList = electricityRepo.findAll();
        Collections.sort(resultList, Comparator.comparing(Electricity::getActualDate));
        return resultList;
    }

    public static List<MonthlyConsumptionStatData> getChartData(List<Electricity> electricities,
            LastElectricityRead lastElectricityRead) {
        List<MonthlyConsumptionStatData> chartDataList = new ArrayList<>();

        Collections.sort(electricities, Comparator.comparing(Electricity::getActualDate));
        int maxSize = Math.min(CommonService.MAX_CHART_MONTHS, electricities.size());
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

    // @PostConstruct
    public void init() {

        electricityRepo.save(new Electricity(4937, 7071, 1722, 5498, LocalDate.of(2023, 8, 30)));
        electricityRepo.save(new Electricity(5489, 9123, 936, 1642, LocalDate.of(2023, 10, 31)));
        electricityRepo.save(new Electricity(6009, 9592, 571, 1591, LocalDate.of(2023, 11, 30)));

    }
}
