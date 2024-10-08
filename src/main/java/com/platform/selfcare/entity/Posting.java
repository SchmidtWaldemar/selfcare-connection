package com.platform.selfcare.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="posting")
public class Posting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn
	private User creator;

	@ManyToOne
	@JoinColumn(name = "groupRef")
	private Group group;
	
	@Column(name = "text", columnDefinition = "TEXT")
	private String text;
	
	@OneToMany(mappedBy = "replied", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Posting> replies;
	
	@ManyToOne
	private Posting replied;
	
	@Column(name = "visible", nullable = false, columnDefinition = "boolean default true")
	private Boolean visible;
	
	@Column(name = "active", nullable = false, columnDefinition = "boolean default true")
	private Boolean active;
	
	@Column(updatable = false, nullable = false)
	private Date created;
	
	@Transient
	private boolean owner;
	
	@Transient
	private Long groupId;
	
	Posting() {}
		
	public Posting(User creator, Group group, String text) {
		this.creator = creator;
		this.group = group;
		this.text = text;
		this.created = new Date();
		this.visible = true;
		this.active = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Posting> getReplies() {
		return replies;
	}

	public void setReplies(List<Posting> replies) {
		this.replies = replies;
	}

	public Posting getReplied() {
		return replied;
	}

	public void setReplied(Posting replied) {
		this.replied = replied;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	public Boolean isActive() {
		return this.active == null ? false : this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public boolean isCreator(User user) {
		return this.creator.getId().equals(user.getId());
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	
	public boolean isOwner() {
		return owner;
	}

	public boolean isMember() {
		return this.group.isMember();
	}
}
