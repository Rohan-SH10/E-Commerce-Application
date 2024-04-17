package com.retail.e_com.mailservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;
    public void sendMailMessage(MessageModel messageModel) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setTo(messageModel.getTo());
        helper.setSubject(messageModel.getSubject());
        helper.setSentDate(new Date());
        helper.setText(messageModel.getText(),true);
        javaMailSender.send(mimeMessage);
    }
}
