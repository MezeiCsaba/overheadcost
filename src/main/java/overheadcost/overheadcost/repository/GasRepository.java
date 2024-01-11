package overheadcost.overheadcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overheadcost.overheadcost.entities.GasModel;
@Repository
public interface GasRepository extends JpaRepository<GasModel, Long> {

}
