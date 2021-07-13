package com.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.FileUploadDetails;

@Repository
public interface FileUploadDetailsRepository extends JpaRepository<FileUploadDetails, Long> {



}
