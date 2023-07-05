package streetalk.demo.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import streetalk.demo.v1.dto.sms.MessagesRequestDto;
import streetalk.demo.v1.dto.sms.SmsRequestDto;
import streetalk.demo.v1.dto.sms.SmsResponseDto;
import streetalk.demo.v1.exception.ArithmeticException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SmsService {

    @Value("${naverSNS.serviceId}")
    private String serviceId;
    @Value("${naverSNS.accessKey}")
    private String accessKey;
    @Value("${naverSNS.secretKey}")
    private String secretKey;


    @Transactional
    public void sendSms(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

        Long time = System.currentTimeMillis();
        List<MessagesRequestDto> messages = new ArrayList<>();
        messages.add(new MessagesRequestDto(recipientPhoneNumber, content));
        SmsRequestDto smsRequest = new SmsRequestDto("SMS", "COMM", "82", "01030323682", "MangoLtd", messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String sig = makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

        RestTemplate restTemplate = new RestTemplate();
        try{
            restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages"), body, SmsResponseDto.class);
        }catch (Error e){
            new ArithmeticException(404, "While Sending Message Error");
        }
    }

    public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";	// url (include query string)
        String timestamp = time.toString();			// current timestamp (epoch)
        String accessKey = this.accessKey;			// access key id (from portal or Sub Account)
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

}
