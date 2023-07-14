package streetalk.demo.v1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.domain.*;
import streetalk.demo.v1.dto.Post.LockReplyDto;
import streetalk.demo.v1.dto.Post.ReplyDto;
import streetalk.demo.v1.dto.Post.ReplyNameDto;
import streetalk.demo.v1.dto.Post.ReplyResponseDto;
import streetalk.demo.v1.enums.Role;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final String PREFIX = "거리지기 ";
    private final ReplyRepository replyRepository;
    private final ReplySaveRepository replySaveRepository;
    private final LockReplyRepository lockReplyRepository;
//    private final ReplySave replySave;
    //post 저장
    @Transactional
    public void saveReply(HttpServletRequest req, ReplyDto replyDto){
        User user = userService.getCurrentUser(req);
        Post post = postRepository.findById(replyDto.getPostId())
                .orElseThrow(() -> new ArithmeticException(404,"can't match post"));

        try{
            System.out.println("savereply 안의 try문 작동");
            Reply reply=replyRepository.save(
                    Reply.builder()
                            .content(replyDto.getContent())
                            // TODO 이렇게 처리하면 안되지않나
                            .writer("글쓴이")
                            .location(user.getLocation().getFullName())
                            .post(post)
                            .user(user)
                            .reportCount(1L)
                            .blocked(false)
                            .build()
                );
            if(!post.getUser().equals(user)){
                Long userReplyId = replyNumbering(user, post);
                String writer = userService.getWriterByCheckName(replyDto.getCheckName(), user, userReplyId.toString());
                reply.setWriter(writer);
            }
            post.replyCountUp();
        }catch (Error e){
            throw new ArithmeticException(404, "Error for reply save");
        }
        return;
    }
    @Transactional
    public Long replyNumbering(User user,Post post){
         /*
         나중에 replySave가 많아지면 replySaveRepository에서 findAllBy 하는 task가 너무 클거 같아서
         한번 최소한의 findAllBy를 최소한으로 써봤어!
         로직은 너가 생각한거랑 같은데, 생각해보니 여기서 id만 주고 위의
         main함수에서 익명 여부를 결정하는게 좋을거 같아서 이렇게 구현해봤어!
         */
        List<ReplySave> replySaveList = replySaveRepository.findAllByPost(post);
        Optional<ReplySave> existReplySave = replySaveList.stream()
                .filter(replySave -> replySave.getUser().equals(user))
                .findFirst();
        Long userReplyId = replySaveList.stream().count() + 1;
        if(existReplySave.isPresent()) {
            userReplyId = existReplySave.get().getUserReplyId();
        }else{
            replySaveRepository.save(
                    ReplySave.builder()
                            .userReplyId(userReplyId)
                            .post(post)
                            .user(user)
                            .build());
        }
        return userReplyId;

    }
    @Transactional
    public void deleteReply(HttpServletRequest req, Long id) {
        User user = userService.getCurrentUser(req);
        Reply reply = replyRepository.findById(id)
                .orElseThrow(()->new ArithmeticException(404,"Doesn't Exist Reply"));
        //reply가 해당 유저의 것인지 확인
        if(reply.getUser().equals(user)){
            replyRepository.deleteById(id);
            Post post = postRepository.findById(reply.getPost().getId())
                    .orElseThrow(()->new ArithmeticException(404,"Doesn't Exist post"));
            post.replyCountDown();
        }else{
            throw new ArithmeticException(404, "해당 유저의 글이 아닙니다.");
        }
        return;
    }

    @Transactional
    public ReplyResponseDto getReplyToDto(Reply reply, User user){
        return ReplyResponseDto.builder()
                .replyId(reply.getId())
                .replyWriterName(reply.getWriter())
                .location(reply.getLocation())
                .content(reply.getContent())
                .replyWriterId(reply.getUser().getId())
                .lastTime(Duration.between(reply.getCreatedDate(), LocalDateTime.now()).getSeconds())
                .hasAuthority(hasAuthority(user, reply.getWriter()))
                .build();
    }

    public Boolean hasAuthority(User user, String name) {
        return user.getName().equals(name) || user.getRole() == Role.ADMIN;
    }

    @Transactional
    public Boolean lockReply(HttpServletRequest req, LockReplyDto lockReplyDto){
        User user=userService.getCurrentUser(req);
        Reply reply=replyRepository.findById(lockReplyDto.getReplyId())
                .orElseThrow(()->new ArithmeticException(404,"Doesn't Exist Reply"));
        Optional<LockReply> lockReply = lockReplyRepository.findByReplyAndUser(reply,user);
        if (lockReply.isPresent()){
            return false;
        }
        else{
            lockReplyRepository.save(
                    LockReply.builder()
                            .user(user)
                            .reply(reply)
                            .lockInfo(lockReplyDto.getLockInfo())
                            .build());
            reply.reportCountUp();
            if (reply.getReportCount()>=5){
                reply.setBlocked(Boolean.TRUE);
            }
            return true;
        }
    }
}