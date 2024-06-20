package com.ankush.cleancity.Authentication;

import com.ankush.cleancity.Users.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("java/api/auth")
@RestController
@Slf4j
public class AuthenicationController {
    @Autowired
    JdbcTemplate template;
    @Autowired
    UserDetailsManager users;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password) {

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body("LOGGED IN");
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthUser user) {
        UserDetails details = User.withUsername(user.getUsername()).password(encoder.encode(user.getPassword())).authorities("USER").build();
        if (users.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().body("USER ALREADY EXISTS");
        }
        users.createUser(details);
        user.insertDetails(template);
        return ResponseEntity.ok("CREATED USER");
    }


    /**
     * RESPONSE HANDLER FOR MethodArgumentNotValidException.class
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
