package com.exam.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.exam.dto.FileRequest;
import com.exam.dto.FileResponse;
import com.exam.exception.FileUploadException;

@Service
public interface FileService {

	public FileResponse uploadFile(FileRequest fileRequest) throws FileUploadException, IOException ;
}
