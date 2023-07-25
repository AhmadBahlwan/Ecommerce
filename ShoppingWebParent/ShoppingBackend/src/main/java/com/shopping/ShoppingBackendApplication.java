package com.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Arrays;

@SpringBootApplication
@EntityScan({"com.shopping.library.entity", "com.shopping.admin.user", "com.shopping.admin.category"})
public class ShoppingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingBackendApplication.class, args);
		//System.out.println(checkAllSameFirstChar(new String[]{"dog","domani"}));
	}

	public static String longestCommonPrefix(String[] strs) {
		if (strs.length == 1) return strs[0];

		int len = getSmallestStringLength(strs);
		if (len == 0) return "";

		int index = 0;

		for (int i=strs.length-1; i>=0 ; i++){
			int finalI = i;
			if (Arrays.stream(strs).allMatch(str-> strs[0].substring(0,finalI).equals(str)))
				return strs[0].substring(i);
		}

		return "";

	}

	public static int getSmallestStringLength(String[] strings) {
		String smallestString = strings[0];

		for (int i = 1; i < strings.length; i++) {
			if (strings[i].compareTo(smallestString) < 0) {
				smallestString = strings[i];
			}
		}

		return smallestString.length();
	}

	public static String checkAllSameFirstChar(String[] strings) {
		char firstChar = strings[0].charAt(0);

		int i;
		int index =   0;
		for (i = 1; i < strings.length; i++) {
			if (strings[i].charAt(index) != strings[0].charAt(index)) {
				break;
			}
			index++;
		}
		return strings[0].substring(0,index);
	}

	private boolean hasSameChar(String [] strList, char letter, int position) {
		return true;
	}

}
