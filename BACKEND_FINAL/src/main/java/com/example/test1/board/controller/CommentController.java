package com.example.test1.board.controller;

import com.example.test1.board.dto.CommentDTO;
import com.example.test1.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO, MultipartRequest request, @RequestHeader("Authorization") String jwtToken) throws IOException {
        commentService.save(commentDTO, jwtToken, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CommentDTO>> findByUser(@RequestHeader("Authorization") String jwtToken){
        List<CommentDTO> commentDTOList = commentService.findAllCommentByUser(jwtToken);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String jwtToken) {
        commentService.delete(id, jwtToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CommentDTO commentDTO, @RequestHeader("Authorization") String jwtToken){
        commentService.updateComment(commentDTO, jwtToken);
        return ResponseEntity.ok().build();
    }


}
