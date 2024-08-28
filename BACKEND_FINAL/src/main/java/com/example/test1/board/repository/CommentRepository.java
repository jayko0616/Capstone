package com.example.test1.board.repository;

import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByStoreEntityOrderByIdDesc(StoreEntity storeEntity);

    List<CommentEntity> findByStoreEntityId(Long storeId);
    
    List<CommentEntity> findByCommentWriterId(Integer commentWriterId);

    void deleteByCommentWriterId(int id);

}
