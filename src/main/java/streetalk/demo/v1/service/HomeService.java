package streetalk.demo.v1.service;

import lombok.AllArgsConstructor;
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

    @Transactional
    public HomeDto getHome(HttpServletRequest req) {
        User user = userService.getCurrentUser(req);
        String notice = noticeRepository.findFirstByOrderByCreatedDateAsc().getTitle();
        List<HomePostListDto> myLocalPosts = getLocalPosts(user.getLocation());
        List<HomePostListDto> myIndustryPosts = getIndustryPosts(user.getIndustry());
        List<HomePostListDto> newPosts = getNewPosts();
        List<LikeBoard> likeBoardList = user.getBoardLikes().stream()
                .map(boardLike ->  new LikeBoard(boardLike.getBoard().getBoardName(), boardLike.getBoard().getId()) )
                .collect(Collectors.toList());
        List<Banner> bannerList = bannerRepository.findAll();
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
        bannerResponseDtoList.sort(Comparator.comparing(BannerResponseDto::getContentId).reversed());

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
    public List<HomePostListDto> getLocalPosts(Location location){
        List<Post> posts = postRepository.findTop5ByLocationAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(location, LocalDateTime.now().minusDays(7));
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
    public List<HomePostListDto> getIndustryPosts(Industry industry){
        List<Post> posts = postRepository.findTop5ByIndustryAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(industry, LocalDateTime.now().minusDays(7));
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
    public List<HomePostListDto> getNewPosts(){
        List<Post> posts = postRepository
                .findTop5ByIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(LocalDateTime.now().minusDays(7));
        return posts.stream()
                .map(post -> new HomePostListDto(post))
                .collect(Collectors.toList());
    }


}
