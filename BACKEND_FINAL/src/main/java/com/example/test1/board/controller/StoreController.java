package com.example.test1.board.controller;

import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.service.StoreService;
import com.example.test1.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final CommentService commentService;


    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute StoreDTO storeDTO, MultipartRequest request, @RequestHeader("Authorization") String jwtToken) throws IOException {
        storeService.save(storeDTO, jwtToken, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
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

    @GetMapping("/storeCode/{storeCode}")
    public ResponseEntity<StoreDTO> findByStoreCode(@PathVariable String storeCode) {
        StoreDTO storeDTO = storeService.findByStoreCode(storeCode);
        return new ResponseEntity<>(storeDTO, HttpStatus.OK);
    }

    @GetMapping("/storeName/{storeName}")
    public ResponseEntity<StoreDTO> findByName(@PathVariable String storeName) {
        StoreDTO storeDTO = storeService.findByStoreName(storeName);
        return new ResponseEntity<>(storeDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> findCommentsByBoardId(@PathVariable Long id) {
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute StoreDTO storeDTO, MultipartRequest request, @RequestHeader("Authorization") String jwtToken) throws IOException {
        ResponseEntity<StoreDTO> updatedBoard = storeService.update(storeDTO, jwtToken, request);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String jwtToken) {
        storeService.delete(id, jwtToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/{storeId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long storeId, @RequestHeader("Authorization") String jwtToken) {
        storeService.toggleLike(jwtToken, storeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}









