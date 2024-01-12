package overheadcost.overheadcost.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overheadcost.overheadcost.entities.LastGasModel;
import overheadcost.overheadcost.repository.LastGasRepository;

@Service
public class LastGasReadService {

    private LastGasRepository lastGasRepository;

    @Autowired
    public void setLastGasRepository(LastGasRepository lastGasRepository) {
        this.lastGasRepository = lastGasRepository;
    }

    public LastGasModel getLastGas(LocalDate invDate) {
        return lastGasRepository.findAll().stream()
                .filter(lastGas -> lastGas.getDate().isAfter(invDate))
                .min(Comparator.comparing(LastGasModel::getDate))
                .orElseGet(() -> lastGasRepository.findAll().stream()
                        .max(Comparator.comparing(LastGasModel::getDate))
                        .orElse(null));

    }

    public void save(LastGasModel gas) {
        if (gas!=null) lastGasRepository.save(gas);
    }

    public List<LocalDate> getAllLocalDateFrom() {
        return findAll().stream().map(LastGasModel::getDate).toList();
    }

    public List<LastGasModel> findAll() {
        return lastGasRepository.findAll();
    }

    //@PostConstruct
    public void init() {

        lastGasRepository.save(new LastGasModel(LocalDate.of(2022, 3, 12), 17755));
        lastGasRepository.save(new LastGasModel(LocalDate.of(2023, 3, 17), 19326));

    }

}
