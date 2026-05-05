package com.example.preparcialarle.repository;

import com.example.preparcialarle.model.Claim;
import com.example.preparcialarle.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUser(User user);
}
