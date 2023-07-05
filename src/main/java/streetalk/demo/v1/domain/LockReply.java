package streetalk.demo.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="replyId")
    private Reply reply;
    @Column(nullable = false)
    private String lockInfo;
}
