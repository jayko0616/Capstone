package com.example.test1.board.dto;

import com.example.test1.board.entity.StoreEntity;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class StoreDTO {
    private Long id;
    private String storeCode;
    private String storeName;
    private String storeContent;
    private int storeHits;
    private Double rating; // 평점 필드 추가
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    private Integer capacity;
    private String coordinateX;
    private String coordinateY;
    private boolean screen;
    private boolean drink;
    private boolean street;
    private String imageUrl;

    public static StoreDTO toStoreDTO(StoreEntity storeEntity) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(storeEntity.getId());
        storeDTO.setStoreCode(storeEntity.getStoreCode());
        storeDTO.setStoreName(storeEntity.getStoreName());
        storeDTO.setStoreContent(storeEntity.getStoreContents());
        storeDTO.setStoreHits(storeEntity.getStoreHits());
        storeDTO.setRating(storeEntity.getAvgRating()); // StoreEntity의 평점을 DTO로 매핑
        storeDTO.setBoardCreatedTime(storeEntity.getCreatedTime());
        storeDTO.setBoardUpdatedTime(storeEntity.getUpdatedTime());
        storeDTO.setCapacity(storeEntity.getCapacity());
        storeDTO.setCoordinateX(storeEntity.getCoordinateX());
        storeDTO.setCoordinateY(storeEntity.getCoordinateY());
        storeDTO.setScreen(storeEntity.isScreen());
        storeDTO.setDrink(storeEntity.isDrink());
        storeDTO.setStreet(storeEntity.isStreet());
        storeDTO.setImageUrl(storeEntity.getImageUrl());
        return storeDTO;
    }


    // 필요에 따라 추가 생성자, 메서드, 로직 등을 추가할 수 있습니다.
}
