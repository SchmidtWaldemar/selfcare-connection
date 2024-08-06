package com.platform.selfcare.entity;

import java.io.Serializable;
import com.platform.selfcare.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="role")
public class Role implements Serializable {
	
	private static final long serialVersionUID = 712170309927474394L;

	/**
	 * we use static id out from type definition: @RoleType
	 */
	@Id
	private Long id;
	
	@Column(nullable = false, unique = true, length = 20)
	private String name;

	public Role() {}
	
	public Role(RoleType type) {
		this.id = Long.valueOf(type.getId());
		this.name = type.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object item) {
		if (this == item) {
			return true;
		}
		if (item == null) {
			return false;
		}
		if (getClass() != item.getClass()) {
			return false;
		}
		final Role role = (Role) item;
		if (!this.name.equals(role.getName())) {
			return false;
		}
		return true;
	}
}
