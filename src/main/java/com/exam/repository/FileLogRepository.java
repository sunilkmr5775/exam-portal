package com.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.FileLog;

@Repository
public interface FileLogRepository extends JpaRepository<FileLog, Long> {



}
