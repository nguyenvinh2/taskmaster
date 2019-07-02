package com.codefellows.vinh.taskmaster.controller;

import com.codefellows.vinh.taskmaster.model.Task;
import com.codefellows.vinh.taskmaster.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value="/tasks", method= RequestMethod.GET)
    @ResponseBody
    public String getTask(){
        Iterable<Task> tasks = taskRepository.findAll();
        return "something";
    }

}
