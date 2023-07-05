package streetalk.demo.v1.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNum(phoneNum)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다." + phoneNum));
        return UserDetailsImpl.builder()
                .user(user)
                .build();
    }
}