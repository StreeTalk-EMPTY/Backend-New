package streetalk.demo.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.domain.Notice;
import streetalk.demo.v1.domain.Policy;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.dto.*;
import streetalk.demo.v1.dto.Post.*;
import streetalk.demo.v1.dto.User.*;
import streetalk.demo.v1.repository.NoticeRepository;
import streetalk.demo.v1.repository.PolicyRepository;
import streetalk.demo.v1.service.LocationService;
import streetalk.demo.v1.service.SmsService;
import streetalk.demo.v1.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController()
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final LocationService locationService;
    private final PolicyRepository policyRepository;

    @GetMapping("/user")
    public ResponseEntity<MessageWithData> getUser(HttpServletRequest req) {
        UserResponseDto data = userService.getUser(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get User info success!", data), HttpStatus.OK);
    }

    /**
     * 현재 하나의 이용약관, 처리방침만 있다고 가정
     * @return 개인정보 처리방침, 이용약관
     * */
    @GetMapping("/user/policy")
    public ResponseEntity<MessageWithData> getPolicy() {
        Optional<Policy> policy = policyRepository.findById(1L);
        return new ResponseEntity<>(new MessageWithData(200, true, "get Policy success", policy), HttpStatus.OK);
    }

    @PostMapping("/user/auth")
    public ResponseEntity<MessageWithData> auth(@RequestBody HashMap<String, Object> values) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        String phoneNum = values.get("phoneNum").toString();
        AuthResponseDto data = userService.doAuth(phoneNum);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<MessageWithData> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto data = userService.login(loginRequestDto);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    // 프로필 변경 눌렀을 때 위도, 경도 받아서 유저 정보와 새로운 위치 정보를 함께 return
    @PostMapping("/user/profile")
    public ResponseEntity<MessageWithData> getUserProfile(HttpServletRequest req, @RequestBody ProfileRequestDto profileRequestDto) {

        ProfileResponseDto data = userService.getUserProfile(req, profileRequestDto);

        return new ResponseEntity<>(new MessageWithData(200, true, "get userProfile success", data), HttpStatus.OK);
    }

    @PutMapping("/user/refreshToken")
    public ResponseEntity<MessageWithData> refreshToken(HttpServletRequest req) {
        RefreshTokenDto data = userService.refreshToken(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "refreshToken success!", data), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<MessageOnly> updateUser(HttpServletRequest req, @RequestBody PutUserRequestDto putUserRequestDto){
        userService.updateUser(req, putUserRequestDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "update User Success"), HttpStatus.OK);
    }

//    @GetMapping("/user/{id}/postlike")
//    public ResponseEntity<MessageWithData> getUserPostLike(HttpServletRequest req){
//        List<PostLikeResponseDto>data=userService.getUserPostLike(req);
//        return new ResponseEntity<>(new MessageWithData(200,true,"postupdate",data),HttpStatus.OK);
//    }

    @GetMapping("/user/postLikes")
    public ResponseEntity<MessageWithData> getUserPostLike(HttpServletRequest req){
        List<PostListDto> data=userService.getUserPostLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserPostLike success!", data), HttpStatus.OK);
    }

    @GetMapping("/user/postScraps")
    public ResponseEntity<MessageWithData> getUserScrapLike(HttpServletRequest req){
        List<PostListDto> data=userService.getUserScrapLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserScrapLike success!", data), HttpStatus.OK);
    }

    @GetMapping("/user/postMyReply")
    public ResponseEntity<MessageWithData> getPostListMyReply(HttpServletRequest req) {
        List<PostListDto> data = userService.getPostListMyReply(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get Post List With My Reply success", data), HttpStatus.OK);
    }

    @PostMapping("/test")
    public void test(@RequestBody HashMap<String, Object> values) {
        Location data = locationService.getKoLocation(Double.parseDouble(values.get("x").toString()), Double.parseDouble(values.get("y").toString()));
    }

    /**
     * 공지 사항 반환하는 API
     * TODO UserController 에 두는게 맞는지
     */
    @GetMapping("/user/notice")
    public ResponseEntity<MessageWithData> getNotice() {
        List<NoticeResponseDto> data = userService.getNoticeResponseDtos();
        return new ResponseEntity<>(new MessageWithData(200, true, "get Notice Success", data), HttpStatus.OK);
    }


    @GetMapping("/authtest")
    public void at(Authentication auth) {
        System.out.println(auth.getName());
    }
    @GetMapping("/user/post")
    public ResponseEntity<MessageWithData> getMyPostList(HttpServletRequest req) {
        List<PostListDto> data = userService.getMyPostList(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get My Post List Success", data), HttpStatus.OK);
    }
}
