package com.example.GastroProject.repository;


import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);


    User findByEmailAndRoles_Name(String email, String rolePatient);


    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllUsersWithRole(String roleName);



}
