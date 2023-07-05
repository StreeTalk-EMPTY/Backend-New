package streetalk.demo.v1.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostScarp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="postId")
    private Post post;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="userId")
    private User user;
}
