package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CommonService {

    /**
     *
     */
    static final int MAX_CHART_MONTHS = 5;

    public static boolean containsSameMonthYear(List<LocalDate> localDateList, LocalDate actualDate) {

        if (actualDate == null)
            return true;
        int actualYear = actualDate.getYear();
        int actualMonth = actualDate.getMonthValue();

        return localDateList.stream()
                .anyMatch(currentDate -> currentDate.getYear() == actualYear
                        && currentDate.getMonthValue() == actualMonth);
    }

    

}
