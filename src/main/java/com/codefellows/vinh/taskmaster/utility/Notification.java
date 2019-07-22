package com.codefellows.vinh.taskmaster.utility;

// https://docs.amazonaws.cn/en_us/sns/latest/dg/sms_publish-to-phone.html#sms_publish_sdk
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import com.codefellows.vinh.taskmaster.model.Task;

import java.util.HashMap;
import java.util.Map;

public class Notification {

    public static void sendSMSMessage(Task task) {

        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("mySenderID")
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.25")
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Promotional")
                .withDataType("String"));

        AmazonSNS snsClient = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

        String message = "Hi, " + task.getAssignee() + ". You have task " + task.getTitle() + " waiting for you.";

        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(task.getPhone())
                .withMessageAttributes(smsAttributes));

        System.out.println(result);
    }

    public static void sendEmailMessage(Task task) {
        AmazonSNS snsClient = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

        String messageAdmin = "Task " + task.getTitle() + " has been assigned to " + task.getAssignee() + ".";

        CreateTopicResult createRes = snsClient.createTopic("dynamodb");

        PublishResult resultAdmin = snsClient.publish(new PublishRequest()
                .withMessage(messageAdmin)
                .withTopicArn(createRes.getTopicArn()));

        System.out.println(resultAdmin);
    }
}
