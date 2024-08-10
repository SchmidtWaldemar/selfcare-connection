package com.platform.selfcare.enums;

public enum TokenStatus {

	TOKEN_VALID(1, "valid"),
	TOKEN_NOT_EXIST(2, "notExistingToken"),
	TOKEN_INVALID(3, "invalidToken"),
	TOKEN_EXPIRED(4, "expired"),
	ERROR(5, "error");
	
	private Integer id;
	private String name;
	
	TokenStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
