package com.example.test1.board.repository;

import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // select * from comment_table where board_id=? order by id desc;
    List<CommentEntity> findAllByStoreEntityOrderByIdDesc(StoreEntity storeEntity);

    List<CommentEntity> findByStoreEntityId(Long storeId);
}
