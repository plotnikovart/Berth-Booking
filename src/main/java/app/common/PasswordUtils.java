package app.common;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.security.SecureRandom;
import java.util.Arrays;

@NoArgsConstructor
public class PasswordUtils {

    public static Pair<byte[], byte[]> createHashAndSalt(String password) {
        var salt = new SecureRandom().generateSeed(16);
        var hash = passwordHash(password, salt);

        return Pair.of(hash, salt);
    }


    public static boolean checkPassword(String checkedPassword, byte[] passwordHash, byte[] salt) {
        var checkedHash = passwordHash(checkedPassword, salt);
        return Arrays.equals(checkedHash, passwordHash);
    }


    private static byte[] passwordHash(String password, byte[] salt) {
        return BCrypt.withDefaults().hash(6, salt, password.getBytes());
    }
}
