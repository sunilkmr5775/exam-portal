package com.exam.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.exam.constant.ExceptionConstant;
import com.exam.constant.StatusConstant;
import com.exam.dto.FileResponse;
import com.exam.exception.FileUploadException;
import com.exam.model.FileTypeRuleAttribute;
import com.exam.repository.FileTypeRuleAttributeRepository;

@Component
public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	@Autowired
	FileTypeRuleAttributeRepository fileTypeRuleAttributeRepository;

	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public static FileResponse wirteToInputFile(MultipartFile multipartFile, String fileInPath, String fileName) {
		FileResponse fileResponse = new FileResponse();

		try {
			File tmpFile = new File(fileName);
			LOGGER.info("tmpFile: " + tmpFile.getName());

			Path filePath = Paths.get(fileInPath, tmpFile.getName());
			LOGGER.info("filePath: " + filePath);

			fileResponse.setFileName(fileName);
			try {
				Files.write(filePath, multipartFile.getBytes());
				fileResponse.setStatus(StatusConstant.STATUS_SUCCESS);
				fileResponse.setFilePath(filePath.toString());

			} catch (IOException e) {
				LOGGER.info("Exception occured => " + e.getMessage());
				fileResponse.setErrorCode(ExceptionConstant.IO_ERROR_OCCURED_EC);
				fileResponse.setErrorCode(e.getMessage());
				fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
			}
		} catch (Exception e) {
			LOGGER.info("Exception occured: " + e.getMessage());
			fileResponse.setErrorCode(ExceptionConstant.REGULAR_ERROR_OCCURED_EC);
			fileResponse.setErrorCode(e.getMessage());
			fileResponse.setStatus(StatusConstant.STATUS_FAILURE);
		}
		return fileResponse;
	}

	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(0, fileName.indexOf("."));
		else
			return "";
	}
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.indexOf(".")+1);
		else
			return "";
	}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}

	// Function to validate image file extension .
	public static boolean checkImageFileExt(String fileName) {
		// Regex to check valid image file extension.
		String regex = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";
		Pattern p = Pattern.compile(regex);
		if (fileName == null) {
			return false;
		}
		Matcher m = p.matcher(fileName);
		return m.matches();
	}

	public static long getFileSize(String fileName) {
		Path path = Paths.get(fileName);
		long bytes = 0L;
		try {

			// size of a file (in bytes)
			bytes = Files.size(path);
			System.out.println(String.format("%,d bytes", bytes));
			System.out.println(String.format("%,d kilobytes", bytes / 1024));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes / 1024;
	}

	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}

	public boolean validateFileRequest(MultipartFile file, String fileType)
			throws FileUploadException, IOException {
		//String fileInPath = jobMasterDetails.getFileInPath();
	//	LOGGER.info("fileInpPath:" + fileInPath);
		List<FileTypeRuleAttribute> fileTypeRuleAttributeList = fileTypeRuleAttributeRepository
				.findFileTypeRuleAttributeListByfileTypeAndStatus(fileType, StatusConstant.STATUS_ACTIVE);

		long fileSize = file.getSize();

		LOGGER.info("fileSize:" + fileSize);
		boolean flag = true;

		LOGGER.info("Checking Validation ,Validation Found " + fileTypeRuleAttributeList);
		for(FileTypeRuleAttribute ruleAttribute:fileTypeRuleAttributeList) {
			if (ruleAttribute.getRuleType().equalsIgnoreCase(StatusConstant.SIZE)) {
				if (!(fileSize > 0)) {
					throw new FileUploadException(ExceptionConstant.EMPTY_FILE_EC, ExceptionConstant.EMPTY_FILE_ED);
				}

				if (!(fileSize <= Long.parseLong(ruleAttribute.getRulevalue()))) {
					throw new FileUploadException(ExceptionConstant.IMAGE_SIZE_EXCEEDS_EC,
							ExceptionConstant.IMAGE_SIZE_EXCEEDS_ED);
				}

				LOGGER.info("SIZE: true");

			}			
		}


		LOGGER.info("Validated");
		return flag;
	}
}
