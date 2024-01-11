package overheadcost.overheadcost.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overheadcost.overheadcost.entities.Electricity;


@Repository
public interface ElectricityRepository extends JpaRepository<Electricity, Long> {

    Electricity findByActualDate(LocalDate actualDate);

    

}
