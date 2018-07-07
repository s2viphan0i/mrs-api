package com.sinnguyen.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {
 
    private TemplateEngine templateEngine;
 
    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
 
    public String buildWelcomeMail(Map<String, Object> model) {
        Context context = new Context();
        context.setVariable("username", model.get("username"));
        context.setVariable("code", model.get("code"));
        return templateEngine.process("welcomeMail", context);
    }
    public String buildForgotMail(Map<String, Object> model) {
        Context context = new Context();
        context.setVariable("username", model.get("username"));
        context.setVariable("code", model.get("code"));
        return templateEngine.process("forgotMail", context);
    }
 
}
