package streetalk.demo.v1.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(unique = true)
//    private String fileName;

    @Column
    private String title;

    @Column
    private String content;

    // 1 := Notice, 0 := Post
    @Column
    private boolean isNotice;

    // 연결된 공지(게시글)의 id
    @Column
    private Long contentId;

}
