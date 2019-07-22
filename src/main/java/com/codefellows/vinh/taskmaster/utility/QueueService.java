package com.codefellows.vinh.taskmaster.utility;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class QueueService {
    public static void publisher(String queue, String message) {
        String[] msgArray = message.split("/");
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String queueName = sqs.getQueueUrl(queue).getQueueUrl();
        SendMessageRequest sendMessage = new SendMessageRequest()
                .withQueueUrl(queueName)
                .withMessageBody(msgArray[3])
                .withDelaySeconds(5);
        int statusCode = sqs.sendMessage(sendMessage).getSdkHttpMetadata().getHttpStatusCode();
        System.out.println(statusCode + "");
    }
}
