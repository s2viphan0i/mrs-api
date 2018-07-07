package com.sinnguyen.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinnguyen.util.CommonConstant;

@Controller
@RequestMapping("/resource")
public class ResourceDownloaderController {
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void login(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
		File file = new File(CommonConstant.IMAGE_LOCATION + name);
		response.setContentType("image/*");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}

		out.flush();
	}
}
