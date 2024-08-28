package com.example.test1.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentAIDTO {
    private Integer commentWriterId;
    private Long storeId;
    private int grade;
}
