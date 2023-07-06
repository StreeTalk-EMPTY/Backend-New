package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.*;
import streetalk.demo.v1.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping("/test/post")
    public String testPost(HttpServletRequest req, @ModelAttribute PostDto postDto) {
        postService.save(req, postDto);
        return postDto.toString();
    }
    @PostMapping("/post")
    public ResponseEntity<MessageOnly> save(HttpServletRequest req, @ModelAttribute PostDto postDto){
        System.out.println("start POST /post");
        postService.save(req, postDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "post Save Success"), HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<MessageWithData> getPostById(HttpServletRequest req, @PathVariable Long postId){
        System.out.println("GET /post/{postId}");
        PostResponseDto data = postService.findPostById(req,postId);
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
    public ResponseEntity<MessageWithData> getPostList(@PathVariable Long boardId,@PathVariable(required = false) Long postId){
        List<PostListDto> data = postService.getPostListByPage(boardId, postId);
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
    public ResponseEntity<MessageWithData>postSearch(HttpServletRequest req,@PathVariable("word") String key){
        System.out.println("GET /searchPost/{word}");
        List<Post> data=postService.searchPost(req,key);
        return new ResponseEntity<>(new MessageWithData(200, true, "search Post Success", data), HttpStatus.OK);
    }
    @PostMapping("/lockPost")
    public ResponseEntity<MessageWithData> lockReply(HttpServletRequest req, @RequestBody LockPostDto lockPostDto){
        System.out.println("POST /lockPost");
        Boolean data=postService.lockPost(req, lockPostDto);
        return new ResponseEntity<>(new MessageWithData(200,true,"lock post  Success", data),HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageOnly> deletePost(HttpServletRequest req, @PathVariable("postId") Long id) {
        System.out.println("DELETE /post/{postId}");
        postService.deletePost(req,id);
        return new ResponseEntity<>(new MessageOnly(200,true,"post delete Success"),HttpStatus.OK);
    }

}