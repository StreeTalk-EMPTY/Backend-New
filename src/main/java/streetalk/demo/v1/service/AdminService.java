package streetalk.demo.v1.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.domain.*;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AdminService {

    private final LocationRepository locationRepository;
    private final LinkedLocationRepository linkedLocationRepository;
    private final IndustryRepository industryRepository;
    private final BoardRepository boardRepository;
    private final NoticeRepository noticeRepository;
    private final S3Service s3Service;
    private final BannerRepository bannerRepository;


    @Transactional
    public void linkLocation(String first, String second){
        Location location1= locationRepository.findByFullName(first)
                .orElseThrow(() -> new ArithmeticException(404,"can't find location"));
        Location location2= locationRepository.findByFullName(second)
                .orElseThrow(() -> new ArithmeticException(404,"can't find location"));

        //중복 체크
        List<LinkedLocation> linkedLocationList = linkedLocationRepository.findAllByMainLocation(location1);
        for (LinkedLocation linkedLocation : linkedLocationList) {
            if(linkedLocation.getSubLocation().equals(location2)){
                throw new ArithmeticException(404, "already inserted");
            }

        }
        //data insert
        linkedLocationRepository.save(new LinkedLocation(location1, location2));
        linkedLocationRepository.save(new LinkedLocation(location2, location1));
    }


    @Transactional
    public void newIndustry(String industryName){
        Optional<Industry> industry = industryRepository.findByName(industryName);
        if(industry.isPresent())
            return;
        else{
            Industry newIndustry = Industry.builder()
                    .name(industryName)
                    .build();
            industryRepository.save(newIndustry);
        }
    }

    @Transactional
    public void newBoard(String boardName, String category){
        Optional<Board> board = boardRepository.findBoardByBoardName(boardName);
        if(board.isPresent())
            return;
        else{
            Board board1 = Board.builder()
                    .boardName(boardName)
                    .category(category)
                    .build();
            boardRepository.save(board1);
        }
    }

    @Transactional
    public void createNotice(String title, String content){
        noticeRepository.save(Notice.builder()
                        .title(title)
                        .content(content)
                        .build());
    }

//    @Transactional
//    public void inputNoticeImg(List<MultipartFile> multipartFiles) throws IOException {
//        for (MultipartFile multipartFile : multipartFiles) {
//            String fileName = multipartFile.getOriginalFilename();
//            s3Service.upload("notice1", multipartFile);
//            bannerImgUrlRepository.save(Banner.builder()
//                            .fileName("notice1")
//                            .build());
//        }
//    }

}
