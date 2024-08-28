package com.example.test1.login.service;

import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.repository.StoreRepository;
import com.example.test1.login.DTO.UserDTO;
import com.example.test1.login.entity.UserEntity;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public UserDTO findByUserId(String jwtToken){
        String username = jwtUtil.getUsername(jwtToken);
        UserEntity userEntity = userRepository.findByUsername(username);
        return new UserDTO(userEntity);
    }

    public boolean isStoreLiked(String userId, Long storeId) {
        UserEntity userEntity = userRepository.findByUsername(userId);
        if (userEntity != null) {
            List<StoreEntity> likedStores = userEntity.getLikedStores();
            for (StoreEntity store : likedStores) {
                if (store.getId().equals(storeId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public void likeStore(String userId, Long storeId) {
        UserEntity userEntity = userRepository.findByUsername(userId);
        if (userEntity != null) {
            StoreEntity storeEntity = storeRepository.findById(storeId).orElse(null);
            if (storeEntity != null) {
                userEntity.getLikedStores().add(storeEntity);
                userRepository.save(userEntity);
            }
        }
    }

    @Transactional
    public void unlikeStore(String userId, Long storeId) {
        UserEntity userEntity = userRepository.findByUsername(userId);
        if (userEntity != null) {
            StoreEntity storeEntity = storeRepository.findById(storeId).orElse(null);
            if (storeEntity != null) {
                userEntity.getLikedStores().remove(storeEntity);
                userRepository.save(userEntity);
            }
        }
    }
}
