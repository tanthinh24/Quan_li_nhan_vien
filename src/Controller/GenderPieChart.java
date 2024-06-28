
package Controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

public class GenderPieChart extends JFrame {
    private static final String username = "root";
    private static final String password = "12345";
    private static final String dataConn = "jdbc:mysql:///employee_management_system";

    public GenderPieChart(String title) {
        super(title);

        DefaultPieDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createPieChart(
                "Gender Distribution",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1} ({2})"));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    private DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        try (Connection connection = DriverManager.getConnection(dataConn, username, password)) {
            String sql = "SELECT gender, COUNT(*) as count FROM employee GROUP BY gender";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String gender = resultSet.getString("gender");
                    int count = resultSet.getInt("count");
                    dataset.setValue(gender, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }
}


