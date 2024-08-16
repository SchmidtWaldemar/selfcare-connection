package com.platform.selfcare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.GroupRepository;
import com.platform.selfcare.repository.PostingRepository;
import com.platform.selfcare.repository.UserRepository;

@Service
public class GroupService implements IGroupService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	PostingRepository postingRepository;

	@Override
	public Optional<Group> getGroupById(Long groupId) {
		return this.groupRepository.findById(groupId);
	}

	@Override
	public Group createNewGroup(User creator, String title, String description) {
		Group group = new Group();
		group.setCreator(creator);
		group.setTitle(title);
		group.setDescription(description);
		group.setActive(true);
		
		return this.groupRepository.save(group);
	}

	@Override
	public List<Group> getAll() {
		return this.groupRepository.findAll();
	}
	
}
