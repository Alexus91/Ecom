package com.EComP.backend.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EComP.backend.Entity.User;

@Repository
public interface  UserRepo extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);



    
}
