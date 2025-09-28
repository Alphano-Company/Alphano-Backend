package com.alphano.alphano.domain.submission.domain;

public enum SubmissionStatus {
    UPLOADING,  // S3에 업로드 전
    READY,  // S3에 코드 업로드 완료 및 매칭 가능 상태
    FAILED
}
