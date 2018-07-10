package com.sinnguyen.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class MainUtility {
	public static Date StringtoDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	public static java.sql.Date convertDate(java.util.Date date) {
	    return new java.sql.Date(date.getTime());
	}
	public static String dateToStringFormat(Date date, String formatOutput) {
		String result = "";
		try {
			DateFormat df = new SimpleDateFormat(formatOutput);
			result = df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
              sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            
        }
        return null;
    }
	public static String getExt(String filename) {
		String extension = "";

		int i = filename.lastIndexOf('.');
		if (i > 0) {
		    return extension = filename.substring(i+1);
		}
        return null;
    }
	public static String saveFile(MultipartFile file) {
		String result = "";
		try {
			String rootPath = "";
			String extFile = getExt(file.getOriginalFilename());
			if(file.getContentType().matches("image\\/?\\w+")) {
				rootPath = CommonConstant.IMAGE_LOCATION;
			}
			String filename = System.currentTimeMillis() +"."+ extFile;
			Path pathFile = Paths.get(rootPath+filename);
			result = filename;
			Files.copy(file.getInputStream(), pathFile);
		} catch (Exception ie) {
			return null;
		}
		return result;
	}
}
