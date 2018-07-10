package com.sinnguyen.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
	
	static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
	public static String genPassword(String rawPassword) {
		return bCryptPasswordEncoder.encode(rawPassword);
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(genPassword("123"));
		System.out.println(System.currentTimeMillis());
//		System.out.println("image/jpeg".matches("image\\/?\\w+"));
	}
}
