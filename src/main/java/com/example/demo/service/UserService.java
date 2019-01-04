package com.example.demo.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import com.example.demo.Mail;
import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    @Qualifier("getFreeMarkerConfiguration")
    private Configuration freemarkerConfig;
    
    @Autowired
    private EntityManagerFactory factory;

    public void addUsers(UserEntity vo) {
        usersRepository.save(vo);
    }

    public void updateUser(UserEntity vo) {
        usersRepository.save(vo);
    }

    public List<UserEntity> listUsers() {
        List<UserEntity> users = new ArrayList<>();
        try {
            AuditReader audit = AuditReaderFactory.get(factory.createEntityManager());
            testAuditReader(audit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    private void testAuditReader(AuditReader audit) {
        List<UserEntity> userEntities = audit.createQuery()
                .forRevisionsOfEntity(UserEntity.class, true, true)
                .add(AuditEntity.id().eq(3L)) // UserId
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(5)
                .getResultList();
        System.out.println(userEntities);
    }

        
    public void sendSimpleMessage(String emailTo, String userName) throws IOException, TemplateException {
        String msgText = "";
        		/*"<html>As requested by you, we have reset your password to access Arch Insurance Portal. Please " +
                " find below your temporary password to access the portal. Please change you password upon signing in.<br/><br/>" +
                " Temporary password - %s <br/><br/>" +
                "Regards,<br/>Arch Insurance Portal Support Team</html>"; */
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
        //msgText = String.format(msgText, RandomStringUtils.randomAlphanumeric(8));
    	Mail mail = new Mail();
        mail.setFrom("no-reply@archinsurance.com");
        String toEmail = mail.getFrom();
        Map<String, String> model = new HashMap<String, String>();
        model.put("name", userName);
        model.put("random", RandomStringUtils.randomAlphanumeric(8));
        mail.setModel(model);
        
        Template t = freemarkerConfig.getTemplate("emailtemplate.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        emailSender.send(mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(toEmail);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject("Temporary password for Arch Insurance Portal - reset required");
            messageHelper.setText(html, true);
        });

    }




}
