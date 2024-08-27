package com.platform.selfcare.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.platform.selfcare.enums.RoleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="groups")
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 255)
	private String title;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "creator")
	private User creator;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "group_members", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> members = new HashSet<>();
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Blacklist> blacklists = new HashSet<>();
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Candidate> candidates = new HashSet<>();
	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Posting> postings;
	
	@Column(name = "active", nullable = false, columnDefinition = "boolean default true")
	private Boolean active;
	
	@Column(name = "visible", nullable = false, columnDefinition = "boolean default true")
	private Boolean visible;
	
	@Column(updatable = false, nullable = false)
	private Date created;
	
	@Column(nullable = false)
	private Date lastUpdate;
	
	@Transient
	private boolean authorized;
	
	@Transient
	private boolean candidate;
	
	@Transient
	private boolean member;
	
	@Transient
	private boolean owner;
	
	@Transient
	private boolean blacklisted;
	
	@Transient
	private boolean readonly;
	
	public Group() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}
	
	public void addMember(User member) {
		if (this.members == null) {
			this.members = new HashSet<User>();
		}
		this.members.add(member);
	}

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public Set<Blacklist> getBlacklists() {
		return blacklists;
	}

	public void setBlacklists(Set<Blacklist> blacklists) {
		this.blacklists = blacklists;
	}

	public boolean isBlacklisted(User user) {
		if (this.blacklists == null || this.blacklists.size() == 0) {
			return false;
		}
		
		return this.blacklists.stream().anyMatch(b -> b.getUser().getId().equals(user.getId()));
	}
	
	public boolean isBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(boolean blacklisted) {
		this.blacklisted = blacklisted;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public Set<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(Set<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(Candidate candidate) {
		if (this.candidates == null) {
			this.candidates = new HashSet<Candidate>();
		}
		this.candidates.add(candidate);
	}
	
	public boolean isCandidate(User user) {
		if (this.candidates == null || this.candidates.size() == 0) {
			return false;
		}
		
		return this.candidates.stream().anyMatch(c -> c.getUser().getId().equals(user.getId()));
	}
	
	public boolean isCandidate() {
		return candidate;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	public List<Posting> getPostings() {
		return postings;
	}

	public void setPostings(List<Posting> postings) {
		this.postings = postings;
	}

	public Boolean isActive() {
		return this.active == null ? false : this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean isVisible() {
		return this.visible == null ? false : this.visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public boolean isMember(User user) {
		return this.members != null ? this.members.contains(user) : false;
	}
	
	public boolean isAuthorized(User user) {
		return user.hasRole(RoleType.ADMIN.getName()) 
				|| this.getCreator().getId().equals(user.getId()) 
				|| isMember(user);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@PrePersist
	protected void onCreate() {
		this.created = new Date();
		this.lastUpdate = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdate = new Date();
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public boolean isCreator(User user) {
		return this.creator.getId().equals(user.getId());
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}
}
