package com.platform.selfcare.entity;

import java.util.HashSet;
import java.util.Set;

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
	private Set<Posting> replies = new HashSet<>();
	
	@ManyToOne
	private Posting replied;
	
	@Column(name = "visible", nullable = false, columnDefinition = "boolean default true")
	private Boolean visible;
	
	Posting() {}

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

	public Set<Posting> getReplies() {
		return replies;
	}

	public void setReplies(Set<Posting> replies) {
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
}
