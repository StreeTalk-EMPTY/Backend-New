package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.LockReplyDto;
import streetalk.demo.v1.dto.Post.ReplyDto;
import streetalk.demo.v1.dto.Post.ReplyRequestDto;
import streetalk.demo.v1.service.ReplyService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<MessageOnly> save(HttpServletRequest req, @RequestBody ReplyDto replyDto){
        replyService.saveReply(req, replyDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "Reply Save Success"), HttpStatus.OK);
    }

    @PutMapping("/reply")
    public ResponseEntity<MessageOnly> update(HttpServletRequest req, @RequestBody ReplyRequestDto replyDto) {
        replyService.updateReply(req, replyDto);
        return new ResponseEntity<>(new MessageOnly(200, true, "Reply Update Success"), HttpStatus.OK);
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<MessageOnly> delete(HttpServletRequest req, @PathVariable("replyId") Long id) {
        replyService.deleteReply(req,id);
        return new ResponseEntity<>(new MessageOnly(200,true,"reply delete success"),HttpStatus.OK);
    }
    @PostMapping("/lockReply")
    public ResponseEntity<MessageWithData> lockReply(HttpServletRequest req, @RequestBody LockReplyDto lockReplyDto){
        Boolean data=replyService.lockReply(req, lockReplyDto);
        return new ResponseEntity<>(new MessageWithData(200,true,"lock reply  Success", data),HttpStatus.OK);
    }
}
