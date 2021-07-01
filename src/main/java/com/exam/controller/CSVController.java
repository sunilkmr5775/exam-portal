package com.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.exam.dto.CSVHelper;
import com.exam.dto.ResponseMessage;
import com.exam.model.DeveloperTutorial;
import com.exam.service.CSVService;

//import springfox.documentation.service.ResponseMessage;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

	@Autowired
	CSVService fileService;

	@PostMapping("/uploadCategories")
	public ResponseEntity<ResponseMessage> uploadCategories(@RequestParam("file") MultipartFile file) {
		String message = "";

		try {
			if (CSVHelper.hasCSVFormat(file)) {
				fileService.saveToCategory(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();

				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/csv/download/")
						.path(file.getOriginalFilename()).toUriString();

				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fileDownloadUri));
			}
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, ""));
		}
//    }

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message, ""));
	}

	
	@PostMapping("/uploadQuizzes")
	public ResponseEntity<ResponseMessage> uploadQuizzes(@RequestParam("file") MultipartFile file) {
		String message = "";

		try {
			if (CSVHelper.hasCSVFormat(file)) {
				fileService.saveToQuiz(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();

				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/csv/download/")
						.path(file.getOriginalFilename()).toUriString();

				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fileDownloadUri));
			}
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, ""));
		}
//    }
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message, ""));
	}
	
//	@GetMapping("/tutorials")
	public ResponseEntity<List<DeveloperTutorial>> getAllTutorials() {
		try {
			List<DeveloperTutorial> tutorials = fileService.getAllTutorials();

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		InputStreamResource file = new InputStreamResource(fileService.load());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}
}
