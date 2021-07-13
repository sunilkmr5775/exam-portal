package com.exam.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.exam.constant.ExceptionConstant;
import com.exam.constant.JobConstant;
import com.exam.constant.StatusConstant;
import com.exam.dto.FileResponse;
import com.exam.dto.ImageRequest;
import com.exam.dto.ImageResponse;
import com.exam.dto.ProfilePicRequest;
import com.exam.exception.BadParameterException;
import com.exam.exception.NoDataFoundException;
import com.exam.model.JobMaster;
import com.exam.model.ProfilePic;
import com.exam.repository.JobMasterRepository;
import com.exam.repository.ProfilePicRepository;
import com.exam.service.ProfilePicService;
import com.exam.util.CommonUtil;
import com.exam.util.FileUtil;
import com.google.common.io.Files;

@Service
public class ProfilePicSeriveImpl implements ProfilePicService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfilePicSeriveImpl.class);

	@Autowired
	JobMasterRepository jobMasterRepository;

	@Autowired
	ProfilePicRepository imageRepository;

	@Autowired
	private FileUtil fileUtil;

	@Override
	public FileResponse uploadProfilePicture(ProfilePicRequest profilePicRequest) throws IllegalStateException,
			MultipartException, URISyntaxException, UnsupportedOperationException, IOException, FileUploadException {
		FileResponse fileResponse = new FileResponse();

		String jobName = profilePicRequest.getJobName();
		MultipartFile multipartFile = profilePicRequest.getFile();
		String username = profilePicRequest.getUsername();
//		String firstName = profilePicRequest.getFirstName() != null ? profilePicRequest.getFirstName() : "";
//		String lastName = profilePicRequest.getLastName() != null ? profilePicRequest.getLastName() : "";

		ProfilePic profilePic = imageRepository.findAllByUsername(username);
		if (profilePic != null) {
			JobMaster jobMasterDetails = jobMasterRepository.findJobMasterDetailsByJobNameAndStatus(jobName,
					StatusConstant.STATUS_ACTIVE);
			if (jobMasterDetails != null) {

				LOGGER.info("jobMaster Record: " + jobMasterDetails.toString());

				if (jobName.equalsIgnoreCase(JobConstant.PROFILE_PIC_DELETE_JOB)) {
					fileResponse = deleteProfilelPic(profilePicRequest);
				} else {

					try {
						String fileType = jobMasterDetails.getFileType();
//						if (FileUtil.checkImageFileExt(multipartFile.getOriginalFilename())) {
							boolean isImageSizeValid = false;
//							isImageSizeValid = fileUtil.validateFileRequest(multipartFile, "PROFILE_PIC_UPLOAD");
					if (fileUtil.validateFileRequest(multipartFile, fileType)) {
//							if (isImageSizeValid) {
								String fileExtension = FileUtil
										.getFileExtension(new File(multipartFile.getOriginalFilename()));
								String finalFileName = username + "." + fileExtension;

								String inputFilePath = Paths.get(jobMasterDetails.getFileInPath()).toString();
								File inFile = new File(inputFilePath);
								if (!inFile.exists()) {
									FileUtils.forceMkdir(inFile);
								}
								FileResponse fileRes = FileUtil.wirteToInputFile(multipartFile,
										jobMasterDetails.getFileInPath(), finalFileName);

								if (StatusConstant.STATUS_SUCCESS.equalsIgnoreCase(fileRes.getStatus())) {
									String filePath = Paths.get(jobMasterDetails.getFileInPath(), finalFileName)
											.toString();

									String outputFilePath = Paths.get(jobMasterDetails.getFileOutPath()).toString();
									File outFile = new File(outputFilePath);
									if (!outFile.exists()) {
										FileUtils.forceMkdir(outFile);
									}

									try {

										File inputFile = new File(filePath);
										LOGGER.info("Input file path: " + filePath);

										String fileArchivePath = outputFilePath + "/" + finalFileName;
										File outputFile = new File(fileArchivePath);
										try {
											Files.move(inputFile, outputFile);
											fileResponse.setUsername(username);
											fileResponse.setStatus(fileRes.getStatus());
											fileResponse.setJobName(jobName);
//									fileResponse.setFileName(finalFileName);
//									fileResponse.setFilePath(outFile.toString());
											LOGGER.info("File moved from " + inputFile.toPath() + "to"
													+ outputFile.toPath());

											fileResponse = processImage(multipartFile, username, outFile.toString(),
													finalFileName);
											if (StatusConstant.STATUS_SUCCESS
													.equalsIgnoreCase(fileResponse.getStatus())) {
												fileResponse.setUsername(username);
												fileResponse.setErrorCode(ExceptionConstant.IMAGE_SAVED_IN_DB_EC);
												fileResponse
														.setErrorDescription(ExceptionConstant.IMAGE_SAVED_IN_DB_ED);
												fileResponse.setStatus(fileRes.getStatus());
												fileResponse.setJobName(jobName);
												fileResponse.setFileName(finalFileName);
												fileResponse.setFilePath(outFile.toString());
//										fileResponse.setHostName(CommonUtil.getHostName());
//										fileResponse.setHostAddress(CommonUtil.getHostAddress());
											} else {
												LOGGER.error(ExceptionConstant.IMAGE_NOT_SAVED_IN_DB_ED);
												fileResponse.setErrorCode(ExceptionConstant.IMAGE_NOT_SAVED_IN_DB_EC);
												fileResponse.setErrorDescription(
														ExceptionConstant.IMAGE_NOT_SAVED_IN_DB_ED);
												fileResponse.setStatus(fileRes.getStatus());
												fileResponse.setJobName(jobName);
											}

										} catch (IOException e) {
											LOGGER.error("File not readable exception...", e.getMessage());
											fileResponse.setUsername(username);
											fileResponse.setJobName(jobName);
											fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
											fileResponse.setErrorCode(ExceptionConstant.IO_ERROR_OCCURED_EC);
											fileResponse.setErrorDescription(e.getMessage());
										}

									} catch (Exception e) {
										LOGGER.error(
												ExceptionConstant.REGULAR_ERROR_OCCURED_ED + " : " + e.getMessage());
										fileResponse.setUsername(username);
										fileResponse.setJobName(jobName);
										fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
										fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
										fileResponse.setErrorDescription(e.getMessage());
										throw new FileUploadException(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
									}

								} else {
									LOGGER.error(ExceptionConstant.ERROR_WRITING_FILE_ED);
									fileResponse.setUsername(username);
									fileResponse.setJobName(jobName);
									fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
									fileResponse.setErrorCode(ExceptionConstant.ERROR_WRITING_FILE_EC);
									fileResponse.setErrorDescription(ExceptionConstant.ERROR_WRITING_FILE_ED);
								}
//							} else {
//
//								LOGGER.error(ExceptionConstant.IMAGE_SIZE_EXCEEDS_ED);
//								fileResponse.setErrorCode(ExceptionConstant.IMAGE_SIZE_EXCEEDS_EC);
//								fileResponse.setErrorDescription(ExceptionConstant.IMAGE_SIZE_EXCEEDS_ED);
//								fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
//								fileResponse.setJobName(jobName);
//								throw new MaxUploadSizeExceededException(1024);
////						throw new com.ooredoo.app.exception.FileUploadException(ExceptionConstant.IMAGE_SIZE_EXCEEDS_EC,
////								ExceptionConstant.IMAGE_SIZE_EXCEEDS_ED);
//							}
						} else {

							LOGGER.error(ExceptionConstant.INVALID_IMAGE_FORMAT_ED);
							fileResponse.setUsername(username);
							fileResponse.setJobName(jobName);
							fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
							fileResponse.setErrorCode(ExceptionConstant.INVALID_IMAGE_FORMAT_EC);
							fileResponse.setErrorDescription(ExceptionConstant.INVALID_IMAGE_FORMAT_ED);
						}
					} catch (Throwable th) {
						LOGGER.error(ExceptionConstant.REGULAR_ERROR_OCCURED_ED + " : " + th.getMessage());
						fileResponse.setUsername(username);
						fileResponse.setJobName(jobName);
						fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
						fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
						fileResponse.setErrorDescription(th.getMessage());
//				throw new MaxUploadSizeExceededException(multipartFile.getSize());
					}
				}
			} else {
				LOGGER.error(ExceptionConstant.JOB_NOT_FOUND_ED);
			    fileResponse.setUsername(username);
				fileResponse.setJobName(jobName);
				fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
				fileResponse.setErrorCode(ExceptionConstant.JOB_NOT_FOUND_EC);
				fileResponse.setErrorDescription(ExceptionConstant.JOB_NOT_FOUND_ED);
			}
		} else {
			LOGGER.error(ExceptionConstant.USER_NOT_REGISTERED_ED);
		    fileResponse.setUsername(username);
			fileResponse.setJobName(jobName);
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			fileResponse.setErrorCode(ExceptionConstant.USER_NOT_REGISTERED_EC);
			fileResponse.setErrorDescription(ExceptionConstant.USER_NOT_REGISTERED_ED);
		}
		return fileResponse;
	}

	
	
	@Override
	public ImageResponse getImage(ImageRequest imageRequest) throws BadParameterException {

		String imageType = null;
		String username = null;
		ImageResponse imageResponse = new ImageResponse();
		if (CommonUtil.isStringNotNullandEmpty(imageRequest.getImageType())) {
			imageType = imageRequest.getImageType();
		} else {
			imageResponse.setStatus(StatusConstant.STATUS_FAILURE);
			imageResponse.setErrorCode(ExceptionConstant.IMAGE_TYPE_REQUIRED_EC);
			imageResponse.setErrorDescription(ExceptionConstant.IMAGE_TYPE_REQUIRED_ED/* +" "+e.getMessage() */);
			return imageResponse;
		}
		if (CommonUtil.isStringNotNullandEmpty(imageRequest.getImageType())) {
			username = imageRequest.getUsername();
		} else {
			imageResponse.setStatus(StatusConstant.STATUS_FAILURE);
			imageResponse.setErrorCode(ExceptionConstant.MSISDN_REQUIRED_EC);
			imageResponse.setErrorDescription(ExceptionConstant.MSISDN_REQUIRED_ED);
			return imageResponse;
		}

		if (StatusConstant.PROFILE_IMAGE.equalsIgnoreCase(imageType)) {
			try {
				Optional<ProfilePic> profilePicModel = Optional.ofNullable(imageRepository.findAllByUsername(username));

				if (!profilePicModel.isPresent() || profilePicModel == null) {
//				throw new NoDataFoundException(ExceptionConstant.RECORD_NOT_FOUND_EC);
					imageResponse.setStatus(StatusConstant.STATUS_FAILURE);
					imageResponse.setErrorCode(ExceptionConstant.RECORD_NOT_FOUND_EC);
					imageResponse.setErrorDescription(ExceptionConstant.RECORD_NOT_FOUND_ED);
					return imageResponse;
				}

				imageResponse.setStatus(StatusConstant.STATUS_SUCCESS);
				imageResponse.setProfilePicStatus(profilePicModel.get().getStatus());
				imageResponse.setFileName(profilePicModel.get().getFileName());
				imageResponse.setUsername(profilePicModel.get().getUsername());
				imageResponse.setFileType(profilePicModel.get().getFileType());
				imageResponse.setImageType(profilePicModel.get().getImageType());
				imageResponse.setFilePath(profilePicModel.get().getFileInPath());
				imageResponse.setFilePath(profilePicModel.get().getFileInPath());
				imageResponse.setPicByte(FileUtil.decompressBytes(profilePicModel.get().getPicByte()));
			} catch (Exception ex) {
				throw new NoDataFoundException(ex.getMessage());
			}
		} else {
			imageResponse.setStatus(StatusConstant.STATUS_FAILURE);
			imageResponse.setErrorCode(ExceptionConstant.INVALID_IMAGE_TYPE_EC);
			imageResponse.setErrorDescription(ExceptionConstant.INVALID_IMAGE_TYPE_ED);
		}

		return imageResponse;
	}
	
	
	
	
	
	public FileResponse deleteProfilelPic(ProfilePicRequest profilePicRequest) {

		FileResponse fileResponse = new FileResponse();
		try {
			JobMaster jobMaster = jobMasterRepository.findJobMasterDetailsByJobNameAndStatus(
					profilePicRequest.getJobName(), StatusConstant.STATUS_ACTIVE);

			if (null != jobMaster) {
				ProfilePic imageModel = imageRepository.findAllByUsername(profilePicRequest.getUsername());
				File file = new File(jobMaster.getFileOutPath() + "/" + imageModel.getFileName());

				if (file.exists()) {
					
					FileResponse fileRes = deleteFromDb(imageModel);
					if (fileRes.getStatus().equalsIgnoreCase(StatusConstant.STATUS_SUCCESS)) {
						FileUtils.forceDelete(file);
						fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
						fileResponse.setUsername(profilePicRequest.getUsername());
						fileResponse.setJobName(profilePicRequest.getJobName());
						fileResponse.setFilePath(jobMaster.getFileOutPath() + "/" + profilePicRequest.getUsername());
						fileResponse.setFileName(imageModel.getFileName());
						fileResponse.setErrorCode(ExceptionConstant.IMAGE_DELETED_FROM_DB_EC);
						fileResponse.setErrorDescription(ExceptionConstant.IMAGE_DELETED_FROM_DB_ED);
					} else {
						fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
					}

				} else {
					throw new com.exam.exception.FileUploadException(ExceptionConstant.FILE_NOT_EXIST_EC,
							ExceptionConstant.FILE_NOT_EXIST_ED);
				}

			} else {
				throw new com.exam.exception.FileUploadException(ExceptionConstant.JOB_NOT_FOUND_EC,
						ExceptionConstant.JOB_NOT_FOUND_ED);

			}

		} catch (com.exam.exception.FileUploadException fileUploadException) {

			LOGGER.error(fileUploadException.toString());
			fileResponse.setErrorCode(fileUploadException.getErrorCode());
			fileResponse.setErrorDescription(fileUploadException.getMessage());
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);

		} catch (Exception e) {
			fileResponse.setErrorCode(ExceptionConstant.UNKNOWN_ERROR_EC);
			fileResponse.setErrorDescription(ExceptionConstant.UNKNOWN_ERROR_ED);
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);

			LOGGER.error(e.getMessage());

		}
		return fileResponse;
	}

	private FileResponse processImage(MultipartFile multipartFile, String username, String outFile,
			String finalFileName) throws FileUploadException {
		FileResponse fileResponse = new FileResponse();
		try {
			ProfilePic imageModel = imageRepository.findAllByUsername(username);
			if (imageModel != null) {
				long recordId = updateImageToDb(imageModel, multipartFile, username, outFile.toString(), finalFileName);
				if (recordId > 0) {
					fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
				} else {
					fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
				}
			} else {
				Long recordId = saveImageToDb(multipartFile, username, outFile.toString(), finalFileName);
				if (recordId > 0) {
					fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
				} else {
					fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
				}
			}
		} catch (Exception ex) {
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			throw new FileUploadException(ExceptionConstant.FILE_UPLOAD_EX_ED);

		}

		return fileResponse;

	}

	private Long saveImageToDb(MultipartFile multipartFile, String username, String filePath, String finalFileName)
			throws FileUploadException {

		ProfilePic profilePicModel = new ProfilePic();
		try {
			profilePicModel.setUsername(username);
			profilePicModel.setPicByte(FileUtil.compressBytes(multipartFile.getBytes()));
			profilePicModel.setFileName(finalFileName);
			profilePicModel.setFileInPath(filePath);
			profilePicModel.setFileType(multipartFile.getContentType());
			profilePicModel.setImageType(StatusConstant.PROFILE_IMAGE);
			profilePicModel.setStatus(StatusConstant.STATUS_ACTIVE1);
			profilePicModel.setCreatedBy(username);
			profilePicModel.setCreatedDate(LocalDateTime.now());
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			throw new FileUploadException(ExceptionConstant.FILE_UPLOAD_EX_ED);
		}
		LOGGER.info("Image saved to db...");
		return imageRepository.save(profilePicModel).getId();
	}

	private Long updateImageToDb(ProfilePic profilePic, MultipartFile multipartFile, String username, String filePath,
			String finalFileName) throws FileUploadException {
		try {
			profilePic.setUsername(username);
			profilePic.setPicByte(FileUtil.compressBytes(multipartFile.getBytes()));
			profilePic.setFileName(finalFileName);
			profilePic.setFileInPath(filePath);
			profilePic.setImageType(StatusConstant.PROFILE_IMAGE);
			profilePic.setFileType(multipartFile.getContentType());
			profilePic.setStatus(StatusConstant.STATUS_ACTIVE1);
//			profilePic.setCreatedBy(username);
//			profilePic.setCreatedDate(LocalDateTime.now());
			profilePic.setModifiedBy(username);
			profilePic.setModifiedDate(LocalDateTime.now());

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new FileUploadException(ExceptionConstant.FILE_UPLOAD_EX_ED);
		}
		LOGGER.info("Image saved to db...");
		return imageRepository.save(profilePic).getId();
	}

	private FileResponse deleteFromDb(ProfilePic imageModel2) throws FileUploadException, IOException {
		FileResponse fileResponse = new FileResponse();
		long id = imageModel2.getId();
//		imageModel2.setFileInPath("");
//		imageModel2.setFileName("");
//		imageModel2.setFileType("");
//		imageModel2.setImageType("");
//		imageModel2.setModifiedBy("");
//		imageModel2.setModifiedDate(null);
//		imageModel2.setPicByte(null);
		imageModel2.setStatus(false);
		
		imageRepository.save(imageModel2);
		fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
		fileResponse.setErrorCode(ExceptionConstant.IMAGE_DELETED_FROM_DB_EC);
		fileResponse.setErrorDescription(ExceptionConstant.IMAGE_DELETED_FROM_DB_ED);
		LOGGER.info("Image deleted from db for MSISDN..." + imageModel2);
		return fileResponse;
	}

	@Override
	public List<JobMaster> getAllJobs() {
		List<JobMaster> jobMasterDetailsList = jobMasterRepository.findAll();
		return jobMasterDetailsList;
	}





}
