package streetalk.demo.v1.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.enums.Role;
import streetalk.demo.v1.exception.ArithmeticException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


    @Value("${jwt.accessSecret}")
    private String secretKey;

    private final UserDetailServiceImpl userDetailService;
    // 토큰 유효 기간
    public static final long JWT_TOKEN_VALIDITY = 15 * 24 * 60 * 60 * 1000L; //15일

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(UserDetailsImpl userDetails) {

        Map<String, Object> claims = new HashMap<>();

        Boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ADMIN));
        if (isAdmin) {
            claims.put("role","ADMIN");
        } else {
            claims.put("role","USER");
        }
        String userName = userDetails.getUsername();
        claims.put("name", userName);

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public String refreshToken(User user) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(user.getPhoneNum())
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // set Expire Time
                .signWith(SignatureAlgorithm.HS512, secretKey)// 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Request의 Header에서 token 값을 가져옴.
    public String resolveToken(HttpServletRequest request) {
        String token =  request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null  || token.length()==0)
            return null;
        else
            return token.substring(7);
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            throw new ArithmeticException(404,"token expired you fucking idiot");
        }
    }


}