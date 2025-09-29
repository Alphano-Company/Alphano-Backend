package com.alphano.alphano.common.application;

import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.common.exception.InternalServerError;
import com.alphano.alphano.common.upload.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

import static com.alphano.alphano.common.consts.AlphanoStatic.ALPHANO_PUBLIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.presigned.expire-minutes}")
    private long expireMinutes;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String createPublicUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", ALPHANO_PUBLIC, region, key);
    }

    public String createPresignedGetUrl(String bucketName, String keyName) {
        try {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expireMinutes))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            log.info("Presigned URL: [{}]", presignedRequest.url().toString());
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        } catch (Exception e){
            log.error("Presigned URL 생성 실패: {}/{}", bucketName, keyName, e);
            throw InternalServerError.EXCEPTION;
        }
    }

    public S3Response createPresignedPutUrl(
            String bucketName,
            String fileName,
            Map<String, String> metadata,
            KeyGenerator keyGenerator
    ) {
        try {
            String key = keyGenerator.generateKey(fileName);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .metadata(metadata)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expireMinutes))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            log.info("Presigned URL 생성: {}", presignedRequest.url());

            return S3Response.of(key, presignedRequest.url().toExternalForm());
        } catch (Exception e) {
            log.error("Presigned URL 생성 실패", e);
            throw InternalServerError.EXCEPTION;
        }
    }

    public boolean objectExists(String bucketName, String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }
}
