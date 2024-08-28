package com.example.test1.board.repository;

import com.example.test1.board.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Modifying
    @Query(value = "update StoreEntity b set b.storeHits=b.storeHits+1 where b.id=:id")
    void increaseHits(@Param("id") Long id);

    @Modifying
    @Query(value = "update StoreEntity b set b.storeHits=b.storeHits-1 where b.id=:id")
    void decreaseHits(@Param("id") Long id);

    Optional<StoreEntity> findByStoreName(String storeName);
    Optional<StoreEntity> findByStoreCode(String storeCode);
}














