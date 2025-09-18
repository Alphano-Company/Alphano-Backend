package com.alphano.alphano.domain.problem.dao;

import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.dto.query.ProblemSummaryQuery;
import com.alphano.alphano.domain.problem.dto.response.ProblemSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query(
            value = """
                    select new com.alphano.alphano.domain.problem.dto.query.ProblemSummaryQuery(
                        p.id,
                        p.title,
                        p.submissionCount, 
                        p.submitterCount,
                        pi.imageKey
            )
            from Problem p
            left join p.iconKey pi
            """,
            countQuery = "select count(p) from Problem p"
    )
    Page<ProblemSummaryQuery> findAllSummary(Pageable pageable);
}
