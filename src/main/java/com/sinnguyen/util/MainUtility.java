package com.sinnguyen.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class MainUtility {
	public static Date stringtoDate(String date, String format) {
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
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
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
			return extension = filename.substring(i + 1);
		}
		return null;
	}

	public static String saveFile(MultipartFile file) {
		String result = "";
		try {
			String rootPath = "";
			String extFile = getExt(file.getOriginalFilename());
			if (file.getContentType().matches("image\\/?\\w+")) {
				rootPath = CommonConstant.IMAGE_LOCATION;
			} else if (file.getContentType().matches("audio\\/?\\w+")) {
				rootPath = CommonConstant.AUDIO_LOCATION;
			}
			String filename = System.currentTimeMillis() + "." + extFile;
			Path pathFile = Paths.get(rootPath + filename);
			result = filename;
			Files.copy(file.getInputStream(), pathFile);
		} catch (Exception ie) {
			return null;
		}
		return result;
	}

	public static String saveSquareImage(MultipartFile file) {
		String result = "";
		try {
			BufferedImage image = cropImageSquare(file.getBytes());
			String extFile = getExt(file.getOriginalFilename());
			String rootPath = CommonConstant.IMAGE_LOCATION;
			String filename = System.currentTimeMillis() + "." + extFile;
			File outputfile = new File(rootPath + filename);
			result = filename;
			ImageIO.write(image, extFile, outputfile);
		} catch (Exception ie) {
			return null;
		}
		return result;
	}

	private static BufferedImage cropImageSquare(byte[] image) throws IOException {
		// Get a BufferedImage object from a byte array
		InputStream in = new ByteArrayInputStream(image);
		BufferedImage originalImage = ImageIO.read(in);

		// Get image dimensions
		int height = originalImage.getHeight();
		int width = originalImage.getWidth();

		// The image is already a square
		if (height == width) {
			return originalImage;
		}

		// Compute the size of the square
		int squareSize = (height > width ? width : height);

		// Coordinates of the image's middle
		int xc = width / 2;
		int yc = height / 2;

		// Crop
		BufferedImage croppedImage = originalImage.getSubimage(xc - (squareSize / 2), // x coordinate of the upper-left
																						// corner
				yc - (squareSize / 2), // y coordinate of the upper-left corner
				squareSize, // widht
				squareSize // height
		);

		return croppedImage;
	}
}
