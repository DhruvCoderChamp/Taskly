package com.codewithdhruv.Task_SpringBoot.controller.employee;

import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;
import com.codewithdhruv.Task_SpringBoot.entities.Task;
import com.codewithdhruv.Task_SpringBoot.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTaskByUserId(){
        return ResponseEntity.ok(employeeService.getTaskByUserId());

    }

    @PutMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,@PathVariable String status){
        TaskDTO updateTaskDto = employeeService.updateTask(id,status);
        if(updateTaskDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updateTaskDto);

    }
}
