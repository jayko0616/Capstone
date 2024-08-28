package com.example.test1.board.repository;

import com.example.test1.board.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    // update board_table set board_hits=board_hits+1 where id=?
    @Modifying
    @Query(value = "update StoreEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void increaseHits(@Param("id") Long id);

    @Modifying
    @Query(value = "update StoreEntity b set b.boardHits=b.boardHits-1 where b.id=:id")
    void decreaseHits(@Param("id") Long id);
}














