package com.example.test1.login.entity;

import com.example.test1.board.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class UserLikedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikedEntity that = (UserLikedEntity) o;
        return Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(store.getId(), that.store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), store.getId());
    }
}
