package com.ginotoro.backend.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ginotoro.backend.entity.UserM;

@Repository
public interface UserMRepository extends CrudRepository<UserM, Integer> {

    @Query("SELECT u FROM UserM u WHERE u.email = :email AND u.status = '1'")
    Optional<UserM> findActiveByEmail(String email);
}
