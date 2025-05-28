package graduation.lunchvote.user.service;

import graduation.lunchvote.common.error.NotFoundException;
import graduation.lunchvote.user.model.User;
import graduation.lunchvote.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static graduation.lunchvote.app.config.SecurityConfig.PASSWORD_ENCODER;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }

    public User getExistedByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }
}
