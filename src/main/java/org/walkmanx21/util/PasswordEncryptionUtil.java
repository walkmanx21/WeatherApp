package org.walkmanx21.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptionUtil {

    public String hashPassword (String password) {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }
}
