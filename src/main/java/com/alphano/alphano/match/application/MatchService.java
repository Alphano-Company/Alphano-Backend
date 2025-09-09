package com.alphano.alphano.match.application;

import com.alphano.alphano.match.dto.request.MatchRequest;
import com.alphano.alphano.match.dto.response.MatchResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    public MatchResponse create(Long problemId, @Valid MatchRequest request) {
    }
}
