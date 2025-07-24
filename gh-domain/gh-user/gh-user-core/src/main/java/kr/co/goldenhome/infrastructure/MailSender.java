package kr.co.goldenhome.infrastructure;


public interface MailSender {
    void send(String to, String subject, String content);
}
