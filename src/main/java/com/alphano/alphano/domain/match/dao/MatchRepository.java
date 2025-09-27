package com.alphano.alphano.domain.match.dao;

import com.alphano.alphano.domain.match.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
