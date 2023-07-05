package streetalk.demo.v1.domain;


import lombok.*;
import streetalk.demo.v1.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="userName",nullable = false, length = 100)
    private String name;
    @Column(name="phoneNum",nullable = false, length = 100)
    private String phoneNum;
    @Column(name="password", length = 100)
    private String password;
    private LocalDateTime recentWriteTime = LocalDateTime.now().minusDays(1);


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name="locationId")
    private Location location;
    @ManyToOne
    @JoinColumn(name="industryId")
    private Industry industry;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> posts = new ArrayList<Post>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<PostLike> postLikes = new ArrayList<PostLike>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<BoardLike> boardLikes = new ArrayList<BoardLike>();


    public Integer countPost(){
        return this.posts.size();
    }
}
