package com.exam.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exam.constant.ExceptionConstant;
import com.exam.constant.JobConstant;
import com.exam.constant.StatusConstant;
import com.exam.dto.FileRequest;
import com.exam.dto.FileResponse;
import com.exam.exception.FileUploadException;
import com.exam.model.Category;
import com.exam.model.FileLog;
import com.exam.model.FileUploadDetails;
import com.exam.model.JobMaster;
import com.exam.model.Quiz;
import com.exam.repository.CategoryRepository;
import com.exam.repository.FileLogRepository;
import com.exam.repository.FileUploadDetailsRepository;
import com.exam.repository.JobMasterRepository;
import com.exam.repository.QuizRepository;
import com.exam.service.FileService;
import com.exam.util.CommonUtil;
import com.exam.util.FileUtil;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfilePicSeriveImpl.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	FileUploadDetailsRepository fileUploadDetailsRepository;

	@Autowired
	FileLogRepository fileLogRepository;

	@Autowired
	private JobMasterRepository jobMasterRepository;

	@Autowired
	private FileUtil fileUtil;

	@SuppressWarnings("unused")
	@Override
	public FileResponse uploadFile(FileRequest fileRequest) throws FileUploadException, IOException {
		FileResponse fileResponse = new FileResponse();

		String jobName = fileRequest.getJobName();
		String userName = fileRequest.getUserName();
		MultipartFile multipartFile = fileRequest.getFile();
		JobMaster jobMasterDetails = null;
		Long fileLogId = 0L;

		try {
			jobMasterDetails = jobMasterRepository.findJobMasterDetailsByJobNameAndStatus(jobName,
					StatusConstant.STATUS_ACTIVE);
			LOGGER.info("jobMaster Record: " + jobMasterDetails.toString());

			if (null == jobMasterDetails) {
				LOGGER.error(ExceptionConstant.JOB_NOT_FOUND_ED);
				System.out.println(ExceptionConstant.JOB_NOT_FOUND_ED);
				fileResponse.setFileName(multipartFile.getOriginalFilename());
				fileResponse.setJobName(jobName);
				fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
				fileResponse.setErrorCode(ExceptionConstant.JOB_NOT_FOUND_EC);
				fileResponse.setErrorDescription(ExceptionConstant.JOB_NOT_FOUND_ED);
				fileResponse.setUsername(userName);
			} else {
				String fileType = jobMasterDetails.getFileType();
				String fileExtension = FileUtil.getFileExtension(new File(multipartFile.getOriginalFilename()));
				if (fileUtil.validateFileRequest(multipartFile, fileType)) {
//					if (isImageSizeValid) {
					String finalFileName = fileType.replace("BULK_UPLOAD_", "") + "_" + CommonUtil.currentDateTime()
							+ "." + fileExtension;

					String inputFilePath = Paths.get(jobMasterDetails.getFileInPath()).toString();
					File inFile = new File(inputFilePath);
					if (!inFile.exists()) {
						FileUtils.forceMkdir(inFile);
					}
					String outputFilePath = Paths.get(jobMasterDetails.getFileOutPath()).toString();
					File outFile = new File(outputFilePath);
					if (!outFile.exists()) {
						FileUtils.forceMkdir(outFile);
					}

					FileResponse fileRes = FileUtil.wirteToInputFile(multipartFile, inputFilePath, finalFileName);

					if (StatusConstant.STATUS_SUCCESS.equalsIgnoreCase(fileRes.getStatus())) {
						String filePath = Paths.get(inputFilePath, finalFileName).toString();

						try {

							File inputFile = new File(filePath);
							LOGGER.info("Input file path: " + filePath);

							String fileArchivePath = inputFilePath + "/" + finalFileName;
							String fileArchiveOutputPath = outputFilePath + "/" + finalFileName;
							File outputFile = new File(fileArchiveOutputPath);

							try {
								FileReader filereader = new FileReader(fileArchivePath);

//								Find Total Records
								long totalRecords = Files.lines(Paths.get(fileArchivePath)).count();

								System.out.println("Total Record Count : " + (totalRecords - 1));
								FileLog fileLog = new FileLog();
								fileLog.setFileType(fileType);
								fileLog.setFileName(finalFileName);
								fileLog.setTotalCount(totalRecords - 1);
								fileLog.setSuccessCount(0L);
								fileLog.setFailureCount(0L);
								fileLog.setStatus(StatusConstant.STATUS_SUBMITTED);
								fileLog.setCreatedBy(userName);
								fileLog.setCreatedDate(LocalDateTime.now());

//								Process records line by line
								BufferedReader br = new BufferedReader(filereader);
								String line = null;
								String HEADER = br.readLine();
								
								System.out.println("HEADER: " + HEADER);
								if (fileUtil.validateFileHeader(fileType, HEADER)) {
								long cat =	categoryRepository.count();
								if(fileType.equalsIgnoreCase(JobConstant.BULK_UPLOAD_QUIZZES) && cat==0) {
									fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
									fileResponse.setErrorCode(ExceptionConstant.NO_CATEGORY_FOUND_EC);
									fileResponse.setErrorDescription(ExceptionConstant.NO_CATEGORY_FOUND_ED);
									br.close();
									filereader.close();
									return fileResponse;
									
								} else {
									fileLogId = fileLogRepository.save(fileLog).getId();	
									while ((line = br.readLine()) != null) {
										fileResponse = processFile(line, finalFileName, fileType, fileLogId, userName);
									}
								 }
								}
								br.close();
								filereader.close();

							} catch (NullPointerException ex) {
								LOGGER.error(ExceptionConstant.JOB_NOT_FOUND_ED);
								System.out.println(ExceptionConstant.JOB_NOT_FOUND_ED);
								fileResponse.setFileName(multipartFile.getOriginalFilename());
								fileResponse.setJobName(jobName);
								fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
								fileResponse.setErrorCode(ExceptionConstant.JOB_NOT_FOUND_EC);
								fileResponse.setErrorDescription(ExceptionConstant.JOB_NOT_FOUND_ED);
								fileResponse.setUsername(userName);
								return fileResponse;
							} catch (FileUploadException ex) {
								LOGGER.error(ex.getMessage());
								fileResponse.setFileName(multipartFile.getOriginalFilename());
								fileResponse.setJobName(jobName);
								fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
								fileResponse.setErrorCode(ex.getErrorCode());
								fileResponse.setErrorDescription(ex.getMessage());
								fileResponse.setUsername(userName);
								return fileResponse;
							} catch (Exception ex) {
								LOGGER.error(ExceptionConstant.REGULAR_ERROR_OCCURED_ED + " : " + ex.getMessage());
								fileResponse.setFileName(multipartFile.getOriginalFilename());
								fileResponse.setJobName(jobName);
								fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
								fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_ED);
								fileResponse.setErrorDescription(ex.getMessage());
								fileResponse.setUsername(userName);
								return fileResponse;
							}

							if (StatusConstant.STATUS_SUCCESS.equalsIgnoreCase(fileResponse.getStatus())) {
								fileResponse.setErrorCode(ExceptionConstant.FILE_SAVED_IN_DB_EC);
								fileResponse.setErrorDescription(ExceptionConstant.FILE_SAVED_IN_DB_ED+". File Log Id is: "+fileLogId);
								fileResponse.setStatus(fileRes.getStatus());
								fileResponse.setJobName(jobName);
								fileResponse.setFileName(finalFileName);
								fileResponse.setFilePath(outFile.toString());
								fileResponse.setUsername(userName);
								
								try {
									Files.move(inputFile.toPath(), outputFile.toPath(),
											StandardCopyOption.REPLACE_EXISTING);
									LOGGER.info("File moved from " + inputFile.toPath() + "to" + outputFile.toPath());
									System.out.println("File moved:\n Source " + inputFile.toPath() + "\nDestinatopn: " + outputFile.toPath());

								} catch (IOException e) {
									LOGGER.error("File not readable exception...", e.getMessage());
									fileResponse.setFileName(multipartFile.getOriginalFilename());
									fileResponse.setJobName(jobName);
									fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
									fileResponse.setErrorCode(ExceptionConstant.FILE_CANNOT_BE_MOVED_EC);
									fileResponse.setErrorDescription(e.getMessage());
									fileResponse.setUsername(userName);
								}

							} else {
								LOGGER.error(ExceptionConstant.FILE_NOT_SAVED_ED);
								fileResponse.setErrorCode(fileResponse.getErrorCode());
								fileResponse.setErrorDescription(fileResponse.getErrorDescription());
								fileResponse.setStatus(fileResponse.getStatus());
								fileResponse.setJobName(jobName);
								fileResponse.setUsername(userName);
								fileResponse.setFileName(multipartFile.getOriginalFilename());
//								throw new FileUploadException(ExceptionConstant.FILE_NOT_SAVED_EC,ExceptionConstant.FILE_NOT_SAVED_ED);
							}
							

						} catch (Exception e) {
							LOGGER.error(ExceptionConstant.REGULAR_ERROR_OCCURED_ED + " : " + e.getMessage());
							fileResponse.setFileName(multipartFile.getOriginalFilename());
							fileResponse.setJobName(jobName);
							fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
							fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
							fileResponse.setErrorDescription(e.getMessage());
							fileResponse.setUsername(userName);
							throw new FileUploadException(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
						}

					} else {
						LOGGER.error(ExceptionConstant.ERROR_WRITING_FILE_ED);
						fileResponse.setFileName(multipartFile.getOriginalFilename());
						fileResponse.setJobName(jobName);
						fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
						fileResponse.setErrorCode(ExceptionConstant.ERROR_WRITING_FILE_EC);
						fileResponse.setErrorDescription(ExceptionConstant.ERROR_WRITING_FILE_ED);
						fileResponse.setUsername(userName);
					}
				}
			}

		} catch (NullPointerException ex) {
			LOGGER.error(ExceptionConstant.JOB_NOT_FOUND_ED);
			System.out.println(ExceptionConstant.JOB_NOT_FOUND_ED);
			fileResponse.setFileName(multipartFile.getOriginalFilename());
			fileResponse.setJobName(jobName);
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			fileResponse.setErrorCode(ExceptionConstant.JOB_NOT_FOUND_EC);
			fileResponse.setErrorDescription(ExceptionConstant.JOB_NOT_FOUND_ED);
			fileResponse.setUsername(userName);
		} catch (FileUploadException ex) {
			LOGGER.error(ex.getMessage());
			fileResponse.setFileName(multipartFile.getOriginalFilename());
			fileResponse.setJobName(jobName);
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			fileResponse.setErrorCode(ex.getErrorCode());
			fileResponse.setErrorDescription(ex.getMessage());
			fileResponse.setUsername(userName);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstant.REGULAR_ERROR_OCCURED_ED + " : " + ex.getMessage());
			fileResponse.setFileName(multipartFile.getOriginalFilename());
			fileResponse.setJobName(jobName);
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_ED);
			fileResponse.setErrorDescription(ex.getMessage());
			fileResponse.setUsername(userName);
		}

		return fileResponse;
	}

	private FileResponse processFile(String record, String finalFileName, String fileType, Long fileLogId, String userName)
			throws FileUploadException {
		FileResponse fileResponse = new FileResponse();
		try {
			List<String> list = Arrays.asList(record.split(","));
			System.out.println("Line number: " + list.get(0));
			switch (fileType) {
			case JobConstant.BULK_UPLOAD_CATEGORIES:

				Category category = new Category();
				category.setTitle(list.get(1));
				category.setDescription(list.get(2));
				category.setCreatedBy(userName);
				category.setCreatedDate(LocalDateTime.now());

				categoryRepository.save(category);
				fileResponse = updateFileUplDtls(record, finalFileName, fileType, fileLogId, list.get(0), userName);
				break;

			case JobConstant.BULK_UPLOAD_QUIZZES:
				Category category1 = null;
				Category category2 = null;
				try {
					category1 = categoryRepository.findByTitle(list.get(6).trim());
					category2 = new Category();
					category2.setCid(category1.getCid());
				} catch (NullPointerException e) {
					e.getStackTrace();
					System.out.println(e.getMessage());
					fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
					fileResponse.setErrorCode(ExceptionConstant.NO_CATEGORY_FOUND_EC);
					fileResponse.setErrorDescription(ExceptionConstant.NO_CATEGORY_FOUND_ED);
					return fileResponse;
				}

				Quiz quiz = new Quiz();
				quiz.setTitle(list.get(1));
				quiz.setDescription(list.get(2));
				quiz.setMaxMarks(list.get(3));
				quiz.setNumberOfQuestions(list.get(4));
				quiz.setActive(Boolean.parseBoolean(list.get(5)));
				quiz.setCategory(category2);
				quiz.setCreatedDate(LocalDateTime.now());

				quizRepository.save(quiz);

				fileResponse = updateFileUplDtls(record, finalFileName, fileType, fileLogId, list.get(0), userName);
				break;
			}

			
		} catch (Exception ex) {
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			throw new FileUploadException(ExceptionConstant.FILE_UPLOAD_EX_ED);

		}

		return fileResponse;

	}

	private FileResponse updateFileUplDtls(String record, String finalFileName, String fileType, Long fileLogId,
			String sno, String userName) {

		FileResponse fileResponse = new FileResponse();
		FileLog fileLog = fileLogRepository.findById(fileLogId).get();

		FileLog fileLogDetails = new FileLog();
		fileLogDetails.setId(fileLog.getId());

		FileUploadDetails fileUploadDetails = new FileUploadDetails();
		fileUploadDetails.setFileType(fileType);
		fileUploadDetails.setFileName(finalFileName);
		fileUploadDetails.setFileLogId(fileLogDetails);
		fileUploadDetails.setsNo(sno);
		fileUploadDetails.setRecord(record);
		fileUploadDetails.setStatus(StatusConstant.STATUS_SUCCESS);
		fileUploadDetails.setCreatedBy(userName);
		fileUploadDetails.setCreatedDate(LocalDateTime.now());
		fileUploadDetailsRepository.save(fileUploadDetails);

		fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
		
		updateFileLogCounter(fileLogId, fileResponse);

		return fileResponse;
	}

	private FileResponse updateFileLogCounter(Long fileLogId, FileResponse fileResponse) {
		FileLog fileLog = fileLogRepository.findById(fileLogId).get();
		Long successCount = fileLog.getSuccessCount();
		Long failureCount = fileLog.getFailureCount();
		Long totalCount = fileLog.getTotalCount();
		
		FileResponse fileRes = new FileResponse();
		
		if(fileResponse.getStatus().equalsIgnoreCase("SUCCESS"))
			fileLog.setSuccessCount(successCount+1);
		if(fileResponse.getStatus().equalsIgnoreCase("FAILURE"))
			fileLog.setFailureCount(failureCount+1);
		fileLog.setStatus(totalCount==successCount+failureCount+1?StatusConstant.STATUS_COMPLETED:StatusConstant.STATUS_INPROGRESS);
//		fileLog.setProcessedCount(successCount+failureCount);
//		fileLog.setTotalCount(successCount+failureCount);
		fileLogRepository.save(fileLog);
		
		fileRes.setStatus(StatusConstant.STATUS_SUCCESS);
		
		return fileRes;
	}
	
}
