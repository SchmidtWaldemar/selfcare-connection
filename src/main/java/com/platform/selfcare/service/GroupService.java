package com.platform.selfcare.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.selfcare.entity.Blacklist;
import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.Posting;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.BlacklistRepository;
import com.platform.selfcare.repository.CandidateRepository;
import com.platform.selfcare.repository.GroupRepository;
import com.platform.selfcare.repository.PostingRepository;
import com.platform.selfcare.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class GroupService implements IGroupService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	PostingRepository postingRepository;

	@Autowired
	CandidateRepository candidateRepository;
	
	@Autowired
	BlacklistRepository blacklistRepository;
	
	@Autowired
	ISendMailService sendMailService;
	
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
		group.setVisible(true);
		return this.groupRepository.save(group);
	}

	@Override
	public List<Group> getAll() {
		return this.groupRepository.findAll();
	}

	@Override
	public boolean createCandidate(Candidate candidate) {
		if (candidate.getUser() == null || candidate.getGroup() == null || !candidate.getGroup().isActive()) {
			return false;
		}
		
		if (candidate.getGroup().isCandidate(candidate.getUser())
				|| candidate.getGroup().isBlacklisted(candidate.getUser())) {
			// check if candidate is already in blacklist
			// or candidate already send request
			return false;
		}
		
		Candidate savedCandidate = this.candidateRepository.save(candidate);
		candidate.getGroup().addCandidate(savedCandidate);
		
		try {
			// send e-mail to creator
			sendMailService.sendCandidateStatus(candidate.getUser(), candidate.getGroup().getCandidates().size());
		} catch (MessagingException | IOException e) {
			// TODO set only logging info
		}
		
		return true;
	}

	@Override
	public List<Candidate> getAllCandidatesByCreator(User creator) {
		return this.candidateRepository.findAllByGroupCreator(creator);
	}

	@Override
	public boolean changeCandidateStatus(Candidate candidate, boolean feedbackStatus) {
		if (candidate.getUser().isEnabled()) {
			if (feedbackStatus && !candidate.getGroup().isMember(candidate.getUser())) {
				candidate.getGroup().addMember(candidate.getUser());
				this.groupRepository.save(candidate.getGroup());
			}
			else if (!feedbackStatus) {
				Blacklist blacklist = new Blacklist();
				blacklist.setGroup(candidate.getGroup());
				blacklist.setUser(candidate.getUser());
				blacklist.setReason("declined by creator");
				this.blacklistRepository.save(blacklist);
			}
			
			return true;
		}
		return false;
	}

	@Override
	public Optional<Candidate> findByCandidateId(Long candidateId) {
		return this.candidateRepository.findById(candidateId);
	}

	@Override
	public void savePosting(Posting posting) {
		this.postingRepository.save(posting);
	}

	@Override
	public Optional<Posting> findPostingById(Long postingId) {
		return this.postingRepository.findById(postingId);
	}

	@Override
	public List<Posting> findByGroup(Group group) {
		return this.postingRepository.findByGroup(group);
	}

	@Override
	public List<Posting> findByGroupAndNoParent(Group group) {
		return this.postingRepository.findByGroupAndRepliedIsNull(group);
	}

	@Override
	public void removeGroup(Group group) {
		this.groupRepository.delete(group);
	}

	@Override
	public void deactivateGroup(Group group) {
		this.groupRepository.deactivateGroup(group);
	}

	@Override
	public void showGroup(Group group) {
		this.groupRepository.setGroupVisible(group);
	}

	@Override
	public void hideGroup(Group group) {
		this.groupRepository.hideGroup(group);
	}

	@Override
	public void reactivateGroup(Group group) {
		this.groupRepository.reactivateGroup(group);
	}

	@Override
	public void removePosting(Posting posting) {
		this.postingRepository.delete(posting);
	}

	@Override
	public void deactivatePosting(Posting posting) {
		this.postingRepository.deactivatePosting(posting);
	}

	@Override
	public void reactivatePosting(Posting posting) {
		this.postingRepository.reactivatePosting(posting);
	}

	@Override
	public void showPosting(Posting posting) {
		this.postingRepository.setPostingVisible(posting);
	}

	@Override
	public void hidePosting(Posting posting) {
		this.postingRepository.hidePosting(posting);
	}

	@Override
	public void deleteCandidate(Candidate candidate) {
		this.candidateRepository.deleteById(candidate.getId());
	}
}
