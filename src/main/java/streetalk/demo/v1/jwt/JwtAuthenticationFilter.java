package streetalk.demo.v1.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import streetalk.demo.v1.exception.ArithmeticException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {  // * 클래스로서 요청당 한번의 filter를 수행하도록 * //

    //Authorization값을 꺼내어 토큰을 검사하고 해당 유저가 실제 DB에 있는지 검사하는 등의 전반적인 인증처리
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException, ArithmeticException {
        // 헤더에서 JWT 받아옴
        String jwtToken = jwtTokenProvider.resolveToken(req);
        // 유효한 토큰인지 확인
        if (jwtToken != null) {
                if (jwtTokenProvider.validateToken(jwtToken)) {
                    // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                    // SecurityContext 에 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    System.out.println("else문 들어왔음");
                    throw new ArithmeticException(404,"token has expired");
                }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }

        filterChain.doFilter(req, res);
    }

}