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

    @RequestMapping(value="/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllTask(){
        Iterable<Task> tasks = taskRepository.findAll();
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(tasks);
    }

    @RequestMapping(value="/tasks", method = RequestMethod.POST)
    public String createTask(@RequestBody Task task){
        if(task.getAssignee() == null) {
            task.setStatus(Status.Available.toString());
        } else {
            task.setStatus(Status.Assigned.toString());
        }
        taskRepository.save(task);
        return "redirect:/tasks/"+task.getId().toString();
    }

    @RequestMapping(value="/tasks/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getTask(@PathVariable UUID id){
        Task selectTask = taskRepository.findById(id);
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(selectTask);
    }

    @RequestMapping(value="/tasks/{id}/state", method = RequestMethod.PUT)
    public String changeTaskState(@PathVariable UUID id){
        Task selectedTask = taskRepository.findById(id);
        if(selectedTask != null && !selectedTask.getStatus().equals("Finished")) {
            Status nextStatus = Status.nextValue(Status.valueOf(selectedTask.getStatus()));
            selectedTask.setStatus(nextStatus.toString());
            taskRepository.save(selectedTask);
            return "redirect:/tasks/"+id;
        } else
        return "redirect:/tasks"+id;
    }

    @RequestMapping(value="/users/{name}/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserTask(@PathVariable String name) {
        Iterable<Task> userTasks = taskRepository.findAllByAssignee(name);
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(userTasks);
    }

    @RequestMapping(value="/tasks/{id}/assign/{assignee}", method = RequestMethod.PUT)
    public String changeTaskAssignee(@PathVariable UUID id, @PathVariable String assignee){
        Task selectedTask = taskRepository.findById(id);
        if(selectedTask != null) {
            selectedTask.setAssignee(assignee);
            selectedTask.setStatus(Status.Assigned.toString());
            taskRepository.save(selectedTask);
            return "redirect:/tasks/"+id;
        } else
            return "redirect:/tasks"+id;
    }
}
