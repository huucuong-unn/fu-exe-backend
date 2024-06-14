package com.exe01.backend.fileStore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.exe01.backend.bucket.BucketName;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.CannedAccessControlList;


import java.util.Optional;
import java.util.Map;
import java.io.InputStream;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    //    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetedata, InputStream inputStream) throws BaseException {
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        optionalMetedata.ifPresent(map -> {
//            if (!map.isEmpty()) {
//                map.forEach(metadata::addUserMetadata);
//            }
//        });
//        try {
//            PutObjectRequest putObjectRequest = new PutObjectRequest(BucketName.PROFILE_IMAGE.getBucketName(), fileName, inputStream, metadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead);
//
//            s3.putObject(putObjectRequest);
////            s3.putObject("tortee-image-upload", fileName, inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
//        } catch (AmazonServiceException e) {
//throw new BaseException(ErrorCode.ERROR_500.getCode(), "Failed to store file to s3", ErrorCode.ERROR_500.getMessage());
//        }
//
//    }
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetadata, InputStream inputStream) throws BaseException {
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach((key, value) -> {
                    if (key.equalsIgnoreCase("Content-Type")) {
                        metadata.setContentType(value);
                    } else if (key.equalsIgnoreCase("Content-Length")) {
                        metadata.setContentLength(Long.parseLong(value));
                    } else {
                        metadata.addUserMetadata(key, value);
                    }
                });
            }
        });

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest("tortee-image-upload", fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            s3.putObject(putObjectRequest);
        } catch (AmazonServiceException e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "Failed to store file to s3", ErrorCode.ERROR_500.getMessage());
        }
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | java.io.IOException e) {
            throw new IllegalStateException("Failed to download file to s3", e);
        }
    }
}
