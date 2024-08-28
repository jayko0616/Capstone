package com.example.test1.login.DTO;

import com.example.test1.login.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


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

    public UserDTO(UserEntity userEntity, List<Long> userLikedStores, List<Long> userCommentedStores) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.likedStores = userLikedStores;
        this.commentedStores = userCommentedStores;

    }
}

