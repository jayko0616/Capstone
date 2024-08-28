package com.example.test1.login.entity;

import com.example.test1.board.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUserEntity {

    @EmbeddedId
    private CommentUserId id;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "item_id")
    private StoreEntity storeEntity;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "rating")
    private int grade;
    public CommentUserEntity(StoreEntity storeEntity, UserEntity userEntity, int grade) {
        this.id = new CommentUserId(storeEntity.getId(), userEntity.getId());
        this.storeEntity = storeEntity;
        this.userEntity = userEntity;
        this.grade = grade;
    }
}
