package com.platform.selfcare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostingDto {
	
	@NotNull
	@Size(min = 1, message = "Beitrag muss mindestens ein Zeichen enthalten")
	private String text;
	
	private Long postingId;

	public PostingDto() {}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getPostingId() {
		return postingId;
	}

	public void setPostingId(Long postingId) {
		this.postingId = postingId;
	}
}
