package streetalk.demo.v1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import streetalk.demo.v1.dto.sms.SmsResponseDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
public class CalcService {

    @Transactional
    public Integer getRandomNum(Integer integer) {
        Random random = new Random();
        int num = random.nextInt(integer);
        return num;
    }

    @Transactional
    public String getRandomName(){
        return "babo";
    }

    @Transactional
    public Long getDuration(LocalDateTime from, LocalDateTime to){
        return Duration.between(from,to).getSeconds();
    }
}
