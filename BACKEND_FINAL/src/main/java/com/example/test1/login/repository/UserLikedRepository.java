package com.example.test1.login.repository;

import com.example.test1.login.entity.UserLikedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLikedRepository extends JpaRepository<UserLikedEntity, Long> {
    List<UserLikedEntity> findByUserId(int userId);

    UserLikedEntity findByUserIdAndStoreId(int userId, Long storeId);
    void deleteByStoreId(Long storeId);
    void deleteByUserId(int id);

    List<UserLikedEntity> findByStoreId(Long id);
}
