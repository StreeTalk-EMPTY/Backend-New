package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.BoardDto;
import streetalk.demo.v1.dto.Post.BoardListDto;
import streetalk.demo.v1.dto.Post.PostResponseDto;
import streetalk.demo.v1.service.BoardService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 좋아요 API
     * @param req
     * @param id
     * @return
     */
    @PostMapping("/boardLike/{boardId}")
    public ResponseEntity<MessageOnly> BoardLike(HttpServletRequest req, @PathVariable("boardId") Long id) {
        boardService.BoardLike(req, id);
        return new ResponseEntity<>(new MessageOnly(200, true, "Board Like Success"), HttpStatus.OK);
    }

    /**
     * 게시판 좋아요 취소 API
     * @param req
     * @param id
     * @return
     */
    @DeleteMapping("/boardLike/{boardId}")
    public ResponseEntity<MessageOnly> removeBoardLike(HttpServletRequest req, @PathVariable("boardId") Long id) {
        boardService.removeBoardLike(req, id);
        return new ResponseEntity<>(new MessageOnly(200, true, "Board Like Remove Success"), HttpStatus.OK);
    }

    /**
     * 게시판 리스트 전송 API
     * @param req
     * @return
     */
    @GetMapping("/board/list")
    public ResponseEntity<MessageWithData>getBoardList(HttpServletRequest req) {
        List<BoardDto> data = boardService.getBoardList(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "getBoardListSuccess", data), HttpStatus.OK);
    }

}
