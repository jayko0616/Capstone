package com.example.test1.board.controller;

import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.service.StoreService;
import com.example.test1.board.service.CommentService;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final CommentService commentService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/save")
    public ResponseEntity<StoreDTO> save(@RequestBody StoreDTO storeDTO) throws IOException {
        StoreDTO savedStoreDTO = storeService.save(storeDTO);
        return new ResponseEntity<>(savedStoreDTO, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<StoreDTO>> findAll() {
        List<StoreDTO> storeDTOList = storeService.findAll();
        return new ResponseEntity<>(storeDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> findById(@PathVariable Long id) {
        StoreDTO storeDTO = storeService.findById(id);
        return new ResponseEntity<>(storeDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> findCommentsByBoardId(@PathVariable Long id) {
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<StoreDTO> update(@RequestBody StoreDTO storeDTO) {
        StoreDTO updatedBoard = storeService.update(storeDTO);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        storeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<StoreDTO>> paging(@PageableDefault(page = 1) Pageable pageable) {
        Page<StoreDTO> storePage = storeService.paging(pageable);
        return new ResponseEntity<>(storePage, HttpStatus.OK);
    }

    @PutMapping("/{storeId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long storeId, @RequestHeader("Authorization") String jwtToken) {
        String userId = jwtUtil.getUsername(jwtToken); // JWT 토큰에서 사용자 ID 추출

        // 사용자가 해당 가게를 이미 좋아요한 상태인지 확인
        boolean isLiked = userService.isStoreLiked(userId, storeId);

        // 좋아요 토글 처리
        if (isLiked) {
            // 이미 좋아요한 경우 -> 좋아요 취소
            userService.unlikeStore(userId, storeId);
            storeService.decreaseHits(storeId);
        } else {
            // 좋아요하지 않은 경우 -> 좋아요 추가
            userService.likeStore(userId, storeId);
            storeService.increaseHits(storeId);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}









