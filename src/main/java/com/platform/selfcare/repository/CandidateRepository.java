package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.User;

import jakarta.transaction.Transactional;

@Transactional
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	
	void deleteById(Long id);
	
	@Query("SELECT c FROM Candidate c WHERE c.user = ?1")
	List<Candidate> findCandidatures(User user);

	@Query("SELECT c FROM Candidate c JOIN Group g ON c.group.id = g.id WHERE g.creator = ?1")
	List<Candidate> findAllByGroupCreator(User creator);
}
