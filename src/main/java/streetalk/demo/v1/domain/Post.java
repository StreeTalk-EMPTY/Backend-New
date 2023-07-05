package streetalk.demo.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import streetalk.demo.v1.service.PostImageService;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likeCount;
    @Column(nullable = false)
    private Long scrapCount;
    @Column(nullable = false)
    private Long replyCount;
    private String writer;

    @Column(nullable = false)
    private Long reportCount;
    @Column(nullable = false)
    private Boolean blocked;
    public void reportCountUp(){
        this.reportCount++;
    }

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "locationId")
    private Location location;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "industryId")
    private Industry industry;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="boardId")
    private Board board;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="userId")
    private User user;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PostImages> images = new ArrayList<PostImages>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }

    public void postLikeUp(){
        this.likeCount++;
    }
    public void postLikeDown(){this.likeCount--;}
    public void postScrapUp(){
        this.scrapCount++;
    }
    public void postScrapDown(){
        this.scrapCount--;
    }
    public void replyCountUp(){
        this.replyCount++;
    }
    public void replyCountDown(){
        this.replyCount--;
    }
    public void scrapUpdate(Long num){
        this.scrapCount=num;
    }
}
