package com.platform.selfcare.enums;

public enum TokenType {

	PASSWORD_TOKEN(1, "valid"),
	REGISTER_TOKEN(2, "notExistingToken");
	
	private Integer id;
	private String name;
	
	TokenType(int id, String name) {
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
