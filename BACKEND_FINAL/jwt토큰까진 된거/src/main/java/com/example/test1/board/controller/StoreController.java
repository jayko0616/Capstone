package com.example.test1.board.controller;

import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.service.StoreService;
import com.example.test1.board.service.CommentService;
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

    @PutMapping("/increase-hits/{id}")
    public ResponseEntity<Void> increaseHits(@PathVariable Long id) {
        storeService.increaseHits(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/decrease-hits/{id}")
    public ResponseEntity<Void> decreaseHits(@PathVariable Long id) {
        storeService.decreaseHits(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}









