    package com.example.expense_tracker.service;

    import com.example.expense_tracker.entity.User;
    import com.example.expense_tracker.repository.UserRepository;
    import com.example.expense_tracker.security.JwtUtil;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    public class AuthService {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JwtUtil jwtUtil;

        public void register(User user) {
            userRepository.save(user);
        }

        public String login(User user) {
            userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!passwordEncoder.matches(user.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            return jwtUtil.generateToken(user.getEmail());
        }
    }
