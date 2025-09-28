package com.alphano.alphano.common.upload;

public class SubmissionKeyGenerator implements KeyGenerator {
    private final Long problemId;
    private final Long userId;
    private final Long submissionId;

    public SubmissionKeyGenerator(Long problemId, Long userId, Long submissionId) {
        this.problemId = problemId;
        this.userId = userId;
        this.submissionId = submissionId;
    }

    @Override
    public String generateKey(String fileName) {
        return "submissions/" + problemId + "/" + userId + "/" + submissionId + "/" + fileName;
    }
}
