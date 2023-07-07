package streetalk.demo.v1.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.domain.Board;
import streetalk.demo.v1.domain.BoardLike;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.dto.Post.BoardDto;
import streetalk.demo.v1.dto.Post.BoardListDto;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.repository.BoardLikeRepository;
import streetalk.demo.v1.repository.BoardRepository;
import streetalk.demo.v1.repository.PostRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final BoardLikeRepository boardLikeRepository;
//    @Transactional
    //특정 게시판의 글들 불러오기
//    public List<PostResponseDto> getBoardAllPost(HttpServletRequest req, Long id){
//        User user=userService.getCurrentUser(req);
//        Board board = boardRepository.findBoardById(id)
//                .orElseThrow(()-> new ArithmeticException(404,"can't find board"));
//        List<Post>posts= postRepository.findPostsByBoard(board);
//        List<PostResponseDto>postResponseDtos = new ArrayList<>();
//        for(Post post:posts){
//            PostResponseDto postResponseDto=PostResponseDto.builder()
//                    .name(user.getName())
//                    .location(user.getLocation().getSmallLocation())
//                    .industry(user.getIndustry().getName())
//                    .title(post.getTitle())
//                    .content(post.getContent())
//                    .likeCount(post.getLikeCount())
//                    .scrapCount(post.getScrapCount())
//                    .build();
//            postResponseDtos.add(postResponseDto);
//        }
//        try{
//            return postResponseDtos;
//        }catch(Error e){
//            throw new ArithmeticException(404,"The board doesn't have posts");
//        }
//    }

    @Transactional
    public void BoardLike(HttpServletRequest req, Long id){
        User user = userService.getCurrentUser(req);
        Board newboard = boardRepository.findBoardById(id)
                .orElseThrow(() -> new ArithmeticException(404,"can't match board"));
        try{
            BoardLike boardLike = boardLikeRepository.save(
                    BoardLike.builder()
                            .board(newboard)
                            .user(user)
                            .build()
            );
        }catch (Error e){
            throw new ArithmeticException(404, "Error for BoardLike");
        }
        return;
    }

    @Transactional
    public List<BoardDto> getBoardList(HttpServletRequest req){
        try{
            List<Board> boardList = boardRepository.findAll();
            List<BoardDto> boardDtoList = boardList.stream()
                    .map(b -> new BoardDto(b.getId(), b.getBoardName(),b.getCategory()))
                    .collect(Collectors.toList());

            return boardDtoList;
        }
        catch (Error e){
            throw new ArithmeticException(404,"Error for getBoardMainList or subboardList");
        }
    }


}
