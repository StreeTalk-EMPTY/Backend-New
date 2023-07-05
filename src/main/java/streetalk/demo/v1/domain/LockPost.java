package streetalk.demo.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;
    @Column(nullable = false)
    private String lockInfo;
}
