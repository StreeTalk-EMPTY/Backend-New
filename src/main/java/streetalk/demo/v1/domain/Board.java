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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="boardName",nullable = false, length = 100)
    private String boardName;
    @Column(name="category",nullable = false, length = 100)
    private String category;
    @JsonManagedReference
    @OneToMany(mappedBy = "board")
    private List<Post> posts = new ArrayList<Post>();
    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLike> boardLikes = new ArrayList<BoardLike>();
}
