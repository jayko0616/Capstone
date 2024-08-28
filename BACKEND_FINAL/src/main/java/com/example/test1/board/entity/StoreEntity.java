package com.example.test1.board.entity;

import com.example.test1.board.dto.StoreDTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

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

    @Column(length = 50, nullable = false) // 크기 20, not null
    private String storeCode;

    @Column
    private String storeName;

    @Column(length = 500)
    private String storeContents;

    @Column
    private int storeHits;

    @Column
    private Double avgRating;

    @Column
    private Integer capacity;

    @Column
    private String coordinateX;

    @Column
    private String coordinateY;

    @Column
    private boolean screen;

    @Column
    private boolean drink;

    @Column
    private boolean street;

    @Column
    private String imageUrl; // 이미지 URL 필드 추가

    public static StoreEntity toSaveEntity(StoreDTO storeDTO, String imageUrl) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreCode(storeDTO.getStoreCode());
        storeEntity.setStoreName(storeDTO.getStoreName());
        storeEntity.setStoreContents(storeDTO.getStoreContent());
        storeEntity.setStoreHits(0);
        storeEntity.setAvgRating(storeDTO.getRating());
        storeEntity.setCapacity(storeDTO.getCapacity());
        storeEntity.setCoordinateX(storeDTO.getCoordinateX());
        storeEntity.setCoordinateY(storeDTO.getCoordinateY());
        storeEntity.setScreen(storeDTO.isScreen());
        storeEntity.setDrink(storeDTO.isDrink());
        storeEntity.setStreet(storeDTO.isStreet());
        storeEntity.setImageUrl(imageUrl);
        return storeEntity; // 수정: 새로운 객체를 반환
    }


}











