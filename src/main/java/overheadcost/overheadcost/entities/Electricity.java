package overheadcost.overheadcost.entities;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "electricity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Electricity {
    public Electricity(int t180, int t280, int solar, int difference, LocalDate date) {
        this.t180 = t180;
        this.t280 = t280;
        this.solar = solar;
        this.difference = difference;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int t180;
    private int t280;
    private int solar;
    private int difference;
    private LocalDate date;
}
