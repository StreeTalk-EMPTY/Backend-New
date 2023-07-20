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

    public void update(String content) {
        this.content = content;
    }
}
