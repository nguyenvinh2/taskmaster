package com.codefellows.vinh.taskmaster;

import com.codefellows.vinh.taskmaster.model.Status;
import com.codefellows.vinh.taskmaster.model.Task;
import com.codefellows.vinh.taskmaster.repository.TaskRepository;
import com.codefellows.vinh.taskmaster.utility.Uploader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class TaskmasterApplication {
	@Autowired
	private TaskRepository taskRepository;


	public static void main(String[] args) {
		SpringApplication.run(TaskmasterApplication.class, args);
	}

}
