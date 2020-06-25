package kr.ac.jejunu.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public void login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        User loginUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            session.setAttribute("loginUserId", loginUser.getId());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
