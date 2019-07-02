package com.codefellows.vinh.taskmaster.repository;
import com.codefellows.vinh.taskmaster.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
