# Taskmaster

http://taskmaster-dev.us-west-2.elasticbeanstalk.com/tasks/

## Intro

This is a API using AWS DynamoDb

This application allows users to retrieve all tasks from the database as well
as create and edit.

## API

        GET /tasks - retrieves all tasks from the database and returns in a JSON format
        POST /tasks - creates a task with the following two parameters passed in:
            title = {value}
            description = {value}
        PUT /tasks/{id}/state - changes the state of the task in the following order:
            AVAILABLE -> ASSIGNED -> ACCEPTED -> FINISHED
            Once the task status reaches finished, it can no longer be modified.
            
      
      
## Notes

Used AmazonDynamoDBClientBuilder as AmazonDynamoDBClient is deprecated

No Views, API interaction can be done via postman or any other GET/POST/PUT
programs.

Set server to 5000.