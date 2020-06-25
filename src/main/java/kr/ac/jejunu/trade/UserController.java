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
    public UserInfo login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        try {
            UserInfo loginUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).get();
            if (loginUser != null) {
                session.setAttribute("loginUserId", loginUser.getId());
                return loginUser;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/check")
    public void isLoggedIn(HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("loginUserId") == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
