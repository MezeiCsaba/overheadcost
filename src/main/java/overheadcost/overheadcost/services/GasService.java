package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.time.YearMonth;
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

    public List<GasChartModel> getGasChartData(Boolean isDayType) {
        List<GasModel> sourceGasList = gasRepository.findAll();
        Collections.sort(sourceGasList, Comparator.comparing(GasModel::getDate));
        List<GasChartModel> resultList = new ArrayList<>();

        var lastGasRead = getLastGasReadsList();
        Collections.sort(lastGasRead, Comparator.comparing(LastGasModel::getDate));
        int lastGasReadSize = lastGasRead.size();
        var isGasMeterReplacement = lastGasRead.get(lastGasReadSize - 1).getIsGasMeterReplacement();
        var lastGasReadMeterReplacement = lastGasRead.get(lastGasReadSize - 1);
        int maxSize = isDayType ? Math.min(33, sourceGasList.size())
                : Math.min(CommonService.MAX_CHART_MONTHS, sourceGasList.size());

        for (int i = sourceGasList.size() - maxSize; i < sourceGasList.size(); i++) {
            LocalDate actualDate = sourceGasList.get(i).getDate();
            int numberOfDaysInMonth = isDayType
                    ? YearMonth.of(actualDate.getYear(), actualDate.getMonthValue()).lengthOfMonth()
                    : 1;
            var actualConsumption = calculateActualConsumption(sourceGasList, i, numberOfDaysInMonth, isGasMeterReplacement, lastGasReadMeterReplacement);
            resultList.add(new GasChartModel(actualDate.toString().substring(2, 7), actualConsumption));
        }
        return resultList;
    }

    private float calculateActualConsumption(List<GasModel> sourceGasList, int index, int numberOfDaysInMonth, boolean isGasMeterReplacement, LastGasModel lastGasReadMeterReplacement) {
        float actualConsumption = (float)(sourceGasList.get(index).getConsumption() - sourceGasList.get(index - 1).getConsumption())
                / numberOfDaysInMonth;
        if (actualConsumption < 0 && isGasMeterReplacement) {
            actualConsumption = (lastGasReadMeterReplacement.getGas() - sourceGasList.get(index - 1).getConsumption()
                    + sourceGasList.get(index).getConsumption()) / numberOfDaysInMonth;
        }
        return actualConsumption;
    }

    public List<MonthlyGasConsumptionDataChartModel> getGasDifferenceChartData() {
        List<GasChartModel> fromDateList = new ArrayList<>();
        List<GasChartModel> toDateList = new ArrayList<>();
        List<MonthlyGasConsumptionDataChartModel> resultList = new ArrayList<>();
        var lastGasRead = getLastGasReadsList();
        Collections.sort(lastGasRead, Comparator.comparing(LastGasModel::getDate));
        int lastGasReadSize = lastGasRead.size();
        int shiftIndex = 1;
        var isGasMeterReplacement = lastGasRead.get(lastGasReadSize - 1).getIsGasMeterReplacement();
        var lastGasReadMeterReplacement = lastGasRead.get(lastGasReadSize - 1);
        if (isGasMeterReplacement) {
            shiftIndex = 2;
        }
        var lastGasReadFirst = lastGasRead.get(lastGasReadSize - shiftIndex);
        var lastGasReadSecond = lastGasRead.get(lastGasReadSize - (shiftIndex + 1));

        LocalDate lastReadFirstDate = lastGasReadFirst.getDate();
        int lastReadFirstValue = lastGasReadFirst.getGas();
        int lastReadSecondValue = lastGasReadSecond.getGas();
        List<GasModel> sourceGasList = gasRepository.findAll();
        Collections.sort(sourceGasList, Comparator.comparing(GasModel::getDate));
        int listIndex = findIndexByDate(sourceGasList, lastReadFirstDate);

        if (listIndex == -1)
            return resultList;

        populateGasChartData(fromDateList, sourceGasList, listIndex, lastReadFirstValue, isGasMeterReplacement, lastGasReadMeterReplacement);
        populateGasChartData(toDateList, sourceGasList, Math.max(0, listIndex - 12), lastReadSecondValue, isGasMeterReplacement, lastGasReadMeterReplacement);

        for (int i = 0; i <= 12; i++) {
            var fromDateGas = getGasValue(fromDateList, i);
            var toDateGas = getGasValue(toDateList, i);
            String date = getDate(fromDateList, toDateList, i);
            resultList.add(new MonthlyGasConsumptionDataChartModel(date, fromDateGas, toDateGas));
        }

        return resultList;
    }

    private void populateGasChartData(List<GasChartModel> gasChartDataList, List<GasModel> sourceGasList, int startIndex, int initialValue, boolean isGasMeterReplacement, LastGasModel lastGasReadMeterReplacement) {
        int lastValue = initialValue;
        int currentValue;
        int chartValue = 0;

        for (int i = startIndex; i < sourceGasList.size(); i++) {
            String currentDate = sourceGasList.get(i).getDate().toString().substring(5, 7);
            int currentGasValue = sourceGasList.get(i).getConsumption();
            currentValue = (i == startIndex) ? (currentGasValue - initialValue) : currentGasValue - lastValue;
            if (currentValue < 0 && isGasMeterReplacement) {
                currentValue = lastGasReadMeterReplacement.getGas() - sourceGasList.get(i - 1).getConsumption()
                        + currentGasValue;
            }

            chartValue += currentValue;
            gasChartDataList.add(new GasChartModel(currentDate, chartValue));
            lastValue = currentGasValue;
        }
    }

    private float getGasValue(List<GasChartModel> gasChartDataList, int index) {
        int size = gasChartDataList.size();
        return (index < size) ? gasChartDataList.get(index).getConsumption()
                : gasChartDataList.get(size - 1).getConsumption();
    }

    private String getDate(List<GasChartModel> fromDateList, List<GasChartModel> toDateList, int index) {
        int toDateListSize = toDateList.size();
        return (index < toDateListSize) ? toDateList.get(index).getDate()
                : (index < fromDateList.size()) ? fromDateList.get(index).getDate() : "na";
    }

    public List<LastGasModel> getLastGasReadsList() {
        return lastGasRepository.findAll();
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
        return getLastGasReadsList().stream()
                .max(Comparator.comparing(LastGasModel::getDate))
                .orElse(null);
    }

    public float[] getGasConsumptionLastYear() {
        float[] result = new float[2];
        var diffChartDataList = getGasDifferenceChartData();
        var consumedGas = diffChartDataList.get(diffChartDataList.size() - 1);
        result[0] = consumedGas.getGasDataFirst();
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
