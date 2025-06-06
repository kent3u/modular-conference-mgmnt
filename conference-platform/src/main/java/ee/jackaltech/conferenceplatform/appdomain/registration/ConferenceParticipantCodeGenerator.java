package ee.jackaltech.conferenceplatform.appdomain.registration;

import java.security.SecureRandom;

public class ConferenceParticipantCodeGenerator {
    private static final String BASE62_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCode() {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = RANDOM.nextInt(BASE62_ALPHABET.length());
            code.append(BASE62_ALPHABET.charAt(index));
        }
        return code.toString();
    }
}
