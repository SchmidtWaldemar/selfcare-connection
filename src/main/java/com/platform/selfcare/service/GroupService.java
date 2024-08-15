package com.platform.selfcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}
