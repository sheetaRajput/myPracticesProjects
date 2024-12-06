package com.studentmanagement.StudentManagementSystem.config;


import com.studentmanagement.StudentManagementSystem.entity.User;
import com.studentmanagement.StudentManagementSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepo.findByUsername(username);
      if (user!=null){
          return new CustomUserDetails(user);
      }

        throw new UsernameNotFoundException("User Not Found Available");
    }
}
