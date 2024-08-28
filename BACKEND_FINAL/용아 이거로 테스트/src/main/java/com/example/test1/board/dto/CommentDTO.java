package com.example.test1.board.dto;

import com.example.test1.board.entity.CommentEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class  CommentDTO {
    private Long id;
    private String commentWriter;
    private Integer commentWriterId;
    private String commentContents;
    private Long storeId;
    private int grade;
    private int capacity;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long storeId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentWriterId(commentEntity.getCommentWriterId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setStoreId(commentDTO.getStoreId());
        commentDTO.setGrade(commentDTO.getGrade());
        commentDTO.setCapacity(commentEntity.getCapacity());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setStoreId(storeId);
        return commentDTO;
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentWriterId(commentEntity.getCommentWriterId());
        commentDTO.setStoreId(commentEntity.getStoreEntity().getId());
        commentDTO.setGrade(commentEntity.getGrade());
        return commentDTO;
    }
}
