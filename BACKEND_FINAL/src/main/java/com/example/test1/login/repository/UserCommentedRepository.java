package com.example.test1.login.repository;

import com.example.test1.board.entity.CommentEntity;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.login.entity.UserCommentedEntity;
import com.example.test1.login.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCommentedRepository extends JpaRepository<UserCommentedEntity, Long> {

    List<UserCommentedEntity> findByUserId(int userId);
    List<UserCommentedEntity> findByStoreId(long storeId);
    UserCommentedEntity findByUserAndStoreAndComment(UserEntity userEntity, StoreEntity storeEntity, CommentEntity commentEntity);

    void deleteByCommentId(Long commentId);
    void deleteByUserId(int id);
    void deleteByStoreId(Long storeId);
}
