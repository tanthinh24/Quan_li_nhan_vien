package Model;

import DAO.EmployeeDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImpl implements EmployeeDAO {

    private Connection con;

    public EmployeeDAOImpl(Connection con) {
        this.con = con;
    }

    private PreparedStatement prepareStatement(String sql, Object... parameters) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }

    @Override
    public int getMax() throws SQLException {
        int id = 0;
        String sql = "SELECT COALESCE(MAX(id), 0) FROM employee";
        try (PreparedStatement ps = prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id + 1;
    }
    
    public void insert(int id, String sname, String date, String gender, String email,
                       String phone, String father, String mother, String address, String salary, String imagePath) throws SQLException {
        String employeeSql = "INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String adminSql = "INSERT INTO admin (id, username, password) VALUES (?, ?, ?)";

        // Chèn thông tin nhân viên vào bảng employee
        try (PreparedStatement ps = prepareStatement(employeeSql, id, sname, date, gender, email, phone, father, mother, address, salary, imagePath)) {
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New Employee added successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add new employee.");
            }
        }

        // Mã hóa mật khẩu và chèn thông tin admin vào bảng admin
        String hashedPassword = BCrypt.hashpw(phone, BCrypt.gensalt());
        try (PreparedStatement ps = prepareStatement(adminSql, id + 1, sname, hashedPassword)) {
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New Admin added successfully. Username: " + sname + ". Password: " + phone);
                System.out.println("New Admin added for Employee: " + sname);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add new admin.");
            }
        }
    }

    @Override
    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT * FROM employee WHERE email = ?";
        try (PreparedStatement ps = prepareStatement(sql, email); ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }

    @Override
    public boolean isPhoneExist(String phone) throws SQLException {
        String sql = "SELECT * FROM employee WHERE phone = ?";
        try (PreparedStatement ps = prepareStatement(sql, phone); ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }

    @Override
    public boolean isIdExist(int id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try (PreparedStatement ps = prepareStatement(sql, id); ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }

    @Override
    public void getEmployeeValue(JTable table, String searchValue) throws SQLException {
        String sql = "SELECT * FROM employee WHERE concat(id, name, email, phone) LIKE ? ORDER BY id DESC";
        try (PreparedStatement ps = prepareStatement(sql, "%" + searchValue + "%"); ResultSet rs = ps.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[11];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                row[9] = rs.getString(10);
                row[10] = rs.getString(11);
                model.addRow(row);
            }
        }
    }

    @Override
    public void update(int id, String sname, String date, String gender, String email,
                       String phone, String father, String mother, String address, String salary, String imagePath) throws SQLException {
        String sql = "UPDATE employee SET name = ?, date_of_birth = ?, gender = ?, email = ?, phone = ?, "
                + "father_name = ?, mother_name = ?, address = ?, salary = ?, image_path = ? WHERE id = ?";
        try (PreparedStatement ps = prepareStatement(sql, sname, date, gender, email, phone, father, mother, address, salary, imagePath, id)) {
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Employee data updated successfully ");
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        int yesOrNo = JOptionPane.showConfirmDialog(null, "Course and score records will also be deleted", "Employee delete", JOptionPane.OK_CANCEL_OPTION, 0);
        if (yesOrNo == JOptionPane.OK_OPTION) {
            String deleteEmployeeSql = "DELETE FROM employee WHERE id = ?";
            String deleteAdminSql = "DELETE FROM admin WHERE id = ?";

            try (PreparedStatement ps = prepareStatement(deleteEmployeeSql, id)) {
                if (ps.executeUpdate() > 0) {
                    try (PreparedStatement psAdmin = prepareStatement(deleteAdminSql, id + 1)) {
                        psAdmin.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "Employee and corresponding Admin records deleted successfully");
                }
            }
        }
    }

    @Override
    public void getEmployeeForLoggedInUser(JTable table, String loggedInUsername) throws SQLException {
        String sql = "SELECT * FROM employee WHERE name = ?";
        try (PreparedStatement ps = prepareStatement(sql, loggedInUsername); ResultSet rs = ps.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[11];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                row[9] = rs.getString(10);
                row[10] = rs.getString(11);
                model.addRow(row);
            }
        }
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        String query = "SELECT password FROM admin WHERE username = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, hashedPassword); // So sánh mật khẩu
            } else {
                return false; // Không tìm thấy username
            }
        }
    }
}
