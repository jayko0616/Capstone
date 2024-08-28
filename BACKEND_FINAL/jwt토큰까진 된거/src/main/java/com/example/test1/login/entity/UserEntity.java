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
    private List<StoreEntity> likedStores;

    @ManyToMany
    private List<StoreEntity> commentedStores;


    private String role;
}