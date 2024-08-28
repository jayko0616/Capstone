package com.example.test1.board.service;


import com.example.test1.board.dto.CommentAIDTO;
import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.entity.CommentEntity;
import com.example.test1.board.repository.StoreRepository;
import com.example.test1.board.repository.CommentRepository;
import com.example.test1.login.entity.UserEntity;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public Long save(CommentDTO commentDTO, String jwtToken) {

        String username = jwtUtil.getUsername(jwtToken);
        /* 부모 엔티티(BoardEntity) 조회 */
        Optional<StoreEntity> optionalStoreEntity = storeRepository.findById(commentDTO.getStoreId());
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByUsername(username));

        if (optionalStoreEntity.isPresent() && optionalUserEntity.isPresent()) {
            StoreEntity storeEntity = optionalStoreEntity.get();
            UserEntity userEntity = optionalUserEntity.get();

            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, storeEntity);
            commentEntity.setCommentWriter(username);
            commentEntity.setCommentWriterId(userEntity.getId());

            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<CommentDTO> findAll(Long storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByStoreEntityOrderByIdDesc(storeEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, storeId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }


    @Transactional
    public void saveCommentedStore(String userId, Long storeId) {
        // 사용자를 DB에서 가져옴
        UserEntity user = userRepository.findByUsername(userId);
        if (user != null) {
            // 사용자의 commentedStores 목록에 댓글이 달린 가게를 추가
            StoreEntity store = storeRepository.findById(storeId).orElse(null);
            if (store != null) {
                user.getCommentedStores().add(store);
                userRepository.save(user);
            }
        }
    }


    public void updateStoreRating(Long storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId).orElse(null);
        if (storeEntity != null) {
            List<CommentEntity> comments = commentRepository.findByStoreEntityId(storeId);
            double totalRating = 0;
            for (CommentEntity comment : comments) {
                totalRating += comment.getGrade();
            }
            double avgRating = totalRating / comments.size();
            // 소수점 첫 번째 자리까지 반올림하여 출력
            avgRating = Math.round(avgRating * 10.0) / 10.0;
            storeEntity.setAvgRating(avgRating);
            storeRepository.save(storeEntity);
        }
    }


    public List<CommentAIDTO> findAllComments() {
        List<CommentEntity> commentEntities = commentRepository.findAll();
        return commentEntities.stream()
                .map(commentEntity -> {
                    CommentAIDTO commentAIDTO = new CommentAIDTO();
                    commentAIDTO.setCommentWriterId(commentEntity.getCommentWriterId());
                    commentAIDTO.setStoreId(commentEntity.getStoreEntity().getId());
                    commentAIDTO.setGrade(commentEntity.getGrade());
                    return commentAIDTO;
                })
                .collect(Collectors.toList());
    }



}
