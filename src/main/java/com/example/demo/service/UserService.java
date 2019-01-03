package com.example.demo.service;


import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private JavaMailSender emailSender;

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

    public void sendSimpleMessage(String emailTo) {
        String msgText = "<html>As requested by you, we have reset your password to access Arch Insurance Portal. Please " +
                " find below your temporary password to access the portal. Please change you password upon signing in.<br/><br/>" +
                " Temporary password - %s <br/><br/>" +
                "Regards,<br/>Arch Insurance Portal Support Team</html>";
        String message = String.format(msgText, RandomStringUtils.randomAlphanumeric(8));
        emailSender.send(mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject("Your password for Arch Insurance Portal is reset");
            messageHelper.setText(message, true);
        });

    }


}
