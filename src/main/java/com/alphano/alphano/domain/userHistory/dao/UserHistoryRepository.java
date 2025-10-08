package com.alphano.alphano.domain.userHistory.dao;

import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.userHistory.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> match(Match match);

    boolean existsByMatchId(Long aLong);

    List<UserHistory> findAllByMatchId(Long matchId);
}
