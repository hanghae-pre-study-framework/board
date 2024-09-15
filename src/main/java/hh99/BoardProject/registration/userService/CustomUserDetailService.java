package hh99.BoardProject.registration.userService;

import hh99.BoardProject.registration.entity.UserInfo;
import hh99.BoardProject.registration.repository.UserInfoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    public CustomUserDetailService(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(userInfo.getUserName())
                .password(userInfo.getPassword())  // 이때 비밀번호는 암호화된 값이어야 함
                .roles("USER")  // 권한 설정
                .build();
    }
}
