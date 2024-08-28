package com.example.test1.board.dto;

import com.example.test1.board.entity.StoreEntity;
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
    private int boardHits;
    private Double rating; // 평점 필드 추가
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    public static StoreDTO toStoreDTO(StoreEntity storeEntity) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(storeEntity.getId());
        storeDTO.setStoreCode(storeEntity.getStoreCode());
        storeDTO.setStoreName(storeEntity.getBoardTitle());
        storeDTO.setStoreContent(storeEntity.getBoardContents());
        storeDTO.setBoardHits(storeEntity.getBoardHits());
        storeDTO.setRating(storeEntity.getAvgRating()); // StoreEntity의 평점을 DTO로 매핑
        storeDTO.setBoardCreatedTime(storeEntity.getCreatedTime());
        storeDTO.setBoardUpdatedTime(storeEntity.getUpdatedTime());

        return storeDTO;
    }

    public StoreDTO(Long id, String storeCode, String storeName, int boardHits, Double rating, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.boardHits = boardHits;
        this.rating = rating;
        this.boardCreatedTime = boardCreatedTime;
    }


    // 필요에 따라 추가 생성자, 메서드, 로직 등을 추가할 수 있습니다.
}
