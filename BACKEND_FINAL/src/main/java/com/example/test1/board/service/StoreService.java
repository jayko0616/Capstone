package com.example.test1.board.service;

import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.repository.StoreRepository;
import com.example.test1.cloud.s3.ImageService;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final CommentService commentService;
    private final UserService userService;
    private final ImageService imageService;
    private final JWTUtil jwtUtil;

    @Transactional
    public void save(StoreDTO storeDTO, String jwtToken, MultipartRequest request) throws IOException {
        Optional<StoreEntity> storeName = storeRepository.findByStoreName(storeDTO.getStoreName());
        Optional<StoreEntity> storeCode = storeRepository.findByStoreCode(storeDTO.getStoreCode());
        System.out.println(storeDTO.isDrink());
        String username = jwtUtil.getUsername(jwtToken);
        if(storeDTO.getRating() == null){
            storeDTO.setRating(0.0);
        }
        if(storeDTO.getCapacity() == null){
            storeDTO.setCapacity(0);
        }
        MultipartFile file = request.getFile("upload");
        String imageUrl = "";

        if (file != null && !file.isEmpty()) {
            imageUrl = imageService.imageUpload(request);
        }
        if(!storeName.isPresent() && !storeCode.isPresent() && username.equals("master")){
            StoreEntity storeEntity = StoreEntity.toSaveEntity(storeDTO, imageUrl);
            storeRepository.save(storeEntity);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store name or user is not authorized.");
        }
    }

    @Transactional
    public List<StoreDTO> findAll() {
        List<StoreEntity> storeEntityList = storeRepository.findAll();
        List<StoreDTO> storeDTOList = new ArrayList<>();
        for (StoreEntity storeEntity : storeEntityList) {
            storeDTOList.add(StoreDTO.toStoreDTO(storeEntity));
        }
        return storeDTOList;
    }

    @Transactional
    public void increaseHits(Long id) {
        storeRepository.increaseHits(id);
    }

    @Transactional
    public void decreaseHits(Long id) {
        storeRepository.decreaseHits(id);
    }

    @Transactional
    public void toggleLike(String jwtToken, Long storeId){
        String userName = jwtUtil.getUsername(jwtToken);

        boolean isLiked = userService.isStoreLiked(userName, storeId);

        // 좋아요 토글 처리
        if (isLiked) {
            // 이미 좋아요한 경우 -> 좋아요 취소
            userService.unlikeStore(userName, storeId);
            decreaseHits(storeId);
        } else {
            // 좋아요하지 않은 경우 -> 좋아요 추가
            userService.likeStore(userName, storeId);
            increaseHits(storeId);
        }
    }

    @Transactional
    public StoreDTO findById(Long id) {
        Optional<StoreEntity> optionalBoardEntity = storeRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            StoreEntity storeEntity = optionalBoardEntity.get();
            StoreDTO storeDTO = StoreDTO.toStoreDTO(storeEntity);
            return storeDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store Id.");
        }
    }

    @Transactional
    public StoreDTO findByStoreCode(String storeCode) {
        Optional<StoreEntity> optionalBoardEntity = storeRepository.findByStoreCode(storeCode);
        if (optionalBoardEntity.isPresent()) {
            StoreEntity storeEntity = optionalBoardEntity.get();
            StoreDTO storeDTO = StoreDTO.toStoreDTO(storeEntity);
            return storeDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input.");
        }
    }

    @Transactional
    public StoreDTO findByStoreName(String storeName) {
        Optional<StoreEntity> optionalBoardEntity = storeRepository.findByStoreName(storeName);
        if (optionalBoardEntity.isPresent()) {
            StoreEntity storeEntity = optionalBoardEntity.get();
            StoreDTO storeDTO = StoreDTO.toStoreDTO(storeEntity);
            return storeDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input.");
        }
    }

    @Transactional
    public ResponseEntity<StoreDTO> update(StoreDTO storeDTO, String jwtToken, MultipartRequest request) throws IOException {
        StoreEntity existingStore = storeRepository.findById(storeDTO.getId()).orElse(null);
        if (existingStore == null) {
            return ResponseEntity.notFound().build(); // 스토어를 찾을 수 없는 경우
        }
        MultipartFile file = request.getFile("upload");
        String imageUrl = "";

        String userName = jwtUtil.getUsername(jwtToken);
        if(userName.equals("master")){
            if(storeDTO.getStoreCode() != null){
                existingStore.setStoreCode(storeDTO.getStoreCode());
            }
//            existingStore.setStoreCode(storeDTO.getStoreCode());
            if(storeDTO.getStoreName() != null){
                existingStore.setStoreName(storeDTO.getStoreName());
            }
//            existingStore.setStoreName(storeDTO.getStoreName());
            if(storeDTO.getStoreContent() != null){
                existingStore.setStoreContents(storeDTO.getStoreContent());
            }
//            existingStore.setStoreContents(storeDTO.getStoreContent());
            if(storeDTO.getCoordinateX() != null){
                existingStore.setCoordinateX(storeDTO.getCoordinateX());
            }
            if(storeDTO.getCoordinateY() != null){
                existingStore.setCoordinateY(storeDTO.getCoordinateY());
            }
            if (file != null && !file.isEmpty()) {
                imageUrl = imageService.imageUpload(request);
                existingStore.setImageUrl(imageUrl);
            }

            // 엔티티 저장
            storeRepository.save(existingStore);
        }
        return ResponseEntity.ok(StoreDTO.toStoreDTO(existingStore));
    }

    @Transactional // 리뷰 삭제도 해야될듯?
    public void delete(Long StoreId, String jwtToken) {
        String userName = jwtUtil.getUsername(jwtToken);
        if(userName.equals("master")){
            userService.deleteCommentStore(StoreId); //유저의 리뷰 정보 삭제
            userService.deleteCommentUserStore(StoreId);
            commentService.deleteCommentStore(StoreId); // 리뷰(댓글) 삭제
            userService.deleteLikeStore(StoreId); // 가게에 대한 좋아요 삭제
            storeRepository.deleteById(StoreId); // 가게 삭제
        }

    }

}














