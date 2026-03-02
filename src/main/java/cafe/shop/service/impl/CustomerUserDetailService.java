package cafe.shop.service.impl;

import cafe.shop.model.AuthUser;
import cafe.shop.model.entities.User;
import cafe.shop.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    UserRepository userRepository;

    CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Cannot found user"));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        AuthUser authUser = new AuthUser(username, user.getPassword(), authorities);
        authUser.setId(user.getId().toString());
        authUser.setDisplayName(user.getFirstName());
        return authUser;
    }
}
