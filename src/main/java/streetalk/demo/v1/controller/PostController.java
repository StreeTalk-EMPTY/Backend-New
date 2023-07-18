package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.*;
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
    @PostMapping("/test/post")
    public String testPost(HttpServletRequest req, @ModelAttribute PostDto postDto) {
        postService.save(req, postDto);
        return postDto.toString();
    }
    @PostMapping("/post")
    public ResponseEntity<MessageOnly> save(HttpServletRequest req, @ModelAttribute PostDto postDto){
        System.out.println("start POST /post");
        System.out.println(postDto);
        postService.save(req, postDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "post Save Success"), HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<MessageWithData> getPostById(HttpServletRequest req, @PathVariable Long postId){
        System.out.println("GET /post/{postId}");
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

    @GetMapping(value={"/post/list/{boardId}/{postId}", "/post/list/{boardId}"})
    public ResponseEntity<MessageWithData> getPostList(@PathVariable Long boardId, @PathVariable(required = false) Long postId, HttpServletRequest req){
        List<PostListDto> data = postService.getPostListByPage(boardId, postId);
        User user = userService.getCurrentUser(req);
        for (PostListDto postListDto : data) {
            // Auth 확인 필요 -> writer 대신 writerId로 변경 필요
            postListDto.setHasAuthority(postService.hasAuthority(user, postListDto.getWriterId()));
        }
        return new ResponseEntity<>(new MessageWithData(200, true, "get postLists", data), HttpStatus.OK);
    }

    @PutMapping("/post")
    public ResponseEntity<MessageOnly> update(HttpServletRequest req, @ModelAttribute PostUpdateDto postUpdateDto) throws IOException {
        postService.update(req,postUpdateDto);
        return new ResponseEntity<>(new MessageOnly(200,true,"post Update Success"),HttpStatus.OK);
    }

    @PutMapping("/postLike/{postId}")
    public ResponseEntity<MessageWithData> postLike(HttpServletRequest req, @PathVariable("postId") Long id){
        Boolean data = postService.postLike(req, id);
        return new ResponseEntity<>(new MessageWithData(200,true,"post Like Success", data),HttpStatus.OK);
    }

    @PutMapping("/postScrap/{postId}")
    public ResponseEntity<MessageWithData> scrapLike(HttpServletRequest req, @PathVariable("postId") Long id){
        Boolean data = postService.postScrap(req, id);
        return new ResponseEntity<>(new MessageWithData(200,true,"scrap Like Success", data),HttpStatus.OK);
    }

    @GetMapping("/searchPost/{word}")
    public ResponseEntity<MessageWithData>postSearch(HttpServletRequest req, @PathVariable("word") String key){
        List<Post> postList = postService.searchPost(req,key);
        List<PostListDto> data = postService.toPostListDto(postList, userService.getCurrentUser(req));
        return new ResponseEntity<>(new MessageWithData(200, true, "search Post Success", data), HttpStatus.OK);
    }
    @PostMapping("/lockPost")
    public ResponseEntity<MessageWithData> lockReply(HttpServletRequest req, @RequestBody LockPostDto lockPostDto){
        Boolean data=postService.lockPost(req, lockPostDto);
        return new ResponseEntity<>(new MessageWithData(200,true,"lock post  Success", data),HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageOnly> deletePost(HttpServletRequest req, @PathVariable("postId") Long id) {
        postService.deletePost(req,id);
        return new ResponseEntity<>(new MessageOnly(200,true,"post delete Success"),HttpStatus.OK);
    }

}
