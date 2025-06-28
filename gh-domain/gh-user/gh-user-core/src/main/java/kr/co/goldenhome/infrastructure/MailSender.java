package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.dto.Email;

public interface MailSender {
    void send(Email email);
}
