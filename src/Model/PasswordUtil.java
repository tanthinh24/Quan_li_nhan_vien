package Model;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Hàm để mã hóa mật khẩu
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Hàm để kiểm tra mật khẩu
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
