package streetalk.demo.v1.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String fullName;
    @Column(name="bigLocation",nullable = false, length = 100)
    private String bigLocation;     //서울 특별시
    @Column(name="middleLocation",nullable = false, length = 100)
    private String middleLocation;  //마포구
    @Column(name="smallLocation",nullable = false, length = 100)
    private String smallLocation;   //

    public Location(String fullName, String bigLocation, String middleLocation, String smallLocation) {
        this.fullName = fullName;
        this.bigLocation = bigLocation;
        this.middleLocation = middleLocation;
        this.smallLocation = smallLocation;
    }
}
