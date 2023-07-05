package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.User.LoginRequestDto;
import streetalk.demo.v1.jwt.JwtTokenProvider;
import streetalk.demo.v1.service.AdminService;
import streetalk.demo.v1.service.S3Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final S3Service s3Service;


    @PostMapping("/admin/linkLocation")
    public ResponseEntity<MessageOnly> linkLocation(@RequestBody HashMap<String, Object> values) {
        String location1 = values.get("location1").toString();
        String location2 = values.get("location2").toString();
        adminService.linkLocation(location1, location2);
        return new ResponseEntity<>(new MessageOnly(200, true, "auth Success"), HttpStatus.OK);
    }

    @PostMapping("/admin/industry")
    public ResponseEntity<MessageOnly> inputIndustry(@RequestBody HashMap<String, Object> values) {
        String industryName = values.get("industry").toString();
        adminService.newIndustry(industryName);
        return new ResponseEntity<>(new MessageOnly(200, true, "create industry Success"), HttpStatus.OK);
    }

    @PostMapping("/admin/board")
    public ResponseEntity<MessageOnly> inputBoard(@RequestBody HashMap<String,Object> values){
        String boardName=values.get("board").toString();
        String category=values.get("category").toString();
        adminService.newBoard(boardName,category);
        return new ResponseEntity<>(new MessageOnly(200,true,"create board Success"), HttpStatus.OK);
    }

    @PostMapping("/admin/notice")
    public ResponseEntity<MessageOnly> inputNotice(@RequestBody HashMap<String,Object> values){
        String noticeTitle=values.get("title").toString();
        String noticeContent=values.get("content").toString();
        adminService.createNotice(noticeTitle,noticeContent);
        return new ResponseEntity<>(new MessageOnly(200,true,"create notice Success"), HttpStatus.OK);
    }

    @PostMapping("/admin/noticeImg")
    public ResponseEntity<MessageOnly> inputNoticeImg(@RequestParam("inputFiles") List<MultipartFile> multipartFile) throws IOException {
        adminService.inputNoticeImg(multipartFile);
        return new ResponseEntity<>(new MessageOnly(200,true,"create notice Success"), HttpStatus.OK);
    }

    @GetMapping("/test/test")
    public Authentication test(@RequestBody LoginRequestDto loginRequestDto){
        System.out.println("done");
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getPhoneNum(), loginRequestDto.getRandomNum()));
    }

    @GetMapping("/test/test2")
    public Authentication test2(@RequestBody LoginRequestDto loginRequestDto){
        System.out.println("done");
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getPhoneNum(), loginRequestDto.getRandomNum()));
    }
}
//1. 게시판 좋아요 어디서 함?
//2. 좋아요와 스크랩의 차이? 같은 기능인데 단지 유저를 위함?


