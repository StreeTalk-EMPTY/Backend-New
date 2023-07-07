package streetalk.demo.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.domain.Policy;
import streetalk.demo.v1.dto.*;
import streetalk.demo.v1.dto.Post.PostLikeResponseDto;
import streetalk.demo.v1.dto.Post.ScrapLikeResponseDto;
import streetalk.demo.v1.dto.User.*;
import streetalk.demo.v1.repository.PolicyRepository;
import streetalk.demo.v1.service.LocationService;
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
        List<PostLikeResponseDto> data=userService.getUserPostLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserPostLike success!", data), HttpStatus.OK);
    }

    @GetMapping("/user/postScraps")
    public ResponseEntity<MessageWithData> getUserScrapLike(HttpServletRequest req){
        List<ScrapLikeResponseDto> data=userService.getUserScrapLike(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getUserScrapLike success!", data), HttpStatus.OK);
    }

    @PostMapping("/test")
    public void test(@RequestBody HashMap<String, Object> values) {
        Location data = locationService.getKoLocation(Double.parseDouble(values.get("x").toString()), Double.parseDouble(values.get("y").toString()));
    }
}
