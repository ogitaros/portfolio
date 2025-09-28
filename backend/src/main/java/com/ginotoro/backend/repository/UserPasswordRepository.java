package com.ginotoro.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ginotoro.backend.entity.UserPassword;

@Repository
public interface UserPasswordRepository extends CrudRepository<UserPassword, Integer> {

    Optional<UserPassword> findFirstByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Integer userId);

}
