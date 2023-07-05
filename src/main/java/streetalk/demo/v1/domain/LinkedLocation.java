package streetalk.demo.v1.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="mainLocation")
    private Location mainLocation;
    @ManyToOne
    @JoinColumn(name="subLocation")
    private Location subLocation;

    public LinkedLocation(Location mainLocation, Location subLocation) {
        this.mainLocation = mainLocation;
        this.subLocation = subLocation;
    }
}


