package com.exam.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;

import com.exam.dto.FileResponse;
import com.exam.dto.ImageRequest;
import com.exam.dto.ImageResponse;
import com.exam.dto.ProfilePicRequest;
import com.exam.exception.BadParameterException;
import com.exam.model.JobMaster;

@Service
@Transactional
public interface ProfilePicService {

	List<JobMaster> getAllJobs();

	FileResponse uploadProfilePicture(ProfilePicRequest profilePicRequest) throws URISyntaxException, UnsupportedOperationException, IOException, IllegalStateException, MultipartException, FileUploadException;

	ImageResponse getImage(ImageRequest imageRequest) throws BadParameterException;

	FileResponse deleteProfilelPic(ProfilePicRequest profilePicRequest);

}
