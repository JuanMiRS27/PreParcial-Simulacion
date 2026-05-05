package com.example.preparcialarle.repository;

import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.Evaluation;
import com.example.preparcialarle.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByClaim(Claim claim);
    List<Evaluation> findByClaimUser(User user);
}
