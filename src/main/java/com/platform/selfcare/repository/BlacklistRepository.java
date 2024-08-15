package com.platform.selfcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.selfcare.entity.Blacklist;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
	
}
