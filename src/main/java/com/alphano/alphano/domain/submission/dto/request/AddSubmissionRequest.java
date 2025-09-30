package com.alphano.alphano.domain.submission.dto.request;

import java.util.List;
import java.util.Map;

public record AddSubmissionRequest(
        String language,
        Integer codeLength,
        String fileName,
        Map<String, String> metadata
){}