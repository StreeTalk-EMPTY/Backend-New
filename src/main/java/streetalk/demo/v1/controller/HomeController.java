package streetalk.demo.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import streetalk.demo.v1.dto.Home.HomeDto;
import streetalk.demo.v1.dto.MessageOnly;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.Post.BoardListDto;
import streetalk.demo.v1.service.BoardService;
import streetalk.demo.v1.service.HomeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/home")
    public ResponseEntity<MessageWithData>getHome(HttpServletRequest req){
        HomeDto data = homeService.getHome(req);
        return new ResponseEntity<>(new MessageWithData(200, true, "Get home Success", data), HttpStatus.OK);
    }

}
