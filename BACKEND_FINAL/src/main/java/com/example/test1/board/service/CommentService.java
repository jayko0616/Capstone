package com.example.test1.board.service;

import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.entity.CommentEntity;
import com.example.test1.board.repository.StoreRepository;
import com.example.test1.board.repository.CommentRepository;
import com.example.test1.cloud.s3.ImageService;
import com.example.test1.login.entity.UserEntity;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.repository.UserCommentedRepository;
import com.example.test1.login.repository.UserRepository;
import com.example.test1.login.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserCommentedRepository userCommentedRepository;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final ImageService imageService;

    @Transactional
    public void save(CommentDTO commentDTO, String jwtToken, MultipartRequest request) throws IOException {

        String username = jwtUtil.getUsername(jwtToken);
        /* 부모 엔티티(BoardEntity) 조회 */
        Optional<StoreEntity> optionalStoreEntity = storeRepository.findById(commentDTO.getStoreId());
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByUsername(username));
        MultipartFile file = request.getFile("upload");
        String imageUrl = "";

        if (file != null && !file.isEmpty()) {
            imageUrl = imageService.imageUpload(request);
        }

        if (optionalStoreEntity.isPresent() && optionalUserEntity.isPresent()) {
            StoreEntity storeEntity = optionalStoreEntity.get();
            UserEntity userEntity = optionalUserEntity.get();

            storeEntity.setCapacity(commentDTO.getCapacity());
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, storeEntity, imageUrl);
            commentEntity.setCommentWriter(username);
            commentEntity.setCommentWriterId(userEntity.getId());

            Long commentId = commentRepository.save(commentEntity).getId();
            userService.commentStore(username, storeEntity.getId(), commentId);
            updateStoreRating(storeEntity.getId());
        }
    }

    @Transactional
    public void delete(Long commentId, String jwtToken){
        String username = jwtUtil.getUsername(jwtToken);
        UserEntity userEntity = userRepository.findByUsername(username);
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){
            if(commentEntity.get().getCommentWriterId() == userEntity.getId()){
                userCommentedRepository.deleteByCommentId(commentId);
                commentRepository.deleteById(commentId);
            }
        }
    }
    @Transactional
    public void deleteCommentStore(Long id){
        List<CommentEntity> comments = commentRepository.findByStoreEntityId(id);
        for (CommentEntity comment : comments) {
            commentRepository.delete(comment);
        }
    }


    @Transactional
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
    public List<CommentDTO> findAllCommentByUser(String jwtToken) {
        String username = jwtUtil.getUsername(jwtToken);
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByUsername(username));
        if(optionalUserEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input.");
        }
        List<CommentEntity> commentEntityList = commentRepository.findByCommentWriterId(optionalUserEntity.get().getId());
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toShowCommentDTO(commentEntity);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }


    @Transactional
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


    @Transactional
    public void updateComment(CommentDTO commentDTO, String jwtToken){
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentDTO.getId());
        if(!commentEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input.");
        }
        CommentEntity existingComment = commentEntity.get();
        String userName = jwtUtil.getUsername(jwtToken);
        UserEntity userEntity = userRepository.findByUsername(userName);
        if(existingComment.getCommentWriterId() == userEntity.getId()){
            existingComment.setCommentContents(commentDTO.getCommentContents());
            existingComment.setGrade(commentDTO.getGrade());
            existingComment.setCapacity(commentDTO.getCapacity());
            commentRepository.save(existingComment);
        }
    }

}
