package streetalk.demo.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String location;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    @Column(nullable = false)
    private Long reportCount;
    @Column(nullable = false)
    private Boolean blocked;
    @Column
    @Builder.Default
    private Boolean isPrivate = false;
    public void reportCountUp(){
        this.reportCount++;
    }
}
