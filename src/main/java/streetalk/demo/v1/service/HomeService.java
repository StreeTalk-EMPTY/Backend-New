package streetalk.demo.v1.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.domain.*;
import streetalk.demo.v1.dto.Home.HomeDto;
import streetalk.demo.v1.dto.Home.HomePostListDto;
import streetalk.demo.v1.dto.Home.LikeBoard;
import streetalk.demo.v1.dto.Post.BannerResponseDto;
import streetalk.demo.v1.dto.Post.PostListDto;
import streetalk.demo.v1.repository.BannerRepository;
import streetalk.demo.v1.repository.NoticeRepository;
import streetalk.demo.v1.repository.PostRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class HomeService {
    private final UserService userService;
    private final S3Service s3Service;
    private final BannerRepository bannerRepository;
    private final NoticeRepository noticeRepository;
    private final PostRepository postRepository;
    private final int MAIN_POSTSIZE = 5;
    @Transactional
    public HomeDto getHome(HttpServletRequest req) {
        User user = userService.getCurrentUser(req);
        String notice = noticeRepository.findFirstByOrderByCreatedDateAsc().getTitle();
        PageRequest pageRequest = PageRequest.of(0, MAIN_POSTSIZE, Sort.by(Sort.Direction.DESC, "likeCount"));
        List<HomePostListDto> myLocalPosts = getLocalPosts(user, pageRequest);
        List<HomePostListDto> myIndustryPosts = getIndustryPosts(user, pageRequest);
        List<HomePostListDto> newPosts = getNewPosts(user, pageRequest);
        List<LikeBoard> likeBoardList = user.getBoardLikes().stream()
                .map(boardLike ->  new LikeBoard(boardLike.getBoard().getBoardName(), boardLike.getBoard().getId()) )
                .collect(Collectors.toList());
        List<Banner> bannerList = bannerRepository.findAll();
        bannerList.sort(Comparator.comparing(Banner::getId));
        List<BannerResponseDto> bannerResponseDtoList = new ArrayList<>();

        for (Banner banner : bannerList) {
            BannerResponseDto bannerResponseDto = BannerResponseDto
                    .builder()
                    .title(banner.getTitle())
                    .content(banner.getContent())
                    .contentId(banner.getContentId())
                    .isNotice(banner.isNotice())
                    .build();
            bannerResponseDtoList.add(bannerResponseDto);
        }

        return HomeDto.builder()
                .userName(user.getName())
                .location(user.getLocation().getSmallLocation())
                .industry(user.getIndustry().getName())
//                .mainNoticeImgUrl(getNoticeUrl())
                .bannerList(bannerResponseDtoList)
                .notice(notice)
                .myLocalPosts(myLocalPosts)
                .myIndustryPosts(myIndustryPosts)
                .newPosts(newPosts)
                .likeBoardList(likeBoardList)
                .build();
    }

//    @Transactional
//    public List<String> getNoticeUrl(){
//        return noticeImgUrlRepository.findAll().stream()
//                .map(url -> s3Service.getPreSignedDownloadUrl(url.getFileName()))
//                .collect(Collectors.toList());
//    }



    @Transactional
    public List<HomePostListDto> getLocalPosts(User user, Pageable pageable){
        List<Post> posts = postRepository.findTop5ByLocationAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(user.getLocation(), user, LocalDateTime.now().minusDays(7), pageable);
//        List<Post> posts = postRepository.findTop5ByIsDeletedIsFalseAndCreatedDateAfterAndLocationOrderByLikeCountDesc(LocalDateTime.now().minusDays(7), location);
//                .stream()
//                .sorted(Comparator.comparing(Post::getReplyCount).reversed())
//                .sorted(Comparator.comparing(Post::getLikeCount).reversed())
//                .limit(5)
//                .collect(Collectors.toList());
        return posts.stream()
                .map(post -> new HomePostListDto(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<HomePostListDto> getIndustryPosts(User user, Pageable pageable){
        List<Post> posts = postRepository.findTop5ByIndustryAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(user.getIndustry(), user, LocalDateTime.now().minusDays(7), pageable);
//        List<Post> posts = postRepository.findTop5ByIsDeletedFalseAndCreatedDateAfterAndIndustryOrderByLikeCountDesc(LocalDateTime.now().minusDays(7), industry);
//                .stream()
//                .sorted(Comparator.comparing(Post::getReplyCount).reversed())
//                .sorted(Comparator.comparing(Post::getLikeCount).reversed())
//                .limit(5)
//                .collect(Collectors.toList());
        return posts.stream()
                .map(post -> new HomePostListDto(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<HomePostListDto> getNewPosts(User user, Pageable pageable){
        List<Post> posts = postRepository
                .findTop5ByIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(user, LocalDateTime.now().minusDays(7), pageable);
        return posts.stream()
                .map(post -> new HomePostListDto(post))
                .collect(Collectors.toList());
    }


}
