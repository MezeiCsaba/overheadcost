package overheadcost.overheadcost.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gas")
public class GasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int consumption;

    public GasModel() {
    }

    public GasModel(LocalDate date, int gas) {
        this.date = date;
        this.consumption = gas;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int gas) {
        this.consumption = gas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Gas [id=" + id + ", date=" + date + ", gas=" + consumption + "]";
    }

}
