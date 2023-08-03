package streetalk.demo.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Policy;
import streetalk.demo.v1.dto.*;
import streetalk.demo.v1.dto.Post.*;
import streetalk.demo.v1.dto.User.*;
import streetalk.demo.v1.repository.PolicyRepository;
import streetalk.demo.v1.service.SmsService;
import streetalk.demo.v1.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final PolicyRepository policyRepository;

    /**
     * 유저 정보를 전송하는 API
     * @param req
     * @return
     */
    @GetMapping
    public ResponseEntity<MessageWithData> getUser(HttpServletRequest req) {
        UserResponseDto data = userService.getUser(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get User info success!", data), HttpStatus.OK);
    }

    /**
     * 이용약관, 처리방침 전송 / 현재 하나의 이용약관, 처리방침만 있다고 가정
     * @return 개인정보 처리방침, 이용약관
     * */
    @GetMapping("/policy")
    public ResponseEntity<MessageWithData> getPolicy() {
        Optional<Policy> policy = policyRepository.findById(1L);
        return new ResponseEntity<>(new MessageWithData(200, true, "get Policy success", policy), HttpStatus.OK);
    }

    /**
     * 유저 정보를 수정하는 API
     * @param req
     * @param putUserRequestDto
     */
    @PutMapping
    public ResponseEntity<MessageOnly> updateUser(HttpServletRequest req, @RequestBody PutUserRequestDto putUserRequestDto){
        userService.updateUser(req, putUserRequestDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "update User Success"), HttpStatus.OK);
    }

    /**
     * 유저를 탈퇴 처리하는 API
     * @param req
     */
    @DeleteMapping
    public ResponseEntity<MessageOnly> deleteUser(HttpServletRequest req) {
        userService.deleteUser(req);
        return new ResponseEntity<>(new MessageOnly(200, true, "delete User Success"), HttpStatus.OK);
    }

    /**
     * 회원가입 API
     * @param values
     */
    @PostMapping("/auth")
    public ResponseEntity<MessageWithData> auth(@RequestBody HashMap<String, Object> values) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        String phoneNum = values.get("phoneNum").toString();
        AuthResponseDto data = userService.doAuth(phoneNum);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    /**
     * 로그인 API
     * @param loginRequestDto
     */
    @PostMapping("/login")
    public ResponseEntity<MessageWithData> login(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto);
        LoginResponseDto data = userService.login(loginRequestDto);
        return new ResponseEntity<>(new MessageWithData(200, true, "auth Success", data), HttpStatus.OK);
    }

    /**
     * 위치정보를 받아 유저 정보를 가공해서 전송해주는 API
     * 프로필 변경 눌렀을 때 위도, 경도 받아서 유저 정보와 새로운 위치 정보를 함께 return
     * @param req
     * @param profileRequestDto
     */
    @PostMapping("/profile")
    public ResponseEntity<MessageWithData> getUserProfile(HttpServletRequest req, @RequestBody ProfileRequestDto profileRequestDto) {

        ProfileResponseDto data = userService.getUserProfile(req, profileRequestDto);

        return new ResponseEntity<>(new MessageWithData(200, true, "get userProfile success", data), HttpStatus.OK);
    }


    /**
     * 본인이 작성한 글 전송 API
     * @param req
     * @return
     */
    @GetMapping("/post")
    public ResponseEntity<MessageWithData> getMyPostList(HttpServletRequest req) {
        List<PostListDto> data = userService.getMyPostList(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get My Post List Success", data), HttpStatus.OK);
    }

    /**
     * 본인이 좋아요 누른 글 전송 API
     * @param req
     */
    @GetMapping("/postLikes")
    public ResponseEntity<MessageWithData> getUserPostLike(HttpServletRequest req){
        List<PostListDto> data=userService.getUserPostLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserPostLike success!", data), HttpStatus.OK);
    }

    /**
     * 본인이 스크랩 누른 글 전송 API
     * @param req
     */
    @GetMapping("/postScraps")
    public ResponseEntity<MessageWithData> getUserScrapLike(HttpServletRequest req){
        List<PostListDto> data=userService.getUserScrapLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserScrapLike success!", data), HttpStatus.OK);
    }

    /**
     * 본인이 댓글 단 글 전송 API
     * @param req
     * @return
     */
    @GetMapping("/postMyReply")
    public ResponseEntity<MessageWithData> getPostListMyReply(HttpServletRequest req) {
        List<PostListDto> data = userService.getPostListMyReply(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "get Post List With My Reply success", data), HttpStatus.OK);
    }

    /**
     * 리프레쉬 토큰 재발급 API
     * @param req
     */
    @PutMapping("/refreshToken")
    public ResponseEntity<MessageWithData> refreshToken(HttpServletRequest req) {
        RefreshTokenDto data = userService.refreshToken(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "refreshToken success!", data), HttpStatus.OK);
    }

    /**
     * 공지 사항 반환하는 API
     * TODO UserController 에 두는게 맞는지 -> 변경 예정
     */
    @GetMapping("/notice")
    public ResponseEntity<MessageWithData> getNotice() {
        List<NoticeResponseDto> data = userService.getNoticeResponseDtos();
        return new ResponseEntity<>(new MessageWithData(200, true, "get Notice Success", data), HttpStatus.OK);
    }

}
