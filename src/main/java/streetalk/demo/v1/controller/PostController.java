package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Board;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.dto.Home.HomeDto;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.*;
import streetalk.demo.v1.service.BoardService;
import streetalk.demo.v1.service.HomeService;
import streetalk.demo.v1.service.PostService;
import streetalk.demo.v1.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final BoardService boardService;

    private final HomeService homeService;

    /**
     * 홈 인기글 (지역/업종/실시간) 게시글 및 배너, 공지
     * 컨트롤러 분리 예정
     * @param req
     * @return
     */
    @GetMapping("/home")
    public ResponseEntity<MessageWithData>getHome(HttpServletRequest req){
        HomeDto data = homeService.getHome(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "Get home Success", data), HttpStatus.OK);
    }

    /**
     * 글 작성 API
     * @param req
     * @param postDto
     * @return
     */
    @PostMapping("/post")
    public ResponseEntity<MessageOnly> save(HttpServletRequest req, @ModelAttribute PostDto postDto){
        postService.save(req, postDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "post Save Success"), HttpStatus.OK);
    }

    /**
     * 게시글 삭제 API
     * @param req
     * @param id
     * @return
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageOnly> deletePost(HttpServletRequest req, @PathVariable("postId") Long id) {
        postService.deletePost(req,id);
        return new ResponseEntity<>(new MessageOnly(200,true,"post delete Success"),HttpStatus.OK);
    }

    /**
     * 글 세부 내용 전송 API
     * @param req
     * @param postId
     * @return
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<MessageWithData> getPostById(HttpServletRequest req, @PathVariable Long postId){
        PostResponseDto data = postService.findPostById(req,postId);

        User user = userService.getCurrentUser(req);
        data.setHasAuthority(postService.hasAuthority(user, data.getPostWriterId()));
        return new ResponseEntity<>(new MessageWithData(200, true, "nicejob", data), HttpStatus.OK);
    }

/*
paging의 정식 방법
 */
//    //http://localhost:8080/list?page=0&size=3
//    @GetMapping("/post/list")
//    public ResponseEntity<MessageWithData> list(Pageable pageable, Long boardId) {
//        List<PostListDto> data = postService.getPostListByPage(boardId, pageable);
//        return new ResponseEntity<>(new MessageWithData(200, true, "get postLists", data), HttpStatus.OK);
//    }

    /**
     * 특정 게시판의 글 중 postId 이하 특정 개수만큼 전송, postId 없는 경우 가장 최신글부터
     * @param boardId
     * @param postId
     * @param req
     * @return
     */
    @GetMapping(value={"/post/list/{boardId}/{postId}", "/post/list/{boardId}"})
    public ResponseEntity<MessageWithData> getPostList(@PathVariable Long boardId, @PathVariable(required = false) Long postId, HttpServletRequest req){
        List<PostListDto> postListDtoList = postService.getPostListByPage(boardId, postId, req);
        User user = userService.getCurrentUser(req);

        BoardResponseDto data = BoardResponseDto
                .builder()
                .isBoardLike(userService.getIsBoardLike(user, boardId))
                .postListDto(postListDtoList)
                .build();
        return new ResponseEntity<>(new MessageWithData(200, true, "get postLists", data), HttpStatus.OK);
    }

    /**
     * 글 수정 API
     * 이미지 관련 로직 수정 필요
     * @param req
     * @param postUpdateDto
     * @return
     */
    @PutMapping("/post")
    public ResponseEntity<MessageOnly> update(HttpServletRequest req, @RequestBody PostUpdateDto postUpdateDto) throws IOException {
        System.out.println(postUpdateDto);
        postService.update(req,postUpdateDto);
        return new ResponseEntity<>(new MessageOnly(200,true,"post Update Success"),HttpStatus.OK);
    }

    /**
     * 게시글 좋아요/취소 API
     * @param req
     * @param id
     * @return
     */
    @PutMapping("/postLike/{postId}")
    public ResponseEntity<MessageWithData> postLike(HttpServletRequest req, @PathVariable("postId") Long id){
        Boolean data = postService.postLike(req, id);
        return new ResponseEntity<>(new MessageWithData(200,true,"post Like Success", data),HttpStatus.OK);
    }

    /**
     * 게시글 스크랩/취소 API
     * @param req
     * @param id
     * @return
     */
    @PutMapping("/postScrap/{postId}")
    public ResponseEntity<MessageWithData> scrapLike(HttpServletRequest req, @PathVariable("postId") Long id){
        Boolean data = postService.postScrap(req, id);
        return new ResponseEntity<>(new MessageWithData(200,true,"scrap Like Success", data),HttpStatus.OK);
    }

    /**
     * 단어가 제목/내용에 포함된 게시글 검색 API
     * @param req
     * @param key
     * @return
     */
    @GetMapping("/searchPost/{word}")
    public ResponseEntity<MessageWithData> postSearch(HttpServletRequest req, @PathVariable("word") String key){
        List<Post> postList = postService.searchPost(req,key);
        List<PostListDto> data = postService.toPostListDto(postList, userService.getCurrentUser(req));
        return new ResponseEntity<>(new MessageWithData(200, true, "search Post Success", data), HttpStatus.OK);
    }

    /**
     * 게시글 차단 API
     * @param req
     * @param lockPostDto
     * @return
     */
    @PostMapping("/lockPost")
    public ResponseEntity<MessageWithData> lockReply(HttpServletRequest req, @RequestBody LockPostDto lockPostDto){
        Boolean data=postService.lockPost(req, lockPostDto);
        return new ResponseEntity<>(new MessageWithData(200,true,"lock post  Success", data),HttpStatus.OK);
    }

}
