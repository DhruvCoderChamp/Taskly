package com.codewithdhruv.Task_SpringBoot.service.admin;

import com.codewithdhruv.Task_SpringBoot.Dto.CommentDto;
import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;
import com.codewithdhruv.Task_SpringBoot.Dto.UserDto;
import com.codewithdhruv.Task_SpringBoot.entities.Comment;
import com.codewithdhruv.Task_SpringBoot.entities.Task;
import com.codewithdhruv.Task_SpringBoot.entities.User;
import com.codewithdhruv.Task_SpringBoot.enums.TaskStatus;
import com.codewithdhruv.Task_SpringBoot.enums.UserRole;
import com.codewithdhruv.Task_SpringBoot.repositories.CommentRepository;
import com.codewithdhruv.Task_SpringBoot.repositories.TaskRepository;
import com.codewithdhruv.Task_SpringBoot.repositories.UserRepository;
import com.codewithdhruv.Task_SpringBoot.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user->user.getUserRole()== UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if(optionalUser.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            return taskRepository.save(task).getTaskDTO();
        }
        return null;
    }


    public List<TaskDTO> getAllTasks() {
         return taskRepository.findAll()
                 .stream()
                 .sorted(Comparator.comparing(Task::getDueDate).reversed())
                 .map(Task::getTaskDTO)
                 .collect(Collectors.toList());
    }


    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask =taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if(optionalTask.isPresent() && optionalUser.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
         return taskRepository.findAllByTitleContaining(title)
                 .stream()
                 .sorted(Comparator.comparing(Task::getDueDate).reversed())
                 .map(Task::getTaskDTO)
                 .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();
        if((optionalTask.isPresent()) && user!=null){
            Comment comment = new Comment();
            comment.setCreatedAt((new Date()));
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDTO();

        }
        throw new EntityNotFoundException("User or task not found");
    }

    @Override
    public List<CommentDto> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDTO)
                .collect(Collectors.toList());
    }

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }



}
