package com.codewithdhruv.Task_SpringBoot.controller.admin;

import com.codewithdhruv.Task_SpringBoot.Dto.CommentDto;
import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;
import com.codewithdhruv.Task_SpringBoot.entities.Task;
import com.codewithdhruv.Task_SpringBoot.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTaskDTO  =  adminService.createTask(taskDTO);
        if(createdTaskDTO == null) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(){
        return ResponseEntity.ok(adminService.getAllTasks());
    }


    @DeleteMapping("/task/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        adminService.deleteTask(id);

        // Return a proper JSON response instead of plain text
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getTaskById(id));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = adminService.updateTask(id,taskDTO);
        if(updatedTask==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/search/{title}")
    public ResponseEntity<List<TaskDTO>> searchTitle(@PathVariable String title){
        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content) {
        CommentDto commentDto = adminService.createComment(taskId,content);
        if(commentDto==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @GetMapping("/task/comments/{taskId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));

    }
}


