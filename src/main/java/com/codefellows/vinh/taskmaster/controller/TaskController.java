package com.codefellows.vinh.taskmaster.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.codefellows.vinh.taskmaster.model.Status;
import com.codefellows.vinh.taskmaster.model.Task;
import com.codefellows.vinh.taskmaster.repository.TaskRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getTask(){
        Iterable<Task> tasks = taskRepository.findAll();
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(tasks);
    }

    @RequestMapping(value="/tasks", method= RequestMethod.POST)
    public String createTask(@RequestBody Task task){
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @RequestMapping(value="/tasks/{id}/state", method= RequestMethod.PUT)
    public String editTask(@PathVariable UUID id){
        Task selectedTask = taskRepository.findById(id);
        if(selectedTask != null && !selectedTask.getStatus().equals("Finished")) {
            Status nextStatus = Status.nextValue(Status.valueOf(selectedTask.getStatus()));
            selectedTask.setStatus(nextStatus.toString());
            taskRepository.save(selectedTask);
            return "redirect:/tasks";
        } else
        return "redirect:/tasks";
    }
}
