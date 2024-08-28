package com.example.test1.board.dto;

import com.example.test1.board.entity.CommentEntity;
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
    private String imageUrl;


    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long storeId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentWriterId(commentEntity.getCommentWriterId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setStoreId(storeId);
        commentDTO.setGrade(commentEntity.getGrade());
        commentDTO.setCapacity(commentEntity.getCapacity());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setImageUrl(commentEntity.getImageUrl()); // 이미지 URL 설정
        return commentDTO;
    }

    public static CommentDTO toShowCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentWriterId(commentEntity.getCommentWriterId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setStoreId(commentEntity.getStoreEntity().getId());
        commentDTO.setGrade(commentEntity.getGrade());
        commentDTO.setCapacity(commentEntity.getCapacity());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setImageUrl(commentEntity.getImageUrl()); // 이미지 URL 설정
        return commentDTO;
    }

}
