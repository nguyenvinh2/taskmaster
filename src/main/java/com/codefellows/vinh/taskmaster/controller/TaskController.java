package com.codefellows.vinh.taskmaster.controller;


import com.amazonaws.util.CollectionUtils;
import com.codefellows.vinh.taskmaster.model.Status;
import com.codefellows.vinh.taskmaster.model.Task;
import com.codefellows.vinh.taskmaster.repository.TaskRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllTask(@RequestParam(required = false) String[] status) {
        List<Task> tasks = new ArrayList<>();
        if (status == null && status.length > 0) {
            tasks = taskRepository.findAll();
        } else {
            for(int i = 0; i < status.length; i++) {
                String filteredStatus = StringUtils.capitalize(status[i].toLowerCase());
                tasks = CollectionUtils.mergeLists(tasks, taskRepository.findAllByStatus(filteredStatus));
            }
        }
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(tasks);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public String createTask(@RequestBody Task task) {
        if (task.getAssignee() == null) {
            task.setStatus(Status.Available.toString());
        } else {
            task.setStatus(Status.Assigned.toString());
        }
        taskRepository.save(task);
        return "redirect:/tasks/" + task.getId().toString();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getTask(@PathVariable UUID id) {
        Task selectTask = taskRepository.findById(id);
        Gson jsonConvert = new Gson();
        return jsonConvert.toJson(selectTask);
    }

    @RequestMapping(value = "/tasks/{id}/state", method = RequestMethod.PUT)
    public String changeTaskState(@PathVariable UUID id) {
        Task selectedTask = taskRepository.findById(id);
        if (selectedTask != null && !selectedTask.getStatus().equals("Finished")) {
            Status nextStatus = Status.nextValue(Status.valueOf(selectedTask.getStatus()));
            selectedTask.setStatus(nextStatus.toString());
            taskRepository.save(selectedTask);
            return "redirect:/tasks/" + id;
        } else
            return "redirect:/tasks" + id;
    }

    @RequestMapping(value = "/users/{name}/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserTask(@PathVariable String name, @RequestParam(required = false) String[] status) {
        Gson jsonConvert = new Gson();
        List<Task> userTasks = taskRepository.findAllByAssignee(name);
        if (status != null && status.length > 0) {
            List<Task> filteredTasks = new ArrayList<>();
            for(int i = 0; i < status.length; i++) {
                String filteredStatus = StringUtils.capitalize(status[i].toLowerCase());
                filteredTasks = CollectionUtils.mergeLists(filteredTasks, userTasks.stream().filter(task -> task.getStatus().equals(filteredStatus)).collect(Collectors.toList()));
            }
            return jsonConvert.toJson(filteredTasks);
        }
        return jsonConvert.toJson(userTasks);
    }

    @RequestMapping(value = "/tasks/{id}/assign/{assignee}", method = RequestMethod.PUT)
    public String changeTaskAssignee(@PathVariable UUID id, @PathVariable String assignee) {
        Task selectedTask = taskRepository.findById(id);
        if (selectedTask != null) {
            selectedTask.setAssignee(assignee);
            selectedTask.setStatus(Status.Assigned.toString());
            taskRepository.save(selectedTask);
            return "redirect:/tasks/" + id;
        } else
            return "redirect:/tasks" + id;
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask(@PathVariable UUID id) {
        Task selectedTasks = taskRepository.findById(id);
        if (selectedTasks != null) {
            taskRepository.delete(selectedTasks);
            return "{\"message\": \"Task#" + id + "has successfully been deleted.\"}";
        } else {
            return "{\"message\": \"Cannot find Task#" + id + ".\"}";
        }
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteAllTask() {
        taskRepository.deleteAll();
        return "{\"message\": \"All tasks have been successfully deleted.\"}";
    }
}
