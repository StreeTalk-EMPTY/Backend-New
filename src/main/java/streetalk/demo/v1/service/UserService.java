package streetalk.demo.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.domain.*;
import streetalk.demo.v1.dto.Post.*;
import streetalk.demo.v1.dto.User.*;
import streetalk.demo.v1.enums.Role;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.jwt.JwtTokenProvider;
import streetalk.demo.v1.jwt.UserDetailsImpl;
import streetalk.demo.v1.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final CalcService calcService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IndustryRepository industryRepository;
    private final LocationRepository locationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final LocationService locationService;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final NoticeRepository noticeRepository;
    private final PostRepository postRepository;

    @Transactional
    public AuthResponseDto doAuth(String phoneNum) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        // 1. check if phoneNum user exists -> if not { sign up }
        User user = userRepository.findByPhoneNum(phoneNum)
                .orElseGet(() -> signUp(phoneNum));

        // 2. get random num
        Integer num = calcService.getRandomNum(1000000);
        String randomNum = String.format("%06d", num);

        // 3. set password with random num
        user.setPassword(passwordEncoder.encode(randomNum));

        // 4. send SMS to user

        smsService.sendSms(phoneNum,setMessageContent(randomNum));

        // 5. response to client with randomNum
        return new AuthResponseDto(randomNum);
    }

    public String setMessageContent(String num) {
        return "[스트릿톡] 인증번호 [" + num + "]를 입력해주세요.";
    }

    @Transactional
    public User signUp(String phoneNum) {
        User user = User.builder()
                .name("거리지기")
                .phoneNum(phoneNum)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        user.setName("거리지기 " + user.getId());
        return user;
    }


    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByPhoneNum(loginRequestDto.getPhoneNum())
                .orElseThrow(() -> new ArithmeticException(404, "Please Authenticate First"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getPhoneNum(), loginRequestDto.getRandomNum()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = doGenerateToken(authentication);
        String userName = null;
        if (user.getName() == null) {
            userName = "거리지기 " + Long.valueOf(userRepository.findAll().size());
        } else
            userName = user.getName();
        Location currentLocation = locationService.getKoLocation(loginRequestDto.getLongitude(), loginRequestDto.getLatitude());

        LoginResponseDto loginResponseDto = new LoginResponseDto(
                token,
                userName,
                null,
                null,
                currentLocation.getFullName(),
                locationService.getNearCities(currentLocation)
        );

        if (user.getLocation() != null)
            loginResponseDto.setLocation(Optional.ofNullable(user.getLocation().getSmallLocation()));
        if (user.getIndustry() != null)
            loginResponseDto.setIndustry(Optional.ofNullable(user.getIndustry().getName()));
        return loginResponseDto;
    }


    @Transactional
    public String doGenerateToken(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(principal);
    }


    @Transactional
    public RefreshTokenDto refreshToken(HttpServletRequest req) {
        if (jwtTokenProvider.validateToken(jwtTokenProvider.resolveToken(req))) {
            User user = getCurrentUser(req);
            return new RefreshTokenDto(jwtTokenProvider.refreshToken(user));
        } else {
            throw new ArithmeticException(404, "token expired!!");
        }
    }

    @Transactional
    public User getCurrentUser(HttpServletRequest req) {
        return userRepository.findByPhoneNum(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(req)))
                .orElseThrow(() -> {
                    throw new RuntimeException("can't find who am i");
                });
    }

    @Transactional
    public void updateUser(HttpServletRequest req, PutUserRequestDto putUserRequestDto) {
        User user = getCurrentUser(req);
        Industry industry = industryRepository.findByName(putUserRequestDto.getIndustry())
                .orElseThrow(() -> new ArithmeticException(404, "can't find matching industry"));
        Location location = locationRepository.findByFullName(putUserRequestDto.getLocation())
                .orElseThrow(() -> new ArithmeticException(404, "can't find matching location"));
        user.setName(putUserRequestDto.getName());
        user.setIndustry(industry);
        user.setLocation(location);
        userRepository.save(user);
        return;
    }

    @Transactional
    public List<PostLikeResponseDto> getUserPostLike(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<PostLike> postLikeList = postLikeRepository.findByUser(user);
        List<PostLikeResponseDto> postlikeResponseDtos = new ArrayList<>();
        for (PostLike postLike : postLikeList) {
            PostLikeResponseDto postLikeResponseDto = PostLikeResponseDto.builder()
                    .name(user.getName())
                    .location(user.getLocation())
                    .title(postLike.getPost().getTitle())
                    .content(postLike.getPost().getContent())
                    .likecount(postLike.getPost().getLikeCount())
                    .scrapcount(postLike.getPost().getScrapCount())
                    .build();
            postlikeResponseDtos.add(postLikeResponseDto);
        }
        try {
            return postlikeResponseDtos;
        } catch (Error e) {
            throw new ArithmeticException(404, "Error for return post");
        }
    }

    @Transactional
    public List<ScrapLikeResponseDto> getUserScrapLike(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<PostScarp> postScarpList = postScrapRepository.findByUser(user);
        List<ScrapLikeResponseDto> scraplikeResponseDtos = new ArrayList<>();
        for (PostScarp postScarp : postScarpList) {
            ScrapLikeResponseDto scrapLikeResponseDto = ScrapLikeResponseDto.builder()
                    .name(user.getName())
                    .location(user.getLocation())
                    .title(postScarp.getPost().getTitle())
                    .content(postScarp.getPost().getContent())
                    .likecount(postScarp.getPost().getLikeCount())
                    .scrapcount(postScarp.getPost().getScrapCount())
                    .build();
            scraplikeResponseDtos.add(scrapLikeResponseDto);
        }
        try {
            return scraplikeResponseDtos;
        } catch (Error e) {
            throw new ArithmeticException(404, "Error for return post");
        }
    }

    @Transactional
    public String getWriterByCheckName(Boolean checkName, User user, String id) {
        if (checkName)
            return "거리지기" + id;
        else
            return user.getName();
    }

    @Transactional
    public UserResponseDto getUser(HttpServletRequest req){
        User user = getCurrentUser(req);
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getLocation().getSmallLocation(),
                user.getIndustry().getName()
        );
    }

    public List<NoticeResponseDto> getNoticeResponseDtos() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();
        for (Notice notice : noticeList) {
            NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createDate(notice.getCreatedDate().toLocalDate())
                    .build();
            noticeResponseDtoList.add(noticeResponseDto);
        }
        return noticeResponseDtoList;
    }

    public List<PostListDto> getMyPostList(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<Post> myPostList = postRepository.findByUser(user);
        List<PostListDto> data = new ArrayList<>();

        for (Post post : myPostList) {
            PostListDto postListDto = PostListDto
                    .builder()
                    .postId(post.getId())
                    .writer(post.getWriter())
                    .writerId(user.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPrivate(post.getIsPrivate())
                    .hasAuthority(true)
                    .createTime(post.getCreatedDate().toLocalDate())
                    .build();
            data.add(postListDto);
        }
        return data;
    }
}
