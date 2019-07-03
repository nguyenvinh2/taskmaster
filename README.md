# Taskmaster

http://taskmaster-dev.us-west-2.elasticbeanstalk.com/tasks/

## Intro

This is a API using AWS DynamoDb

This application allows users to retrieve all tasks from the database as well
as create edit, and delete each task.

## API

        GET /tasks - retrieves all tasks from the database and returns in a JSON format
        GET /tasks?status={available, assigned, accepted, finished} - filters the list
            of task based on the status options provided. More than one option can inserted,
            separated by comma.
        
        GET /users/{name}/tasks - retrieves list of task based on assignee provided
        GET /users/{name}/tasks?status={available, assigned, accepted, finished} -
            filters list of task assigned to user based on status provided
      
        POST /tasks - creates a task with the following two parameters passed in:
            title = {value}
            description = {value}
            assignee = {value}
            
        PUT /tasks/{id}/state - changes the state of the task in the following order:
            AVAILABLE -> ASSIGNED -> ACCEPTED -> FINISHED
            Once the task status reaches finished, it can no longer be modified.
            
        PUT /tasks/{id}/assign/{assignee} - assigns selected task to assignee provided
        
        DELETE /tasks - deletes all tasks
        DELETE /tasks/{id} - deletes selected task
            
## Notes

Used AmazonDynamoDBClientBuilder as AmazonDynamoDBClient is deprecated

No Views, API interaction can be done via postman or any other GET/POST/PUT
programs.

Set server to 5000.