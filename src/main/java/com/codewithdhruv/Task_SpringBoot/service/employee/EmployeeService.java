package com.codewithdhruv.Task_SpringBoot.service.employee;

import com.codewithdhruv.Task_SpringBoot.Dto.TaskDTO;

import java.util.List;

public interface EmployeeService {

    List<TaskDTO> getTaskByUserId();

    TaskDTO updateTask(Long id,String status);
}
