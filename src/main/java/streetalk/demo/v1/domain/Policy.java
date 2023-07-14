package streetalk.demo.v1.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    private Long id;
    @Column(length = 3000)
    private String termsOfUse;
    @Column(length = 3000)
    private String privatePolicy;
}
