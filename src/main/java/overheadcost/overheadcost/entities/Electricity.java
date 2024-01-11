package overheadcost.overheadcost.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "electricity")
public class Electricity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int t180;
    private int t280;
    private int solar;
    private int difference;
    private LocalDate actualDate;

    public Electricity() {
    }

    public Electricity(int t180, int t280, int solar, int difference, LocalDate actualDate) {
        this.t180 = t180;
        this.t280 = t280;
        this.solar = solar;
        this.difference = difference;
        this.actualDate = actualDate;
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

    public int getSolar() {
        return solar;
    }

    public void setSolar(int solar) {
        this.solar = solar;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
    }

    @Override
    public String toString() {
        return "Electricity [t180=" + t180 + ", t280=" + t280 + ", solar=" + solar + ", difference=" + difference
                + ", actualDate=" + actualDate + ", id: " + id + "]";
    }

}
