package streetalk.demo.v1.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import streetalk.demo.v1.dto.MessageWithData;
import streetalk.demo.v1.dto.VersionDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/version")
public class VersionController {

    private final VersionDto versionDto = new VersionDto();

    /**
     * 업데이트 필수 버전, 최신 버전 전송 API
     */
    @GetMapping
    public ResponseEntity<MessageWithData> getVersion() {
        return new ResponseEntity<>(new MessageWithData(200, true,"version response success", versionDto), HttpStatus.OK);
    }


}
