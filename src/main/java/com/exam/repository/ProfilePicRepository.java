package com.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.model.ProfilePic;

public interface ProfilePicRepository extends JpaRepository<ProfilePic, Long> {

	ProfilePic findAllByUsername(String username);


}
