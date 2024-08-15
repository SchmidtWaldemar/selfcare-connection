package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.User;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	
	@Query("SELECT c FROM Candidate c WHERE c.candidate = ?1")
	List<Candidate> findCandidatures(User user);
}
