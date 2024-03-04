package com.example.GastroProject.repository;


import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);


}
