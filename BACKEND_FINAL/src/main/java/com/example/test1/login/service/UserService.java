package com.example.test1.login.service;

import com.example.test1.board.entity.CommentEntity;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.repository.CommentRepository;
import com.example.test1.board.repository.StoreRepository;
import com.example.test1.login.DTO.UserDTO;
import com.example.test1.login.entity.CommentUserEntity;
import com.example.test1.login.entity.UserCommentedEntity;
import com.example.test1.login.entity.UserEntity;
import com.example.test1.login.entity.UserLikedEntity;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.repository.CommentUserRepository;
import com.example.test1.login.repository.UserCommentedRepository;
import com.example.test1.login.repository.UserLikedRepository;
import com.example.test1.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserLikedRepository userLikedRepository;
    private final UserCommentedRepository userCommentedRepository;
    private final CommentUserRepository commentUserRepository;
    private final JWTUtil jwtUtil;

    public UserDTO findByUserId(String jwtToken){
        String username = jwtUtil.getUsername(jwtToken);
        UserEntity userEntity = userRepository.findByUsername(username);
        int userId = userEntity.getId();
        List<UserLikedEntity> userLikedEntities = userLikedRepository.findByUserId(userId);
        List<Long> userLikedStores = new ArrayList<>();
        if (userLikedEntities != null && !userLikedEntities.isEmpty()) {
            for (UserLikedEntity likedEntity : userLikedEntities) {
                userLikedStores.add(likedEntity.getStore().getId());
            }
        } else {
            userLikedStores.add(-1L); // 기본값으로 -1 추가
        }

        List<UserCommentedEntity> userCommentedEntities = userCommentedRepository.findByUserId(userId);
        List<Long> userCommentedStores = new ArrayList<>();
        if (userCommentedEntities != null && !userCommentedEntities.isEmpty()) {
            for (UserCommentedEntity commentedEntity : userCommentedEntities) {
                userCommentedStores.add(commentedEntity.getComment().getId());
            }
        }else{
            userCommentedStores.add(-1L);
        }
        return new UserDTO(userEntity, userLikedStores, userCommentedStores);
    }

    public boolean isStoreLiked(String userName, Long storeId) {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity != null) {
            List<UserLikedEntity> likedStores = userLikedRepository.findByUserId(userEntity.getId());
            for (UserLikedEntity userLikedEntity : likedStores) {
                if (userLikedEntity.getStore().getId() == storeId) {
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
                UserLikedEntity userLikedEntity = new UserLikedEntity();
                userLikedEntity.setUser(userEntity);
                userLikedEntity.setStore(storeEntity);
                userLikedRepository.save(userLikedEntity);
            }
        }
    }

    @Transactional
    public void unlikeStore(String userName, Long storeId) {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity != null) {
            UserLikedEntity userLikedEntity = userLikedRepository.findByUserIdAndStoreId(userEntity.getId(), storeId);
            if (userLikedEntity != null) {
                userLikedRepository.delete(userLikedEntity);
            }
        }
    }

    @Transactional
    public void commentStore(String userName, Long storeId, Long commentId) {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity != null) {
            StoreEntity storeEntity = storeRepository.findById(storeId).orElse(null);
            CommentEntity commentEntity = commentRepository.findById(commentId).orElse(null);
            if (storeEntity != null && commentEntity != null) {
                UserCommentedEntity userCommentedEntity = new UserCommentedEntity();
                userCommentedEntity.setUser(userEntity);
                userCommentedEntity.setStore(storeEntity);
                userCommentedEntity.setComment(commentEntity);
                userCommentedRepository.save(userCommentedEntity);
                CommentUserEntity commentUserEntity = commentUserRepository.findByIdStoreIdAndIdUserId(storeId, userEntity.getId());
                if(commentUserEntity != null){
                    commentUserEntity.setGrade(commentEntity.getGrade());
                    commentUserRepository.save(commentUserEntity);
                }else{
                    commentUserEntity = new CommentUserEntity(storeEntity,userEntity, commentEntity.getGrade());
                    commentUserRepository.save(commentUserEntity);
                }
            }
        }
    }

    @Transactional // 유저 정보 기반으로 연관된 데이터 먼저 지우고 ->
    public void deleteId(String jwtToken){
        String userName = jwtUtil.getUsername(jwtToken);
        UserEntity userEntity = userRepository.findByUsername(userName);
        int userId = userEntity.getId();
        commentUserRepository.deleteByUserEntityId(userId);
        userCommentedRepository.deleteByUserId(userId);
        userLikedRepository.deleteByUserId(userId);
        commentRepository.deleteByCommentWriterId(userId);
        userRepository.deleteById(userEntity.getId());
    }


    @Transactional
    public void deleteLikeStore(Long id){
        List<UserLikedEntity> likes = userLikedRepository.findByStoreId(id);
        for (UserLikedEntity like : likes) {
            userLikedRepository.delete(like);
        }
    }

    @Transactional
    public void deleteCommentUserStore(Long id){
        List<CommentUserEntity> entityList = commentUserRepository.findByStoreEntityId(id);
        for (CommentUserEntity entity : entityList) {
            commentUserRepository.delete(entity);
        }
    }

    @Transactional
    public void deleteCommentStore(Long id){
        List<UserCommentedEntity> comments = userCommentedRepository.findByStoreId(id);
        for (UserCommentedEntity comment : comments) {
            userCommentedRepository.delete(comment);
        }
    }
}
