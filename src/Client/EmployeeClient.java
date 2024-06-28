package Client; // Khai báo package là Client

import java.io.*; // Import tất cả các lớp trong gói java.io
import java.net.Socket; // Import lớp Socket trong gói java.net

public class EmployeeClient {
    private Socket socket; // Khai báo một đối tượng Socket
    private ObjectInputStream in; // Khai báo một đối tượng ObjectInputStream
    private ObjectOutputStream out; // Khai báo một đối tượng ObjectOutputStream

    // Constructor của lớp EmployeeClient
    public EmployeeClient(String host, int port) throws IOException {
        socket = new Socket(host, port); // Khởi tạo đối tượng Socket với host và port được truyền vào
        out = new ObjectOutputStream(socket.getOutputStream()); // Khởi tạo đối tượng ObjectOutputStream từ luồng đầu ra của socket
        in = new ObjectInputStream(socket.getInputStream()); // Khởi tạo đối tượng ObjectInputStream từ luồng đầu vào của socket
    }

    // Phương thức đăng nhập với tham số là tên người dùng và mật khẩu
    public boolean login(String username, String password) throws IOException, ClassNotFoundException {
        out.writeObject("login"); // Gửi chuỗi "login" đến server
        out.writeObject(username); // Gửi tên người dùng đến server
        out.writeObject(password); // Gửi mật khẩu đến server
        out.flush(); // Đảm bảo rằng tất cả dữ liệu đã được gửi đi

        boolean loginSuccess = (boolean) in.readObject(); // Đọc kết quả đăng nhập từ server

        in.close(); // Đóng luồng đầu vào
        out.close(); // Đóng luồng đầu ra
        socket.close(); // Đóng socket

        return loginSuccess; // Trả về kết quả đăng nhập
    }

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        try {
            EmployeeClient client = new EmployeeClient("localhost", 12345); // Khởi tạo đối tượng EmployeeClient với host là "localhost" và port là 12345
            boolean loginSuccess = client.login("admin", "123"); // Gọi phương thức login với tên người dùng "admin" và mật khẩu "123"
            System.out.println("Login successful: " + loginSuccess); // In ra kết quả đăng nhập
        } catch (IOException | ClassNotFoundException e) { // Bắt và xử lý ngoại lệ IOException và ClassNotFoundException
            e.printStackTrace(); // In ra lỗi nếu có ngoại lệ
        }
    }
}
