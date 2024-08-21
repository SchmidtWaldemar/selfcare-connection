package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.Posting;
import com.platform.selfcare.entity.User;

public interface PostingRepository extends JpaRepository<Posting, Long> {
	
	List<Posting> findByCreator(User creator);
	
	List<Posting> findByGroup(Group group);

	List<Posting> findByGroupAndRepliedIsNull(Group group);
}
