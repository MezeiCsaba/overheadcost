package overheadcost.overheadcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overheadcost.overheadcost.entities.LastGasModel;

@Repository
public interface LastGasRepository extends JpaRepository<LastGasModel, Long> {

}
