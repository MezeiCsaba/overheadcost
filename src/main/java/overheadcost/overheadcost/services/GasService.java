package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.GasChartModel;
import overheadcost.overheadcost.entities.GasModel;
import overheadcost.overheadcost.entities.LastGasModel;
import overheadcost.overheadcost.entities.MonthlyGasConsumptionDataChartModel;
import overheadcost.overheadcost.repository.GasRepository;
import overheadcost.overheadcost.repository.LastGasRepository;

@Service
public class GasService {

    private GasRepository gasRepository;
    private LastGasRepository lastGasRepository;
    private static final int GAS_LIMIT = 1729;

    @Autowired
    public void setLastGasRepository(LastGasRepository lastGasRepository) {
        this.lastGasRepository = lastGasRepository;
    }

    @Autowired
    public void setGasRepository(GasRepository gasRepository) {
        this.gasRepository = gasRepository;
    }

    public GasModel getLastGas(LocalDate invDate) {
        return gasRepository.findAll().stream()
                .max(Comparator.comparing(GasModel::getDate))
                .orElse(null);

    }

    public List<GasChartModel> getGasChartData() {
        List<GasModel> sourceGasList = gasRepository.findAll();
        Collections.sort(sourceGasList, Comparator.comparing(GasModel::getDate));
        List<GasChartModel> resultList = new ArrayList<>();
        for (int i = 1; i < sourceGasList.size(); i++) {
            resultList.add(new GasChartModel(sourceGasList.get(i).getDate().toString().substring(2, 7),
                    sourceGasList.get(i).getGas() - sourceGasList.get(i - 1).getGas()));
        }
        return resultList;
    }

    public List<MonthlyGasConsumptionDataChartModel> getGasDifferenceChartData() {
        List<GasChartModel> fromDateList = new ArrayList<>();
        List<GasChartModel> toDateList = new ArrayList<>();
        List<MonthlyGasConsumptionDataChartModel> resultList = new ArrayList<>();
        var lastGasRead = lastGasRepository.findAll();
        Collections.sort(lastGasRead, Comparator.comparing(LastGasModel::getDate));
        var lastGasReadFirst = lastGasRead.get(1);
        var lastGasReadSecond = lastGasRead.get(0);

        LocalDate lastReadFirstDate = lastGasReadFirst.getDate();
        int lastReadFirstValue = lastGasReadFirst.getGas();
        int lastReadSecondValue = lastGasReadSecond.getGas();
        List<GasModel> sourceGasList = gasRepository.findAll();
        Collections.sort(sourceGasList, Comparator.comparing(GasModel::getDate));
        int listIndex = findIndexByDate(sourceGasList, lastReadFirstDate);
        int lastValue = 0;
        int currentValue = 0;
        int chartValue = 0;

        for (int i = listIndex; i < sourceGasList.size(); i++) {
            String currentDate = sourceGasList.get(i).getDate().toString().substring(5, 7);
            int currentGasValue = sourceGasList.get(i).getGas();
            currentValue = (i == listIndex) ? (currentGasValue - lastReadFirstValue) : currentGasValue - lastValue;
            chartValue += currentValue;
            fromDateList.add(new GasChartModel(currentDate, chartValue));
            lastValue = currentGasValue;
        }

        lastValue = lastReadSecondValue;
        currentValue = 0;
        chartValue = 0;

        int firstListIndex = (listIndex - 12) < 0 ? 0 : (listIndex - 12);
        for (int i = firstListIndex; i < listIndex; i++) {
            String currentDate = sourceGasList.get(i).getDate().toString().substring(5, 7);
            int currentGasValue = sourceGasList.get(i).getGas();
            currentValue = currentGasValue - lastValue;
            chartValue += currentValue;
            toDateList.add(new GasChartModel(currentDate, chartValue));
            lastValue = currentGasValue;
        }

        for (int i = 0; i < 12; i++) {
            int fromDateListSize = fromDateList.size();
            int toDateListSize = toDateList.size();
            int fromDateGas = (i < fromDateListSize) ? fromDateList.get(i).getGas()
                    : fromDateList.get(fromDateListSize - 1).getGas();
            int toDategas = (i < toDateListSize) ? toDateList.get(i).getGas()
                    : toDateList.get(toDateListSize - 1).getGas();
            String date = (i < toDateListSize) ? toDateList.get(i).getDate()
                    : (i < fromDateListSize) ? toDateList.get(i).getDate() : "na";

            resultList.add(new MonthlyGasConsumptionDataChartModel(date,
                    fromDateGas, toDategas));
        }

        return resultList;

    }

