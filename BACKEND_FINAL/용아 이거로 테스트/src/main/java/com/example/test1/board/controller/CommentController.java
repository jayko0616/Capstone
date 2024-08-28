package com.example.test1.board.controller;

import com.example.test1.board.dto.CommentAIDTO;
import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.service.CommentService;
import com.example.test1.login.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody CommentDTO commentDTO, @RequestHeader("Authorization") String jwtToken) {
        String userId = jwtUtil.getUsername(jwtToken);
        // CommentDTO에 사용자 ID 설정
        commentDTO.setCommentWriter(userId);
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO, jwtToken);
        if (saveResult != null) {
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getStoreId());
            commentService.saveCommentedStore(userId, commentDTO.getStoreId());
            commentService.updateStoreRating(commentDTO.getStoreId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<CommentAIDTO>> findAll() {
        List<CommentAIDTO> commentAIDTOList = commentService.findAllComments();
        return new ResponseEntity<>(commentAIDTOList, HttpStatus.OK);
    }


}
