package overheadcost.overheadcost.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lastElectricityRead")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LastElectricityRead {

    public LastElectricityRead(int t180, int t280, LocalDate date) {
        this.t180 = t180;
        this.t280 = t280;
        this.date = date;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int t180;
    private int t280;
    private LocalDate date;
}
