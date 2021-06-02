package com.exam.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.JobMaster;

@Repository
@Transactional
public interface JobMasterRepository extends JpaRepository<JobMaster, Long>{

	JobMaster findJobMasterDetailsByJobNameAndStatus(String jobName, String statusActive);


}
