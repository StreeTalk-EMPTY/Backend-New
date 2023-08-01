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
import streetalk.demo.v1.dto.NearCity;
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
import java.util.*;

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
    private final BoardLikeRepository boardLikeRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public AuthResponseDto doAuth(String phoneNum) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        // 1. check if phoneNum user exists -> if not { sign up }

        // TODO
        //  회원가입 로직 변겅 필요
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
                .name("new user")
                .phoneNum(phoneNum)
                .role(Role.USER)
                .build();
        userRepository.save(user);
//        user.setName("거리지기 " + user.getId());
        return user;
    }


    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
        User user = userRepository.findByPhoneNum(loginRequestDto.getPhoneNum())
                .orElseThrow(() -> new ArithmeticException(404, "Please Authenticate First"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getPhoneNum(), loginRequestDto.getRandomNum()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = doGenerateToken(authentication);
        String userName = user.getName();
//        String userName = null;
//        if (user.getName() == null) {
//            userName = "거리지기 " + Long.valueOf(userRepository.findAll().size());
//            userName = "new user";
//        } else
//            userName = user.getName();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(loginRequestDto.getLatitude());
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
    public List<PostListDto> getUserPostLike(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<PostLike> postLikeList = postLikeRepository.findByUser(user);
        List<PostListDto> data = new ArrayList<>();
//        for (PostLike postLike : postLikeList) {
        for (int i=postLikeList.size()-1; i>=0; i--) {
            PostLike postLike = postLikeList.get(i);
            Post post = postLike.getPost();

            if (post.getIsDeleted()) {
                continue;
            }

            // TODO
            //  따로 서비스로 뺄 것
            Optional<PostLike> isPostLike = postLikeRepository.findByPostAndUser(post, user);
            Optional<PostScrap> isPostScarp = postScrapRepository.findByPostAndUser(post, user);
            boolean like = false;
            boolean scrap = false;
            if(isPostLike.isPresent())
                like = true;
            if(isPostScarp.isPresent())
                scrap = true;
            PostListDto postListDto = PostListDto
                    .builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPrivate(post.getIsPrivate())
                    .postId(post.getId())
                    .writer(post.getUser().getName())
                    .createTime(post.getCreatedDate().toLocalDate())
                    .postScrap(scrap)
                    .postLike(like)
                    .scrapCount(post.getScrapCount())
                    .likeCount(post.getLikeCount())
                    .replyCount(post.getReplyCount())
                    .build();
            data.add(postListDto);
        }
        try {
            return data;
        } catch (Error e) {
            throw new ArithmeticException(404, "Error for return post");
        }
    }

    @Transactional
    public List<PostListDto> getUserScrapLike(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<PostScrap> postScrapList = postScrapRepository.findByUser(user);
//        for (PostScarp postScarp : postScarpList) {
        List<PostListDto> data = new ArrayList<>();
        for (int i = postScrapList.size()-1; i>=0; i--) {
            PostScrap postScrap = postScrapList.get(i);
            Post post = postScrap.getPost();
            if (post.getIsDeleted()) {
                continue;
            }
            // TODO
            //  따로 서비스로 뺄 것
            Optional<PostLike> isPostLike = postLikeRepository.findByPostAndUser(post, user);
            Optional<PostScrap> isPostScarp = postScrapRepository.findByPostAndUser(post, user);
            boolean like = false;
            boolean scrap = false;
            if(isPostLike.isPresent())
                like = true;
            if(isPostScarp.isPresent())
                scrap = true;
            PostListDto postListDto = PostListDto
                    .builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPrivate(post.getIsPrivate())
                    .postId(post.getId())
                    .writer(post.getUser().getName())
                    .createTime(post.getCreatedDate().toLocalDate())
                    .postScrap(scrap)
                    .postLike(like)
                    .scrapCount(post.getScrapCount())
                    .likeCount(post.getLikeCount())
                    .replyCount(post.getReplyCount())
                    .build();
            data.add(postListDto);
        }
        try {
            return data;
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
                    .id(notice.getId())
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

        for (int i=myPostList.size()-1; i>=0; i--) {
            Post post = myPostList.get(i);
            if (post.getIsDeleted()) {
                continue;
            }
            Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post, user);
            Optional<PostScrap> postScarp = postScrapRepository.findByPostAndUser(post, user);
            boolean like = false;
            boolean scrap = false;
            if(postLike.isPresent())
                like = true;
            if(postScarp.isPresent())
                scrap = true;
            PostListDto postListDto = PostListDto
                    .builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPrivate(post.getIsPrivate())
                    .postId(post.getId())
                    .writer(post.getUser().getName())
                    .createTime(post.getCreatedDate().toLocalDate())
                    .postScrap(scrap)
                    .postLike(like)
                    .scrapCount(post.getScrapCount())
                    .likeCount(post.getLikeCount())
                    .replyCount(post.getReplyCount())
                    .build();
            data.add(postListDto);
        }
        return data;
    }

    public boolean getIsBoardLike(User user, Long boardId) {
        Optional<BoardLike> boardLike = boardLikeRepository.findBoardLikeByUserAndBoardId(user, boardId);
        return boardLike.isPresent();
    }

    public List<PostListDto> getPostListMyReply(HttpServletRequest req) {
        User user = getCurrentUser(req);
        List<Reply> replyList = replyRepository.findRepliesByUserId(user.getId());
        List<PostListDto> data = new ArrayList<>();
        Set<Long> postIdSet = new HashSet<>();
        for (int i=replyList.size()-1; i>=0; i--) {
            Reply reply = replyList.get(i);
            Post post = reply.getPost();
            if (post.getIsDeleted()) {
                continue;
            }
            if (postIdSet.contains(post.getId())) {
                continue;
            }
            postIdSet.add(post.getId());
            Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post, user);
            Optional<PostScrap> postScarp = postScrapRepository.findByPostAndUser(post, user);
            boolean like = false;
            boolean scrap = false;
            if(postLike.isPresent())
                like = true;
            if(postScarp.isPresent())
                scrap = true;
            PostListDto postListDto = PostListDto
                    .builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPrivate(post.getIsPrivate())
                    .postId(post.getId())
                    .writer(post.getUser().getName())
                    .createTime(post.getCreatedDate().toLocalDate())
                    .postScrap(scrap)
                    .postLike(like)
                    .scrapCount(post.getScrapCount())
                    .likeCount(post.getLikeCount())
                    .replyCount(post.getReplyCount())
                    .build();
            data.add(postListDto);
        }
        return data;
    }

    public ProfileResponseDto getUserProfile(HttpServletRequest req, ProfileRequestDto profileRequestDto) {
        User user = getCurrentUser(req);
        Double latitude = profileRequestDto.getLatitude();
        Double longitude = profileRequestDto.getLongitude();
        Location location = locationService.getKoLocation(longitude, latitude);
        String currentCity = location.getFullName();
        List<NearCity> nearCities = locationService.getNearCities(location);
        ProfileResponseDto profileResponseDto = ProfileResponseDto
                .builder()
                .userName(user.getName())
                .industry(user.getIndustry().getName())
                .currentCity(currentCity)
                .nearCities(nearCities)
                .build();
        return profileResponseDto;
    }

    public void deleteUser(HttpServletRequest req) {
        User user = getCurrentUser(req);
        user.setPhoneNum(user.getPhoneNum()+"D");
        user.setName("탈퇴회원");
        userRepository.saveAndFlush(user);
    }

    public User findUserNameByUserId(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new ArithmeticException(404, "can't find user"));
    }
}
