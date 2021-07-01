package com.exam.dto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.exam.model.Category;
import com.exam.model.DeveloperTutorial;
import com.exam.model.Quiz;
import com.exam.repository.CategoryRepository;

public class CSVHelper {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };

	public static boolean hasCSVFormat(MultipartFile file) {
		System.out.println(file.getContentType());
		if (TYPE.equals(file.getContentType()) || file.getContentType().equals("application/vnd.ms-excel")) {
			return true;
		}

		return false;
	}

	public static List<Category> csvToCategory(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<Category> categoryList = new ArrayList<>();
			System.out.println("Header Names=========> " + csvParser.getHeaderNames());
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				Category category = new Category(
						// Long.parseLong(csvRecord.get("Id")),
						csvRecord.get("Title"), csvRecord.get("Description"), LocalDateTime.now(), "sunilkmr5775"
				// Boolean.parseBoolean(csvRecord.get("Published"))
				);

				categoryList.add(category);
			}

			return categoryList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static List<Quiz> csvToQuiz(InputStream is, CategoryRepository categoryRepository2) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<Quiz> quizList = new ArrayList<>();
			System.out.println("Header Names=========> " + csvParser.getHeaderNames());
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			Category category = null;
			Category cat2  = null;
			for (CSVRecord csvRecord : csvRecords) {
				try {
					category = categoryRepository2.findByTitle("Core Java".trim());
					cat2 = new Category();
					cat2.setCid(category.getCid());
				} catch(NullPointerException e) {
					e.getStackTrace();
					System.out.println(e.getMessage());
				}
				Quiz quiz = new Quiz(
						csvRecord.get("Title"), 
						csvRecord.get("Description"), 
						csvRecord.get("Max_Marks"),
						csvRecord.get("No_Of_Questions"),
						Boolean.parseBoolean(csvRecord.get("Published")),
						cat2,
						LocalDateTime.now()
				);
				quizList.add(quiz);
			}

			return quizList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static ByteArrayInputStream tutorialsToCSV(List<DeveloperTutorial> developerTutorialList) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			for (DeveloperTutorial developerTutorial : developerTutorialList) {
				List<String> data = Arrays.asList(String.valueOf(developerTutorial.getId()),
						developerTutorial.getTitle(), developerTutorial.getDescription(),
						String.valueOf(developerTutorial.isPublished()));

				csvPrinter.printRecord(data);
			}

			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		}
	}
}
