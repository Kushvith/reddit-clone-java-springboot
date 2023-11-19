package com.reddit.demo.service;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reddit.demo.DTO.NotificationEmail;
import com.reddit.demo.exception.SpringRedditExpection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Service
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private  final MailContentBuilder mailContentBuilder;
   @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage ->{
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"UTF-8");
        messageHelper.setFrom("kushvithchinna900@gmail.com");
        messageHelper.setTo(notificationEmail.getReciepent());
        messageHelper.setSubject(notificationEmail.getSubject());
        messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()),true);
         };
         try{
            mailSender.send(messagePreparator);
            log.info("email sent successfully!");
         }
         catch(MailException e){
            throw new SpringRedditExpection("error occured"+e);
         }
      }
}
