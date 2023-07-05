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
public class ReplySave extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userReplyId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
}
