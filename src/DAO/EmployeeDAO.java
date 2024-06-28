package DAO;

import javax.swing.*;
import java.sql.SQLException;

public interface EmployeeDAO {
    int getMax() throws SQLException;
    void insert(int id, String sname, String date, String gender, String email,
                String phone, String father, String mother, String address, String salary, String imagePath) throws SQLException;
    boolean isEmailExist(String email) throws SQLException;
    boolean isPhoneExist(String phone) throws SQLException;
    boolean isIdExist(int id) throws SQLException;
    void getEmployeeValue(JTable table, String searchValue) throws SQLException;
    void update(int id, String sname, String date, String gender, String email,
                String phone, String father, String mother, String address, String salary, String imagePath) throws SQLException;
    void delete(int id) throws SQLException;
    void getEmployeeForLoggedInUser(JTable table, String loggedInUsername) throws SQLException;
    boolean checkLogin(String username, String plainTextPassword) throws SQLException; // Thêm phương thức kiểm tra đăng nhập
}
