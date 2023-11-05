package view;

import dao.ResultSetTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainGeneral extends JFrame implements ActionListener {

    public Connection conn;
    public Statement s;
    public ResultSet r;
    private JButton searchButton = new JButton("보고서 출력");
    private JButton executeButton = new JButton("SQL 실행");
    private JLabel selectLabel = new JLabel("선택할 속성:");
    private JLabel whereLabel = new JLabel("선택할 조건:");
    private JLabel sortLabel = new JLabel("정렬 조건:");
    private JTextArea selectTextArea = new JTextArea(3, 10);
    private JTextArea whereTextArea = new JTextArea(3, 10);
    private JTextArea sortTextArea = new JTextArea(3, 10);
    private JTable resultTable = new JTable();
    private JCheckBox checkBox = new JCheckBox("내림차순");

    public MainGeneral() {

        JPanel InsertPanel = new JPanel();
        InsertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JPanel Top = new JPanel();
        Top.add(searchButton, BorderLayout.WEST);

        Top.add(selectLabel);
        Top.add(selectTextArea, BorderLayout.CENTER);

        Top.add(whereLabel);
        Top.add(whereTextArea, BorderLayout.CENTER);

        Top.add(sortLabel);
        Top.add(sortTextArea, BorderLayout.CENTER);
        Top.add(checkBox);

        Top.add(executeButton, BorderLayout.EAST);
        Top.add(InsertPanel, BorderLayout.EAST);

        add(Top, BorderLayout.NORTH);
        add(resultTable);

        searchButton.addActionListener(this);
        executeButton.addActionListener(this);

        setTitle("CompanyDB");
        setSize(1300, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        try {
            String user = "root";
            String pwd = "12345678"; // 비밀번호 입력
            String dbname = "company";
            String url = "jdbc:mysql://localhost:3306/" + dbname;

            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("정상적으로 연결되었습니다.");

        } catch (SQLException e1) {
            System.err.println("연결할 수 없습니다.");
            e1.printStackTrace();
        }

        if (e.getSource() == searchButton) {
            try {
                s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                r = s.executeQuery("SELECT e.Fname, e.Minit, e.Lname, e.Ssn, e.Bdate, e.Address, e.Sex, e.Salary, e.Super_ssn, d.Dname FROM EMPLOYEE e LEFT OUTER JOIN DEPARTMENT d ON e.dno=d.dnumber");

                resultTable.setModel(new ResultSetTableModel(r));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == executeButton) {
            String select = selectTextArea.getText();
            String where = whereTextArea.getText();
            String sort = sortTextArea.getText();
            String sql = "SELECT ";

            if (select == null || select.isEmpty()) {
                sql += "*";
            } else {
                sql += select;
            }

            sql += " FROM EMPLOYEE";

            if (where != null && !where.isEmpty()) {
                sql += " WHERE " + where;
            }
            if (sort != null && !sort.isEmpty()) {
                sql += " ORDER BY " + sort ;
            }
            if (checkBox.isSelected() && sort != null && !sort.isEmpty()) {
                sql += " DESC ;";
            }


            try {
                s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                r = s.executeQuery(sql);

                resultTable.setModel(new ResultSetTableModel(r));
            } catch (SQLException ex) {
                String error_string = ex.getMessage().toString();
                JOptionPane.showMessageDialog(null, error_string);
            }
        }
    }

    public static void main(String[] args) {
        new MainGeneral();
    }
}
