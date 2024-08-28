package com.example.test1.board.entity;

import com.example.test1.board.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "comment_table")
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


    public static CommentEntity toSaveEntity(CommentDTO commentDTO, StoreEntity storeEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentWriterId(commentDTO.getCommentWriterId() != null ? commentDTO.getCommentWriterId() : 0);
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setGrade(commentDTO.getGrade()); // CommentDTO에서 가져와야 함
        commentEntity.setCapacity(commentDTO.getCapacity()); // CommentDTO에서 가져와야 함
        commentEntity.setStoreEntity(storeEntity);
        return commentEntity;
    }

}
