package kr.ac.jejunu.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping()
    public void regist(@RequestBody User newUser) {
        userRepository.save(newUser);
    }

    @PostMapping("/login")
    public void login(@RequestBody User user) {
        if (userRepository.findByNameAndPassword(user.getName(), user.getPassword()) != null) {
            // TODO: 로그인 성공
        } else {
            // TODO: 로그인 실패
        }
    }

    @GetMapping("/logout")
    public void logout() {
        // TODO: 로그아웃
    }
}
