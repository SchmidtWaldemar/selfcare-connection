package com.platform.selfcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;

import jakarta.transaction.Transactional;

@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {
	
	List<Group> findByCreator(User creator);
	
	@Modifying
	@Query("UPDATE Group g SET g.visible = true WHERE g = :group")
	void setGroupVisible(Group group);
	
	@Modifying
	@Query("UPDATE Group g SET g.visible = false WHERE g = :group")
	void hideGroup(Group group);
	
	@Modifying
	@Query("UPDATE Group g SET g.active = false WHERE g = :group")
	void deactivateGroup(Group group);
	
	@Modifying
	@Query("UPDATE Group g SET g.active = true WHERE g = :group")
	void reactivateGroup(Group group);
}
