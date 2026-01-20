package kr.co.goldenhome.authentication.implement;

import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VerificationManagerFactory {

    private final Map<VerificationType, VerificationManager> managerMap;

    public VerificationManagerFactory(List<VerificationManager> managers) {
        this.managerMap = managers.stream()
                .collect(Collectors.toMap(VerificationManager::getVerificationType, m -> m));
    }

    public String requestVerification(String contact, String type) {
        VerificationManager manager = getManager(type);
        String verificationCode = manager.create(contact);
        manager.send(contact, verificationCode);
        return verificationCode;
    }

    public void confirm(String type, String contact, String verificationCode) {
        VerificationManager manager = getManager(type);
        manager.confirm(contact, verificationCode);
    }

    private VerificationManager getManager(String type) {
        VerificationType verificationType = VerificationType.valueOf(type);
        VerificationManager manager = managerMap.get(verificationType);
        if (manager == null) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_TYPE, "VerificationManagerFactory.getManager");
        }
        return manager;
    }
}
