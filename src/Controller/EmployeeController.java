package Controller;

import Model.EmployeeDAOImpl;
import DAO.EmployeeDAO;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeController {

    private EmployeeDAO employeeDAO;
    private Connection connection;

    public EmployeeController() {
        connection = MyConnection.getConnection();
        employeeDAO = new EmployeeDAOImpl(connection);
    }

    public int getMax() {
        try {
            return employeeDAO.getMax();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1; // Trả về giá trị mặc định nếu có lỗi
        }
    }

    public boolean insert(int id, String sname, String date, String gender, String email, String phone,
                          String father, String mother, String address, String salary, String imagePath) {
        try {
            employeeDAO.insert(id, sname, date, gender, email, phone, father, mother, address, salary, imagePath);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int id, String sname, String date, String gender, String email, String phone,
                          String father, String mother, String address, String salary, String imagePath) {
        try {
            employeeDAO.update(id, sname, date, gender, email, phone, father, mother, address, salary, imagePath);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            employeeDAO.delete(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExist(String email) {
        try {
            return employeeDAO.isEmailExist(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isPhoneExist(String phone) {
        try {
            return employeeDAO.isPhoneExist(phone);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isIdExist(int id) {
        try {
            return employeeDAO.isIdExist(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getEmployeeValue(JTable table, String searchValue) {
        try {
            employeeDAO.getEmployeeValue(table, searchValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getEmployeeForLoggedInUser(JTable table, String loggedInUsername) {
        try {
            employeeDAO.getEmployeeForLoggedInUser(table, loggedInUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String username, String password) {
        try {
            return employeeDAO.checkLogin(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
