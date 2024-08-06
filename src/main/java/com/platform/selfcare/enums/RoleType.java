package com.platform.selfcare.enums;

/**
 * all existing role types
 */
public enum RoleType {
	/**
	 * registered user
	 */
	USER(1, "ROLE_USER"),
	/**
	 * user with all rights
	 */
	ADMIN(2, "ROLE_ADMIN");
	
	private Integer id;
	private String name;
	
	RoleType(int id, String name) {
		this.setId(id);
		this.setName(name);
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
