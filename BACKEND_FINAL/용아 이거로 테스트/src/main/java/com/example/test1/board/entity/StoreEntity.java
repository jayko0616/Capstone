package com.example.test1.board.entity;

import com.example.test1.board.dto.StoreDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "store_table")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class StoreEntity extends BaseEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20, not null
    private String storeCode;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private Double avgRating;


    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    public static StoreEntity toSaveEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreCode(storeDTO.getStoreCode());
        storeEntity.setBoardTitle(storeDTO.getStoreName());
        storeEntity.setBoardContents(storeDTO.getStoreContent());
        storeEntity.setBoardHits(0);
        return storeEntity;
    }

    public static StoreEntity toUpdateEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(storeDTO.getId());
        storeEntity.setStoreCode(storeDTO.getStoreCode());
        storeEntity.setBoardTitle(storeDTO.getStoreName());
        storeEntity.setBoardContents(storeDTO.getStoreContent());
        storeEntity.setBoardHits(storeDTO.getBoardHits());
        return storeEntity;
    }

}











