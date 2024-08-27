package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.Posting;
import com.platform.selfcare.entity.User;

import jakarta.transaction.Transactional;

@Transactional
public interface PostingRepository extends JpaRepository<Posting, Long> {
	
	List<Posting> findByCreator(User creator);
	
	List<Posting> findByGroup(Group group);

	List<Posting> findByGroupAndRepliedIsNull(Group group);

	@Modifying
	@Query("UPDATE Posting p SET p.active = false WHERE p = :posting")
	void deactivatePosting(Posting posting);

	@Modifying
	@Query("UPDATE Posting p SET p.active = true WHERE p = :posting")
	void reactivatePosting(Posting posting);

	@Modifying
	@Query("UPDATE Posting p SET p.visible = true WHERE p = :posting")
	void setPostingVisible(Posting posting);

	@Modifying
	@Query("UPDATE Posting p SET p.visible = false WHERE p = :posting")
	void hidePosting(Posting posting);
}
