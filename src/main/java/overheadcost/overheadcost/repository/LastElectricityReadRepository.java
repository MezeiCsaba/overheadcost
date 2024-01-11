package overheadcost.overheadcost.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overheadcost.overheadcost.entities.LastElectricityRead;

@Repository
public interface LastElectricityReadRepository extends JpaRepository<LastElectricityRead, Long> {
    LastElectricityRead findByActualDate(LocalDate actualDate);
}
