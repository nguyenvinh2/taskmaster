package com.codefellows.vinh.taskmaster.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Uploader {

    @Value("${amazon.s3.clientRegion}")
    private String clientRegion;
    @Value("${amazon.s3.bucketName}")
    private String bucketName;
    @Value("${amazon.s3.endpoint}")
    private String endpoint;
    @Value("${amazon.s3.accessKey}")
    private String accessKey;
    @Value("${amazon.s3.secretKey}")
    private String secretKey;

    public String uploadFile(String id, MultipartFile file) {
        AWSCredentialsProvider credentials = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return accessKey;
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return secretKey;
                    }
                };
            }

            @Override
            public void refresh() {

            }
        };

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(credentials)
                    .build();

            String fileName = file.getOriginalFilename()+ "_" + LocalDateTime.now();
            File convertedFile = fileConversion(file);

            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, convertedFile);
            s3Client.putObject(request);
            convertedFile.delete();
            return endpoint+"/"+fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File fileConversion(MultipartFile file) {
        try {
            File inputFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(inputFile);
            fos.write(file.getBytes());
            fos.close();
            return inputFile;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
