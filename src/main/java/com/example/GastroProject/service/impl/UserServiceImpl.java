package com.example.GastroProject.service.impl;


import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public User save(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRoles(userDto.getRoles());
        user.getRoles().forEach(role -> {
            final Role roleByName = roleRepository.findByName(role.getName());
            role.setId(Objects.requireNonNullElseGet(roleByName, () -> roleRepository.save(role)).getId());
        });

        return userRepository.save(user);
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        user.setAuthorities(authorities);
        return user;
    }


    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new ArrayList<>(roles);
    }


}
