package overheadcost.overheadcost.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lastElectricityRead")
public class LastElectricityRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int t180;
    private int t280;
    private LocalDate date;

    public LastElectricityRead() {
    }

    public LastElectricityRead(int t180, int t280, LocalDate actualDate) {
        this.t180 = t180;
        this.t280 = t280;
        this.date = actualDate;
    }

    public int getT180() {
        return t180;
    }

    public void setT180(int t180) {
        this.t180 = t180;
    }

    public int getT280() {
        return t280;
    }

    public void setT280(int t280) {
        this.t280 = t280;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate actualDate) {
        this.date = actualDate;
    }

    @Override
    public String toString() {
        return "LastElectricity [t180=" + t180 + ", t280=" + t280 + ", actualDate=" + date + ", id: " + id + "]";
    }

}
