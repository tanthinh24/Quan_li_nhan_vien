package Server; // Khai báo package là Server

import javax.swing.*; // Import tất cả các lớp trong gói javax.swing
import java.awt.*; // Import tất cả các lớp trong gói java.awt
import java.awt.event.ActionEvent; // Import lớp ActionEvent từ gói java.awt.event
import java.awt.event.ActionListener; // Import lớp ActionListener từ gói java.awt.event
import java.io.IOException; // Import lớp IOException từ gói java.io
import java.net.ServerSocket; // Import lớp ServerSocket từ gói java.net
import java.net.Socket; // Import lớp Socket từ gói java.net
import java.util.concurrent.ExecutorService; // Import lớp ExecutorService từ gói java.util.concurrent
import java.util.concurrent.Executors; // Import lớp Executors từ gói java.util.concurrent

public class EmployeeServer extends JFrame {
    private JButton startButton; // Khai báo nút bắt đầu server
    private JButton stopButton; // Khai báo nút dừng server
    private JTextArea logArea; // Khai báo khu vực văn bản để ghi log
    private ServerSocket serverSocket; // Khai báo đối tượng ServerSocket
    private ExecutorService threadPool; // Khai báo đối tượng ExecutorService để quản lý luồng
    private Thread serverThread; // Khai báo đối tượng Thread cho server

    // Constructor của lớp EmployeeServer
    public EmployeeServer() {
        setTitle("Employee Server"); // Đặt tiêu đề cho cửa sổ
        setSize(500, 400); // Đặt kích thước cho cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đặt hành động mặc định khi đóng cửa sổ là thoát chương trình
        setLocationRelativeTo(null); // Đặt vị trí của cửa sổ ở giữa màn hình

        initUI(); // Gọi phương thức khởi tạo giao diện người dùng
    }

    // Phương thức khởi tạo giao diện người dùng
    private void initUI() {
        JPanel panel = new JPanel(); // Khởi tạo đối tượng JPanel
        panel.setLayout(new BorderLayout()); // Đặt layout cho panel là BorderLayout

        logArea = new JTextArea(); // Khởi tạo đối tượng JTextArea để ghi log
        logArea.setEditable(false); // Đặt JTextArea không cho chỉnh sửa
        JScrollPane scrollPane = new JScrollPane(logArea); // Khởi tạo JScrollPane để chứa JTextArea
        panel.add(scrollPane, BorderLayout.CENTER); // Thêm JScrollPane vào panel ở vị trí trung tâm

        JPanel buttonPanel = new JPanel(); // Khởi tạo đối tượng JPanel cho các nút
        startButton = new JButton("Start Server"); // Khởi tạo nút bắt đầu server
        stopButton = new JButton("Stop Server"); // Khởi tạo nút dừng server
        stopButton.setEnabled(false); // Đặt nút dừng server ban đầu không hoạt động

        // Thêm ActionListener cho nút startButton
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer(); // Gọi phương thức startServer khi nhấn nút
            }
        });

        // Thêm ActionListener cho nút stopButton
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer(); // Gọi phương thức stopServer khi nhấn nút
            }
        });

        buttonPanel.add(startButton); // Thêm nút startButton vào buttonPanel
        buttonPanel.add(stopButton); // Thêm nút stopButton vào buttonPanel
        panel.add(buttonPanel, BorderLayout.SOUTH); // Thêm buttonPanel vào panel ở vị trí phía dưới

        add(panel); // Thêm panel vào JFrame
    }

    // Phương thức khởi động server
    private void startServer() {
        try {
            serverSocket = new ServerSocket(12345); // Khởi tạo ServerSocket lắng nghe ở cổng 12345
            threadPool = Executors.newFixedThreadPool(10); // Khởi tạo một thread pool với 10 luồng
            serverThread = new Thread(() -> { // Khởi tạo một luồng cho server
                while (!serverSocket.isClosed()) { // Vòng lặp để chấp nhận kết nối khi server socket chưa đóng
                    try {
                        Socket socket = serverSocket.accept(); // Chấp nhận kết nối từ client
                        log("Client connected: " + socket.getInetAddress()); // Ghi log khi có client kết nối
                        threadPool.execute(new ClientHandler(socket) { // Thực thi một ClientHandler mới trong thread pool
                            @Override
                            public void run() {
                                log("Handling client: " + socket.getInetAddress()); // Ghi log khi bắt đầu xử lý client
                                super.run(); // Gọi phương thức run của ClientHandler
                                log("Client disconnected: " + socket.getInetAddress()); // Ghi log khi client ngắt kết nối
                            }
                        });
                    } catch (IOException e) { // Bắt ngoại lệ IOException
                        if (!serverSocket.isClosed()) {
                            log("Server error: " + e.getMessage()); // Ghi log khi có lỗi
                        }
                    }
                }
            });
            serverThread.start(); // Bắt đầu luồng server
            log("Server started on port 12345"); // Ghi log khi server được khởi động

            startButton.setEnabled(false); // Vô hiệu hóa nút startButton
            stopButton.setEnabled(true); // Kích hoạt nút stopButton
        } catch (IOException e) { // Bắt ngoại lệ IOException
            log("Failed to start server: " + e.getMessage()); // Ghi log khi không thể khởi động server
        }
    }

    // Phương thức dừng server
    private void stopServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Đóng ServerSocket
            }
            if (threadPool != null) {
                threadPool.shutdown(); // Tắt thread pool
            }
            if (serverThread != null) {
                serverThread.interrupt(); // Ngắt luồng server
            }
            log("Server stopped"); // Ghi log khi server dừng

            startButton.setEnabled(true); // Kích hoạt nút startButton
            stopButton.setEnabled(false); // Vô hiệu hóa nút stopButton
        } catch (IOException e) { // Bắt ngoại lệ IOException
            log("Failed to stop server: " + e.getMessage()); // Ghi log khi không thể dừng server
        }
    }

    // Phương thức ghi log vào logArea
    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n")); // Thêm dòng log vào logArea
    }

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Tạo và hiển thị giao diện người dùng trong luồng sự kiện Swing
            EmployeeServer serverUI = new EmployeeServer(); // Khởi tạo đối tượng EmployeeServer
            serverUI.setVisible(true); // Hiển thị cửa sổ
        });
    }
}
