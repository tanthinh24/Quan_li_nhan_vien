package Server; // Khai báo package là Server

import Controller.EmployeeController; // Import lớp EmployeeController từ gói Controller
import java.io.*; // Import tất cả các lớp trong gói java.io
import java.net.Socket; // Import lớp Socket trong gói java.net

public class ClientHandler implements Runnable {
    private Socket socket; // Khai báo một đối tượng Socket
    private EmployeeController employeeController; // Khai báo một đối tượng EmployeeController

    // Constructor của lớp ClientHandler
    public ClientHandler(Socket socket) {
        this.socket = socket; // Gán đối tượng Socket được truyền vào cho biến socket
        this.employeeController = new EmployeeController(); // Khởi tạo đối tượng EmployeeController
    }

    // Phương thức run của Runnable interface
    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // Khởi tạo đối tượng ObjectInputStream từ luồng đầu vào của socket
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) { // Khởi tạo đối tượng ObjectOutputStream từ luồng đầu ra của socket

            String command = (String) in.readObject(); // Đọc lệnh từ client

            // Kiểm tra nếu lệnh là "login"
            if ("login".equals(command)) {
                String username = (String) in.readObject(); // Đọc tên người dùng từ client
                String password = (String) in.readObject(); // Đọc mật khẩu từ client
                boolean loginSuccess = employeeController.checkLogin(username, password); // Kiểm tra đăng nhập bằng EmployeeController
                out.writeObject(loginSuccess); // Gửi kết quả đăng nhập về cho client
            }

            out.flush(); // Đảm bảo rằng tất cả dữ liệu đã được gửi đi

        } catch (IOException | ClassNotFoundException e) { // Bắt và xử lý ngoại lệ IOException và ClassNotFoundException
            e.printStackTrace(); // In ra lỗi nếu có ngoại lệ
        }
    }
}
