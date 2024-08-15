package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
	
	List<Group> findByCreator(User creator);
}
