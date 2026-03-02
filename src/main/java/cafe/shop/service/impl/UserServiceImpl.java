package cafe.shop.service.impl;

import cafe.shop.model.dto.RequestCreateAdminUser;
import cafe.shop.model.constant.UserRole;
import cafe.shop.model.dto.RequestCreateUser;
import cafe.shop.model.entities.User;
import cafe.shop.repository.UserRepository;
import cafe.shop.service.BaseService;
import cafe.shop.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createAdminUser(RequestCreateAdminUser requestCreateAdminUser) {
        log.info("Request admin user: {}", requestCreateAdminUser.toString());

        User adminUser = new User();
        adminUser.setUsername(requestCreateAdminUser.getUsername());
        adminUser.setPassword(passwordEncoder.encode(requestCreateAdminUser.getPassword()));
        adminUser.setRole(UserRole.ADMIN);

        userRepository.save(adminUser);

        log.info("Created successfully: {}", adminUser.getId());
    }

    @Override
    public User createUser(RequestCreateUser request) {
        log.info("Request admin user: {}", request.toString());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = userRepository.save(user);
        log.info("Created successfully: {}", user.getId());
        return user;
    }
}
