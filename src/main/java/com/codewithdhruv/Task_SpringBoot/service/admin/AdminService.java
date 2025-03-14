package com.codewithdhruv.Task_SpringBoot.service.admin;

import com.codewithdhruv.Task_SpringBoot.Dto.CommentDto;
import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;
import com.codewithdhruv.Task_SpringBoot.Dto.UserDto;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasks();

    void deleteTask(Long id);

    TaskDTO getTaskById(Long id);

    TaskDTO updateTask(Long id,TaskDTO taskDTO);

    List<TaskDTO> searchTaskByTitle(String title);

    CommentDto createComment(Long taskId,String content);

    List<CommentDto> getCommentsByTaskId(Long taskId);
}
