package com.example.test1.board.entity;

import com.example.test1.board.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "comment_table")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private Integer commentWriterId;

    @Column
    private String commentContents;

    @Column
    private int grade;

    @Column
    private int capacity;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @Column
    private String imageUrl; // 이미지 URL 필드 추가


    public static CommentEntity toSaveEntity(CommentDTO commentDTO, StoreEntity storeEntity, String imageUrl) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentWriterId(commentDTO.getCommentWriterId() != null ? commentDTO.getCommentWriterId() : 0);
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setGrade(commentDTO.getGrade());
        commentEntity.setCapacity(commentDTO.getCapacity());
        commentEntity.setStoreEntity(storeEntity);
        commentEntity.setImageUrl(imageUrl); // 이미지 URL 설정
        return commentEntity;
    }

}

