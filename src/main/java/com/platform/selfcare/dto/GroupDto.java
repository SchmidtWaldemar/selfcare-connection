package com.platform.selfcare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GroupDto {

	@NotNull
	@Size(min = 3, max = 255, message = "Titel muss min. 3 und max. 255 Zeichen enthalten")
	private String title;
	
	private String description;
	
	private Long groupId;
	
	private String msgToCreator;
	
	public GroupDto() {}

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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getMsgToCreator() {
		return msgToCreator;
	}

	public void setMsgToCreator(String msgToCreator) {
		this.msgToCreator = msgToCreator;
	}	
}
