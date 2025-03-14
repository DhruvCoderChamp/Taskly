package com.codewithdhruv.Task_SpringBoot.service.employee;

import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;
import com.codewithdhruv.Task_SpringBoot.entities.Task;
import com.codewithdhruv.Task_SpringBoot.entities.User;
import com.codewithdhruv.Task_SpringBoot.enums.TaskStatus;
import com.codewithdhruv.Task_SpringBoot.repositories.TaskRepository;
import com.codewithdhruv.Task_SpringBoot.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;


    public List<TaskDTO> getTaskByUserId() {
        User user = jwtUtil.getLoggedInUser();

        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        return taskRepository.findAllByUserId(user.getId())
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
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
    @Override
    public TaskDTO updateTask(Long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task existingTask =  optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRepository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }
}
