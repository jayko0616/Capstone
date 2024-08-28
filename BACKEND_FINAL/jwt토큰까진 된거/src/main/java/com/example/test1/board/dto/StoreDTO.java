package com.example.test1.board.dto;

import com.example.test1.board.entity.StoreEntity;
import lombok.*;

import java.time.LocalDateTime;

// DTO(Data Transfer Object), VO, Bean,         Entity
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
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    public StoreDTO(Long id, String storeCode, String storeName, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static StoreDTO toStoreDTO(StoreEntity storeEntity) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(storeEntity.getId());
        storeDTO.setStoreCode(storeEntity.getStoreCode());
        storeDTO.setStoreName(storeEntity.getBoardTitle());
        storeDTO.setStoreContent(storeEntity.getBoardContents());
        storeDTO.setBoardHits(storeEntity.getBoardHits());
        storeDTO.setBoardCreatedTime(storeEntity.getCreatedTime());
        storeDTO.setBoardUpdatedTime(storeEntity.getUpdatedTime());

        return storeDTO;
    }

}
