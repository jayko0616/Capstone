package com.example.test1.login.entity;

import com.example.test1.board.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    private String password;
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "liked_stores", // 조인 테이블의 이름
            joinColumns = @JoinColumn(name = "user_id"), // 현재 엔티티(UserEntity)를 참조하는 컬럼
            inverseJoinColumns = @JoinColumn(name = "store_id") // 반대쪽 엔티티(StoreEntity)를 참조하는 컬럼
    )
    private List<StoreEntity> likedStores;

    @ManyToMany
    @JoinTable(
            name = "commented_stores", // 다른 조인 테이블의 이름
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private List<StoreEntity> commentedStores;

    private String role;

    // Getter, Setter, toString 메서드 생략
}
