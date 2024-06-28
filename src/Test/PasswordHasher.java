
package Test;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        String plainTextPassword = "123";
        String hashedPassword = hashPassword(plainTextPassword);
        System.out.println("Kết quả mã hóa: " + hashedPassword);
    }
}
