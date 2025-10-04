package com.bhaskar.jobposttestapplication.sign_up.repository_dao;

import com.bhaskar.jobposttestapplication.sign_up.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationDao extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByEmail(String email);
}
