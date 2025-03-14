package com.codewithdhruv.Task_SpringBoot.repositories;

import com.codewithdhruv.Task_SpringBoot.Dto.CommentDto;
import com.codewithdhruv.Task_SpringBoot.entities.Comment;
import com.codewithdhruv.Task_SpringBoot.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskId(Long taskId);
}
