package com.exam;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.exam.repository.FileTypeRuleAttributeRepository;

public class Test {
	@Autowired
	static FileTypeRuleAttributeRepository fileTypeRuleAttributeRepository;
	public static void main(String[] args) {

		String fileHeader1 = "﻿SNO,TITLE,DESCRIPTION,MAX_MARKS,NO_OF_QUESTIONS,PUBLISHED,CATEGORY";
		String fileHeader2 = "﻿SNO,TITLE,DESCRIPTION,MAX_MARKS,NO_OF_QUESTIONS,PUBLISHED,CATEGORY";
//		boolean flag = compareStrings(fileHeader1, fileHeader2);

//		System.out.println("Checking Header Validation : " +flag);
		
		String[] v1 = fileHeader1.split(",");
		String[] v2 = fileHeader2.split(",");
		Arrays.sort(v1);
		Arrays.sort(v2);
		System.out.println(Arrays.equals(v1, v2));

		
	}
	 static boolean compareStrings(String s1, String s2) {
		 boolean f = false;
			if (s1.equalsIgnoreCase(s2)) {
				f= true;
			}else {
				f=false;
			}
			return f;
	}

}
