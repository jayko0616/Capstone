package com.example.test1.login.DTO;

import com.example.test1.board.entity.StoreEntity;
import com.example.test1.login.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String username;
    private String phoneNumber;
    private List<Long> likedStores;
    private List<Long> commentedStores;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.phoneNumber = userEntity.getPhoneNumber();
        // StoreEntity 리스트에서 ID만 추출하여 likedStores에 저장
        this.likedStores = userEntity.getLikedStores().stream()
                .map(StoreEntity::getId) // StoreEntity에서 ID만 추출
                .collect(Collectors.toList());
        // StoreEntity 리스트에서 ID만 추출하여 commentedStores에 저장
        this.commentedStores = userEntity.getCommentedStores().stream()
                .map(StoreEntity::getId) // StoreEntity에서 ID만 추출
                .collect(Collectors.toList());
    }
}

