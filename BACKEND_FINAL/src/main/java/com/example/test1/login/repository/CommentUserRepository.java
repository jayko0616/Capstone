package com.example.test1.login.repository;


import com.example.test1.login.entity.CommentUserEntity;
import com.example.test1.login.entity.CommentUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentUserRepository extends JpaRepository<CommentUserEntity, CommentUserId> {

    CommentUserEntity findByIdStoreIdAndIdUserId(Long storeId, int userId);

    List<CommentUserEntity> findByStoreEntityId(Long storeId);

    void deleteByUserEntityId(int id);
}