package com.sinnguyen.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MailContentBuilder mailContentBuilder;
	
	@Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
	
	public void sendWelcomeMail(User user) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setSubject("BBMusic Registration");
			mimeMessageHelper.setFrom("hakleader@gmail.com");
			mimeMessageHelper.setTo(user.getEmail());

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("username", user.getUsername());
			model.put("code", user.getCode());

			String content = mailContentBuilder.buildWelcomeMail(model);
			mimeMessageHelper.setText(content, true);
			mailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
				
		}
	}

	@Override
	public void sendForgotMail(Forgot forgot) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setSubject("BBMusic Registration");
			mimeMessageHelper.setFrom("hakleader@gmail.com");
			mimeMessageHelper.setTo(forgot.getUser().getEmail());

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("username", forgot.getUser().getUsername());
			model.put("code", forgot.getCode());
			String content = mailContentBuilder.buildForgotMail(model);
			mimeMessageHelper.setText(content, true);
			mailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
				
		}
	}
	
//	public String getForgotContentFromTemplate(Map<String, Object> model) {
//		StringBuffer content = new StringBuffer();
//		try {
//			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/mailtemplate/forgot.vm", model));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return content.toString();
//	}

}
