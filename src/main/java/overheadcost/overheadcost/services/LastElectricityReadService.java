package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.LastElectricityRead;
import overheadcost.overheadcost.repository.LastElectricityReadRepository;

@Service
public class LastElectricityReadService {

    private LastElectricityReadRepository lastElectricityReadRepo;

    @Autowired
    public void setLastElectricityReadRepo(LastElectricityReadRepository lastElectricityReadRepo) {
        this.lastElectricityReadRepo = lastElectricityReadRepo;
    }

    public LastElectricityRead getLastLastElectricityRead() {
        return lastElectricityReadRepo.findAll().stream()
                // .filter(lastElectricityRead -> lastElectricityRead.getActualDate().isAfter(LocalDate.now()))
                // .min(Comparator.comparing(LastElectricityRead::getActualDate))
                // .orElseGet(() -> lastElectricityReadRepo.findAll().stream()
                        .max(Comparator.comparing(LastElectricityRead::getActualDate))
                        .orElse(null);

    }

    public List<LocalDate> getAllLocalDateFrom() {
        return lastElectricityReadRepo.findAll().stream().map(LastElectricityRead::getActualDate).toList();
    }

    public void save(LastElectricityRead lastElectricityRead){
        lastElectricityReadRepo.save(lastElectricityRead);
    }

    //@PostConstruct
    public void init() {

        lastElectricityReadRepo.save(new LastElectricityRead(3364, 0, LocalDate.of(2023, 3, 17)));
        lastElectricityReadRepo.save(new LastElectricityRead(4899, 6891, LocalDate.of(2023, 8, 30)));
    }

}