    private static int findIndexByDate(List<GasModel> sourceGasList, LocalDate targetDate) {
        return IntStream.range(0, sourceGasList.size())
                .filter(i -> sourceGasList.get(i).getDate()
                        .toString().substring(2, 7)
                        .equals(targetDate.toString().substring(2, 7)))
                .findFirst()
                .orElse(-1);
    }

    public LastGasModel getLastLastGasRead() {
        return lastGasRepository.findAll().stream()
                .max(Comparator.comparing(LastGasModel::getDate))
                .orElse(null);

    }

    public int[] getGasConsumptionLastYear() {
        int[] result = new int[2];

        int gas = getLastGas(LocalDate.now()).getGas();
        int gasRead = getLastLastGasRead().getGas();
        result[0] = gas - gasRead;
        result[1] = GAS_LIMIT;
        return result;

    }

    public void save(GasModel gas) {
        if (gas != null)
            gasRepository.save(gas);
    }

    public List<LocalDate> getAllLocalDateFrom() {
        return findAll().stream().map(GasModel::getDate).toList();
    }

    public List<GasModel> findAll() {
        List<GasModel> resultList = gasRepository.findAll();
        Collections.sort(resultList, Comparator.comparing(GasModel::getDate));
        return resultList;
    }

    // @PostConstruct
    public void init() {

        gasRepository.save(new GasModel(LocalDate.of(2022, 2, 28), 17621));
        gasRepository.save(new GasModel(LocalDate.of(2022, 3, 30), 17810));
        gasRepository.save(new GasModel(LocalDate.of(2022, 4, 30), 18000));
        gasRepository.save(new GasModel(LocalDate.of(2022, 5, 30), 18140));
        gasRepository.save(new GasModel(LocalDate.of(2022, 6, 30), 18212));
        gasRepository.save(new GasModel(LocalDate.of(2022, 7, 30), 18262));
        gasRepository.save(new GasModel(LocalDate.of(2022, 8, 28), 18315));
        gasRepository.save(new GasModel(LocalDate.of(2022, 9, 30), 18361));
        gasRepository.save(new GasModel(LocalDate.of(2022, 10, 30), 18445));
        gasRepository.save(new GasModel(LocalDate.of(2022, 11, 30), 18641));
        gasRepository.save(new GasModel(LocalDate.of(2022, 12, 30), 18847));
        gasRepository.save(new GasModel(LocalDate.of(2023, 1, 30), 19041));
        gasRepository.save(new GasModel(LocalDate.of(2023, 2, 28), 19225));
        gasRepository.save(new GasModel(LocalDate.of(2023, 3, 30), 19389));
        gasRepository.save(new GasModel(LocalDate.of(2023, 4, 30), 19504));
        gasRepository.save(new GasModel(LocalDate.of(2023, 5, 30), 19572));
        gasRepository.save(new GasModel(LocalDate.of(2023, 6, 30), 19622));
        gasRepository.save(new GasModel(LocalDate.of(2023, 7, 30), 19675));
        gasRepository.save(new GasModel(LocalDate.of(2023, 8, 30), 19721));
        gasRepository.save(new GasModel(LocalDate.of(2023, 9, 30), 19780));
        gasRepository.save(new GasModel(LocalDate.of(2023, 10, 30), 19849));
        gasRepository.save(new GasModel(LocalDate.of(2023, 11, 30), 19997));
        gasRepository.save(new GasModel(LocalDate.of(2023, 12, 30), 20230));

    }

}
