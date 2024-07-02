package overheadcost.overheadcost.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lastgas")
public class LastGasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int gas;
    private Boolean isGasMeterReplacement;

   

    public LastGasModel() {
    }

    public LastGasModel(LocalDate date, int gas) {
        this.date = date;
        this.gas = gas;
    }

    public LastGasModel(LocalDate date, int gas, Boolean isGasMeterReplacement) {
        this.date = date;
        this.gas = gas;
        this.isGasMeterReplacement = isGasMeterReplacement;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsGasMeterReplacement() {
        return isGasMeterReplacement;
    }

    public void setIsGasMeterReplacement(Boolean isGasMeterReplacement) {
        this.isGasMeterReplacement = isGasMeterReplacement;
    }

    @Override
    public String toString() {
        return "LastGasModel [id=" + id + ", date=" + date + ", gas=" + gas + ", isGasMeterReplacement="
                + isGasMeterReplacement + "]";
    }

    
}
