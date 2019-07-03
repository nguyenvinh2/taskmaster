package com.codefellows.vinh.taskmaster.repository;
import com.codefellows.vinh.taskmaster.model.Task;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    @EnableScan
    List<Task> findAll();

    Task findById(UUID id);

    @EnableScan
    List<Task> findAllByAssignee(String assignee);

    @EnableScan
    void deleteAll();

    @EnableScan
    List<Task> findAllByStatus(String status);
}
