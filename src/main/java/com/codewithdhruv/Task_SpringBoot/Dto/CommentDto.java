package com.codewithdhruv.Task_SpringBoot.Dto;


import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;
    private String content;
    private Date createdAt;
    private Long taskId;
    private Long userId;
    private String postedBy;

}
