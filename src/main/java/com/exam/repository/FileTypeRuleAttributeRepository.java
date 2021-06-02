package com.exam.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.model.FileTypeRuleAttribute;

@Repository
@Transactional
public interface FileTypeRuleAttributeRepository extends JpaRepository<FileTypeRuleAttribute, Long> {


	List<FileTypeRuleAttribute> findFileTypeRuleAttributeListByfileTypeAndStatus(String fileType, String statusActive);

}
