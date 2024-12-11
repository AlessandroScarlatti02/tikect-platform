package it.milestone.exam.tikect_platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.milestone.exam.tikect_platform.model.User;
import it.milestone.exam.tikect_platform.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            DatabaseUserDetails userDetails = new DatabaseUserDetails(userOptional.get());
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
