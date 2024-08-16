package com.platform.selfcare.service;

import java.util.List;
import java.util.Optional;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;

/**
 * implemented in @GroupService
 */
public interface IGroupService {
	
	Optional<Group> getGroupById(Long groupId);
	
	Group createNewGroup(final User creator, final String title, final String description);

	List<Group> getAll();
}
