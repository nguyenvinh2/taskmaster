package com.codefellows.vinh.taskmaster.repository;
import com.codefellows.vinh.taskmaster.model.Task;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    @EnableScan
    Iterable<Task> findAll();

    Task findById(UUID id);

    @EnableScan
    Iterable<Task> findAllByAssignee(String assignee);
}
