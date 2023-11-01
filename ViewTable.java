package CompanyDataTable.View;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ViewTable extends JFrame implements ActionListener {
    // JFrame :  Java Swing 라이브러리에서 제공되는 GUI frame inherit
    // ActionListener : Java Swing에서 사용자의 액션(예: 버튼 클릭)에 대한 이벤트 처리를 위한 인터페이스(actionPerformed method)
    public static void main(String[] args) {
        new ViewTable(); // main 함수 실행시 GUI 생성과 동시에 기능 동작 실시
    }
    // AddAction 객체 선언 시작
    AddAction addAction;
    // AddAction 객체 선언 종료

    // JDBC를 위한 필드 선언 시작
    Connection conn;
    Statement statement;
    ResultSet resultSet;
    // JDBC를 위한 필드 선언 종료

    // ComboBox 선언 시작
    JComboBox Category;
    JComboBox Dept;
    JComboBox Sex;
    JComboBox Update;
    // ComboBox 선언 종료

    // User로부터 text input 받을 변수 선언 시작
    JTextField setInfo = new JTextField(10);
    JTextField updateAddressOrSexOrSalary = new JTextField(10);
    // User로부터 text input 받을 변수 선언 종료

    // CheckBox 이름과 초기 상태 선언 시작
    JCheckBox check1 = new JCheckBox("Name", true);
    JCheckBox check2 = new JCheckBox("Ssn", true);
    JCheckBox check3 = new JCheckBox("Bdate", true);
    JCheckBox check4 = new JCheckBox("Address", true);
    JCheckBox check5 = new JCheckBox("Sex", true);
    JCheckBox check6 = new JCheckBox("Salary", true);
    JCheckBox check7 = new JCheckBox("Supervisor", true);
    JCheckBox check8 = new JCheckBox("Department", true);
    // CheckBox 이름과 초기 상태 선언 종료

    // 테이블의 열 제목을 저장하기 위해 Vector(동적으로 크기가 조절되는 배열) 선언 시작
    Vector<String> Head = new Vector<String>();
    // 테이블의 열 제목을 저장하기 위해 Vector(동적으로 크기가 조절되는 배열) 선언 종료

    // 테이블 형식으로 데이터를 관리하기 위해 테이블 선언 시작
    JTable table;
    DefaultTableModel model;
    // 테이블 형식으로 데이터를 관리하기 위해 테이블 선언 종료

    // 업데이트 했을때 사용하기 위한 프로퍼티 선언 시작
    int selectCol = 0;
    int nameCol = 0;
    int salaryCol = 0;
    int addressCol = 0;
    int sexCol = 0;
    String departmentCol;
    int salaryOfResearchDepartment = 0;
    int salaryOfAdministrationDepartment = 0;
    int salaryOfHeadquartersDepartment = 0;
    // 업데이트 했을때 사용하기 위한 프로퍼티 선언 종료


    // 화면에 보이는 버튼 선언 시작
    JButton searchButton = new JButton("Search");
    JButton updateButton = new JButton("Update");
    JButton deleteButton = new JButton("Delete Employee");
    JButton addButton = new JButton("Add Employee");
    // 화면에 보이는 버튼 선언 종료

    // 화면에 보이는 라벨 선언 시작
    JLabel totalEmp = new JLabel("Total number of Employee : "); // 총 인원수를 나타내는 label
    JLabel totalCount = new JLabel(); // 총 선택된 인원수를 나타내는 label
    JLabel empLabel = new JLabel("Selected Employee : "); // 선택된 직원의 label
    JLabel showSelectedEmp = new JLabel(); // 나중에 선택된 직원의 label
    JLabel setLabel = new JLabel("Update : "); // 수정할 목록과 수정할 내용을 표시하는 label
    // 화면에 보이는 라벨 선언 종료


    // 스크롤바 선언 시작
    JPanel panel;
    JScrollPane scrollPanel;	// 스크롤 가능한 컨테이너로 제작
    JPanel ComboBoxPanel = new JPanel();
    // 스크롤바 선언 종료

    // 인원수를 세기위한 변수인 count 선언 시작
    int count = 0;
    // 인원수를 세기위한 변수인 count 선언 종료
    
    // 자기 자신(즉, Container 그 자체)를 나타내는 변수인 thisContainer 선언 시작
    Container thisContainer = this;
    // 자기 자신(즉, Container 그 자체)를 나타내는 변수인 thisContainer 선언 종료
    public ViewTable() {
        // 테이블에 채울 내용 설정 시작
        String[] category = { "전체", "부서","성별","연봉","생일","부하직원","부양가족" };
        String[] dept = { "Research", "Administration", "Headquarters" };
        String[] sex = {"F","M"};
        String[] update = {"Address","Sex","Salary","SalaryOfResearchDepartment", "SalaryOfAdministrationDepartment", "SalaryOfHeadquartersDepartment" };
        Category = new JComboBox(category);
        Dept = new JComboBox(dept);
        Sex = new JComboBox(sex);
        Update = new JComboBox(update);
        // 테이블에 채울 내용 설정 종료

        // Category 혹은 Update에서 변화가 생기면 이벤트 처리 액션 설정 시작
        Category.addActionListener((ActionListener) thisContainer);
        Update.addActionListener((ActionListener) thisContainer);
        // Category 혹은 Update에서 변화가 생기면 이벤트 처리 액션 설정 종료

        // 각각의 버튼들마다 이벤트 설정 시작
        searchButton.addActionListener((ActionListener) thisContainer);
        deleteButton.addActionListener((ActionListener) thisContainer);
        addButton.addActionListener((ActionListener) thisContainer);
        updateButton.addActionListener((ActionListener) thisContainer);
        // 각각의 버튼들마다 이벤트 설정 종료

        // layout 설정 시작
        ComboBoxPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ComboBoxPanel.add(new JLabel("Search Category : "));
        ComboBoxPanel.add(Category);
        JPanel CheckBoxPanel = new JPanel();
        CheckBoxPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        CheckBoxPanel.add(new JLabel("Check Category : "));
        CheckBoxPanel.add(check1);
        CheckBoxPanel.add(check2);
        CheckBoxPanel.add(check3);
        CheckBoxPanel.add(check4);
        CheckBoxPanel.add(check5);
        CheckBoxPanel.add(check6);
        CheckBoxPanel.add(check7);
        CheckBoxPanel.add(check8);
        CheckBoxPanel.add(searchButton);
        JPanel panelAdd = new JPanel();
        panelAdd.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelAdd.add(addButton);
        JPanel panelCheckboxAdd = new JPanel();
        panelCheckboxAdd.setLayout(new BoxLayout(panelCheckboxAdd, BoxLayout.X_AXIS));
        panelCheckboxAdd.add(CheckBoxPanel);
        panelCheckboxAdd.add(panelAdd);
        JPanel panelShowSelected = new JPanel();
        panelShowSelected.setLayout(new FlowLayout(FlowLayout.LEFT));
        empLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        showSelectedEmp.setFont(new Font("Dialog", Font.BOLD, 20));
        departmentCol = "";
        panelShowSelected.add(empLabel);
        panelShowSelected.add(showSelectedEmp);
        JPanel panelTotal = new JPanel();
        panelTotal.setLayout(new FlowLayout(FlowLayout.LEFT));
        totalCount.setFont(new Font("Dialog", Font.BOLD, 20));
        panelTotal.add(totalEmp);
        panelTotal.add(totalCount);
        JPanel panelUpdate = new JPanel();
        panelUpdate.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelUpdate.setFont(new Font("Dialog", Font.BOLD, 20));
        panelUpdate.add(setLabel);
        panelUpdate.add(Update);
        panelUpdate.add(updateAddressOrSexOrSalary);
        panelUpdate.add(updateButton);
        JPanel panelDelete = new JPanel();
        panelDelete.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelDelete.add(deleteButton);
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.add(ComboBoxPanel);
        panelTop.add(panelCheckboxAdd);
        JPanel panelHalfway = new JPanel();
        panelHalfway.setLayout(new BoxLayout(panelHalfway, BoxLayout.X_AXIS));
        panelHalfway.add(panelShowSelected);
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.X_AXIS));
        panelBottom.add(panelTotal);
        panelBottom.add(panelUpdate);
        panelBottom.add(panelDelete);
        JPanel panelVertical = new JPanel();
        panelVertical.setLayout(new BoxLayout(panelVertical, BoxLayout.Y_AXIS));
        panelVertical.add(panelHalfway);
        panelVertical.add(panelBottom);
        add(panelTop, BorderLayout.NORTH);
        add(panelVertical, BorderLayout.SOUTH);
        setTitle("Information Retrival System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        // layout 설정 종료
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // JDBC Connect 시작
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 연결 : Build 관리는 IntelliJ에서 실행하므로 jar파일을 다운받아서 넣어둠
            String user = "root";   // root user
            String password = "b9081620"; // 비밀번호 입력
            String databaseName = "company";
            String connectUrl = "jdbc:mysql://localhost:3306/" + databaseName;
            conn = DriverManager.getConnection(connectUrl, user, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (Exception error) {
            System.err.println("에러가 발생하였습니다.");
            error.printStackTrace();
        }
        // JDBC Connect 종료

        // 첫 시작시 일단 아무것도 없는 상태로 두기 위한 설정 시작
        if (count == 1) {
            thisContainer.remove(panel);
            revalidate();
        }
        // 첫 시작시 일단 아무것도 없는 상태로 두기 위한 설정 종료

        // User가 고른 category에 맞는 동작 설정 시작
        String selectedCategory = Category.getSelectedItem().toString();
        if(selectedCategory == "부서") {
            if(ComboBoxPanel.getComponentCount() != 2) {
                ComboBoxPanel.remove(2);
            }
            ComboBoxPanel.add(Dept);
            ComboBoxPanel.repaint();
            revalidate();
        }else if(selectedCategory == "성별") {
            if(ComboBoxPanel.getComponentCount() != 2) {
                System.out.println(ComboBoxPanel.getComponentCount());
                ComboBoxPanel.remove(2);
            }
            ComboBoxPanel.add(Sex);
            ComboBoxPanel.repaint();
            revalidate();
        }else if(selectedCategory.equals("연봉") || selectedCategory.equals("생일")
                || selectedCategory.equals("부하직원") || selectedCategory.equals("부양가족")){
            if(ComboBoxPanel.getComponentCount() != 2) {
                ComboBoxPanel.remove(2);
            }
            ComboBoxPanel.repaint();
            ComboBoxPanel.add(setInfo);
            revalidate();
        }else if(selectedCategory.equals("전체")) {
            if(ComboBoxPanel.getComponentCount() != 2) {
                ComboBoxPanel.remove(2);
            }
            ComboBoxPanel.repaint();
            revalidate();
        }
        // User가 고른 category에 맞는 동작 설정 종료

        // Search Button 클릭시 동작하는 동작 설정 시작
        if (actionEvent.getSource() == searchButton) {
            String getInfo = null;
            String queryStatement;
            if(setInfo.getText() != null) {
                getInfo = setInfo.getText();
            }
            if(Category.getSelectedItem().toString() == "부양가족") {
                Head.clear();
                Head.add(" ");
                Head.add("부양가족 이름");
                Head.add("성별");
                Head.add("생일");
                Head.add("관계");
                String fname = null;
                String minit = null;
                String lname = null;
                int nameCorrect = 1;
                try {
                    String[] name = getInfo.split(" ");
                    fname = name[0];
                    minit = name[1];
                    lname = name[2];
                }catch(Exception error) {
                    nameCorrect = 0;
                    JOptionPane.showMessageDialog(null, "부양가족의 이름을 'fname(공백)minit(공백)lname'의 형식에 맞게 정확하게 입력해주세요.");
                }
                queryStatement = "select d.dependent_name, d.sex,d.bdate,d.relationship from employee s, dependent d where s.ssn = d.essn";
                queryStatement += " and s.fname = \"" +fname + "\""+ " and s.minit = \"" + minit + "\""+" and s.lname = \"" + lname + "\"" + ";";
                setInfo.setText("");
                model = new DefaultTableModel(Head, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) { return true;}};
                table = new JTable(model) {
                    @Override
                    public Class getColumnClass(int column) { return " ".getClass();}};

                showSelectedEmp.setText(" ");
                int personCount = 0;
                try {
                    count = 1;
                    statement = conn.createStatement();
                    resultSet = statement.executeQuery(queryStatement);
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();
                    int rowCount = table.getRowCount();

                    while (resultSet.next()) {
                        Vector<Object> rowElement = new Vector<Object>();
                        rowElement.add(" ");
                        for (int i = 1; i < columnCount + 1; i++) {
                            rowElement.add(resultSet.getString(resultSetMetaData.getColumnName(i)));
                        }
                        model.addRow(rowElement);
                        rowCount++;
                        personCount++;
                    }
                    totalCount.setText(String.valueOf(rowCount));
                } catch (SQLException error) {
                    System.out.println("actionPerformed err : " + error);
                    error.printStackTrace();
                }

                if(nameCorrect == 1){
                    if(personCount == 0) {
                        JOptionPane.showMessageDialog(null, "선택한 직원에게 딸려있는 부양가족은 없습니다.");
                    }else {
                        panel = new JPanel();
                        scrollPanel = new JScrollPane(table);
                        scrollPanel.setPreferredSize(new Dimension(1200, 500));
                        panel.add(scrollPanel);
                        add(panel, BorderLayout.CENTER);
                        revalidate();
                    }
                }

            }else {
                if (check1.isSelected() || check2.isSelected() || check3.isSelected() || check4.isSelected() || check5.isSelected()
                        || check6.isSelected() || check7.isSelected() || check8.isSelected()) {
                    Head.clear();
                    Head.add("Select");

                    queryStatement = "select";
                    if (check1.isSelected()) {
                        queryStatement += " concat(e.fname,' ', e.minit,' ', e.lname,' ') as Name";
                        Head.add("NAME");
                    }
                    if (check2.isSelected()) {
                        if (!check1.isSelected())
                            queryStatement += " e.ssn";
                        else
                            queryStatement += ", e.ssn";
                        Head.add("SSN");
                    }
                    if (check3.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected())
                            queryStatement += " e.bdate";
                        else
                            queryStatement += ", e.bdate";
                        Head.add("BDATE");
                    }
                    if (check4.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected() && !check3.isSelected())
                            queryStatement += " e.address";
                        else
                            queryStatement += ", e.address";
                        Head.add("ADDRESS");
                    }
                    if (check5.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected() && !check3.isSelected() && !check4.isSelected())
                            queryStatement += " e.sex";
                        else
                            queryStatement += ", e.sex";
                        Head.add("SEX");
                    }
                    if (check6.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected() && !check3.isSelected() && !check4.isSelected()
                                && !check5.isSelected())
                            queryStatement += " e.salary";
                        else
                            queryStatement += ", e.salary";
                        Head.add("SALARY");
                    }
                    if (check7.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected() && !check3.isSelected() && !check4.isSelected() && !check5.isSelected()
                                && !check6.isSelected())
                            queryStatement += " concat(s.fname, ' ', s.minit, ' ',s.lname,' ') as Supervisor ";
                        else
                            queryStatement += ", concat(s.fname, ' ', s.minit, ' ',s.lname,' ') as Supervisor ";
                        Head.add("SUPERVISOR");
                    }
                    if (check8.isSelected()) {
                        if (!check1.isSelected() && !check2.isSelected() && !check3.isSelected() && !check4.isSelected() && !check5.isSelected()
                                && !check6.isSelected() && !check7.isSelected())
                            queryStatement += " dname";
                        else
                            queryStatement += ", dname";
                        Head.add("DEPARTMENT");
                    }
                    queryStatement += " from employee e left outer join employee s ";
                    queryStatement += "on e.super_ssn=s.ssn, department where e.dno = dnumber";
                    if (Category.getSelectedItem().toString() == "부서") {
                        if (Dept.getSelectedItem().toString() == "Research")
                            queryStatement += " and dname = \"Research\";";
                        else if (Dept.getSelectedItem().toString() == "Administration")
                            queryStatement += " and dname = \"Administration\";";
                        else if (Dept.getSelectedItem().toString() == "Headquarters")
                            queryStatement += " and dname = \"Headquarters\";";
                    }else if(Category.getSelectedItem().toString() == "성별") {
                        if(Sex.getSelectedItem().toString() == "F") {
                            queryStatement += " and e.SEX = \"F\";";
                        }else if(Sex.getSelectedItem().toString() == "M") {
                            queryStatement += " and e.SEX = \"M\";";
                        }
                    }else if(Category.getSelectedItem().toString() == "연봉") {
                        queryStatement += " and e.salary >=" + getInfo + ";";
                        setInfo.setText("");
                    }else if(Category.getSelectedItem().toString() == "생일") {
                        queryStatement += " and e.bdate like \"____-" + getInfo + "%\";";
                        setInfo.setText("");
                    }else if(Category.getSelectedItem().toString() == "부하직원") {
                        String[] name = getInfo.split(" ");
                        String fname = name[0];
                        String minit = name[1];
                        String lname = name[2];
                        queryStatement += " and s.fname = \"" +fname + "\""+
                                " and s.minit = \"" + minit + "\""+
                                " and s.lname = \"" + lname + "\"" + ";";
                        setInfo.setText("");
                    }

                    model = new DefaultTableModel(Head, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            if (column > 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    for (int i = 0; i < Head.size(); i++) {
                        if (Head.get(i) == "NAME") {
                            nameCol = i;
                        } else if (Head.get(i) == "SALARY") {
                            salaryCol = i;
                        }else if (Head.get(i) == "ADDRESS") {
                            addressCol = i;
                        }else if (Head.get(i) == "SEX") {
                            sexCol = i;
                        } else if (Head.get(i) == "SalaryOfResearchDepartment") {
                            salaryOfResearchDepartment = i;
                        } else if (Head.get(i) == "SalaryOfAdministrationDepartment") {
                            salaryOfAdministrationDepartment = i;
                        } else if (Head.get(i) == "SalaryOfHeadquartersDepartment") {
                            salaryOfHeadquartersDepartment = i;
                        }
                    }

                    table = new JTable(model) {
                        @Override
                        public Class getColumnClass(int column) {
                            if (column == 0) {
                                return Boolean.class;
                            } else
                                return String.class;
                        }
                    };
                    showSelectedEmp.setText(" ");
                    try {
                        count = 1;
                        statement = conn.createStatement();
                        resultSet = statement.executeQuery(queryStatement);
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnCnt = rsmd.getColumnCount();
                        int rowCnt = table.getRowCount();
                        while (resultSet.next()) {
                            Vector<Object> tuple = new Vector<Object>();
                            tuple.add(false);
                            for (int i = 1; i < columnCnt + 1; i++) {
                                tuple.add(resultSet.getString(rsmd.getColumnName(i)));
                            }
                            model.addRow(tuple);
                            rowCnt++;
                        }
                        totalCount.setText(String.valueOf(rowCnt));
                    } catch (SQLException error) {
                        System.out.println("actionPerformed err : " + error);
                        error.printStackTrace();

                    }
                    panel = new JPanel();
                    scrollPanel = new JScrollPane(table);
                    table.getModel().addTableModelListener(event -> {
                                int row = event.getFirstRow();
                                int column = event.getColumn();
                                if (column == selectCol) {
                                    TableModel model = (TableModel) event.getSource();
                                    String columnName = model.getColumnName(1);
                                    Boolean checked = (Boolean) model.getValueAt(row, column);
                                    if (columnName == "NAME") {
                                        if (checked) {
                                            departmentCol = "";
                                            for (int i = 0; i < table.getRowCount(); i++) {
                                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                                    departmentCol += (String) table.getValueAt(i, nameCol) + "    ";

                                                }
                                            }
                                            showSelectedEmp.setText(departmentCol);
                                        } else {
                                            departmentCol = "";
                                            for (int i = 0; i < table.getRowCount(); i++) {
                                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                                    departmentCol += (String) table.getValueAt(i, 1) + "    ";
                                                }
                                            }
                                            showSelectedEmp.setText(departmentCol);
                                        }
                                    }
                                }
                            }
                    );
                    scrollPanel.setPreferredSize(new Dimension(1200, 500));
                    panel.add(scrollPanel);
                    add(panel, BorderLayout.CENTER);
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "검색하고자 하는 항목을 반드시 한 개이상 체크해주세요.");
                }
            }
        }
        // Search Button 클릭시 동작하는 동작 설정 종료


        // Delete Button 클릭시 동작하는 동작 설정 시작
        if (actionEvent.getSource() == deleteButton) {
            Vector<String> delete_ssn = new Vector<String>();
            try {
                String columnName = model.getColumnName(2);
                if (columnName == "SSN") {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (table.getValueAt(i, 0) == Boolean.TRUE) {
                            delete_ssn.add((String) table.getValueAt(i, 2));
                        }
                    }
                    for (int i = 0; i < delete_ssn.size(); i++) {
                        for (int k = 0; k < model.getRowCount(); k++) {
                            if (table.getValueAt(k, 0) == Boolean.TRUE) {
                                model.removeRow(k);
                                totalCount.setText(String.valueOf(table.getRowCount()));
                            }
                        }
                    }
                    for (int i = 0; i < delete_ssn.size(); i++) {
                        String deleteStmt = "DELETE FROM EMPLOYEE WHERE Ssn=?";
                        PreparedStatement p = conn.prepareStatement(deleteStmt);
                        p.clearParameters();
                        p.setString(1, String.valueOf(delete_ssn.get(i)));
                        p.executeUpdate();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "NAME과 SSN을 모두 다 체크하셔야 삭제 작업 진행이 가능합니다.");
                }
                showSelectedEmp.setText(" ");
            } catch (SQLException error) {
                System.out.println("actionPerformed err : " + error);
                error.printStackTrace();
            }
            panel = new JPanel();
            scrollPanel = new JScrollPane(table);
            table.getModel().addTableModelListener(event -> {
                        int row = event.getFirstRow();
                        int column = event.getColumn();
                        if (column == selectCol) {
                            TableModel model = (TableModel) event.getSource();
                            String columnName = model.getColumnName(1);
                            Boolean checked = (Boolean) model.getValueAt(row, column);
                            if (columnName == "NAME") {
                                if (checked) {
                                    departmentCol = "";
                                    for (int i = 0; i < table.getRowCount(); i++) {
                                        if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                            departmentCol += (String) table.getValueAt(i, nameCol) + "    ";
                                        }
                                    }
                                    showSelectedEmp.setText(departmentCol);
                                } else {
                                    departmentCol = "";
                                    for (int i = 0; i < table.getRowCount(); i++) {
                                        if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                            departmentCol += (String) table.getValueAt(i, 1) + "    ";
                                        }
                                    }
                                    showSelectedEmp.setText(departmentCol);
                                }
                            }
                        }
                    }
            );
            scrollPanel.setPreferredSize(new Dimension(1200, 500));
            panel.add(scrollPanel);
            add(panel, BorderLayout.CENTER);
            revalidate();
        }
        // Delete Button 클릭시 동작하는 동작 설정 시작

        // Update Button 클릭시 동작하는 동작 설정 시작
        if (actionEvent.getSource() == updateButton) {
            Vector<String> update_ssn = new Vector<String>();
            try {
                String columnName = model.getColumnName(2);
                System.out.println(columnName);
                if (columnName == "SSN") {
                    if(Update.getSelectedItem().toString() == "Salary") {
                        if(model.getColumnName(salaryCol) == "SALARY") {
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                    String updateSalary = updateAddressOrSexOrSalary.getText();
                                    table.setValueAt(Double.parseDouble(updateSalary), i, salaryCol);
                                    String updateStmt = "UPDATE EMPLOYEE SET Salary=? , modified=SYSDATE() WHERE Ssn=?";
                                    PreparedStatement p = conn.prepareStatement(updateStmt);
                                    p.clearParameters();
                                    p.setString(1, updateSalary);
                                    p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
                                    p.executeUpdate();
                                }
                            }
                        }else {
                            JOptionPane.showMessageDialog(null, "SALARY에 대한 수정 작업을 진행하기 위해 SALARY를 체크해주세요");
                        }
                    }else if(Update.getSelectedItem().toString() == "Sex") {
                        if(model.getColumnName(sexCol) == "SEX") {
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                    String updateSalary = updateAddressOrSexOrSalary.getText();
                                    table.setValueAt(updateSalary, i, sexCol);
                                    String updateStmt = "UPDATE EMPLOYEE SET SEX=? , modified=SYSDATE() WHERE Ssn=?";
                                    PreparedStatement p = conn.prepareStatement(updateStmt);
                                    p.clearParameters();
                                    p.setString(1, String.valueOf(updateSalary));
                                    p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
                                    p.executeUpdate();
                                }
                            }
                        }else {
                            JOptionPane.showMessageDialog(null, "SEX에 대한 수정 작업을 진행하기 위해 SEX를 체크해주세요");
                        }

                    }else if(Update.getSelectedItem().toString() == "Address") {
                        if(model.getColumnName(addressCol) == "ADDRESS") {
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                    String updateSalary = updateAddressOrSexOrSalary.getText();
                                    table.setValueAt(updateSalary, i, addressCol);
                                    String updateStmt = "UPDATE EMPLOYEE SET Address=? , modified=SYSDATE() WHERE Ssn=?";
                                    PreparedStatement p = conn.prepareStatement(updateStmt);
                                    p.clearParameters();
                                    p.setString(1, String.valueOf(updateSalary));
                                    p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
                                    p.executeUpdate();
                                }
                            }
                        }else {
                            JOptionPane.showMessageDialog(null, "ADDRESS 대한 수정 작업을 진행하기 위해 ADDRESS를 체크해주세요");
                        }
                    }else if (Update.getSelectedItem().toString() == "SalaryOfResearchDepartment") {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 8).toString().equals("Research")) {
                                String updateSalary = updateAddressOrSexOrSalary.getText();
                                table.setValueAt(Double.parseDouble(updateSalary), i, salaryCol);
                                String ex = "Research";
                                String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
                                PreparedStatement p = conn.prepareStatement(updateStmt);
                                p.clearParameters();
                                p.setString(1, updateSalary);
                                p.setString(2, ex);
                                p.executeUpdate();
                            }
                        }
                    } else if (Update.getSelectedItem().toString() == "SalaryOfAdministrationDepartment") {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 8).toString().equals("Administration")) {
                                String updateSalary = updateAddressOrSexOrSalary.getText();
                                table.setValueAt(Double.parseDouble(updateSalary), i, salaryCol);
                                String ex = "Administration";
                                String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
                                PreparedStatement p = conn.prepareStatement(updateStmt);
                                p.clearParameters();
                                p.setString(1, updateSalary);
                                p.setString(2, ex);
                                p.executeUpdate();

                            }
                        }
                    } else if (Update.getSelectedItem().toString() == "SalaryOfHeadquartersDepartment") {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 8).toString().equals("Headquarters")) {
                                String updateSalary = updateAddressOrSexOrSalary.getText();
                                table.setValueAt(Double.parseDouble(updateSalary), i, salaryCol);
                                String ex = "Headquarters";
                                String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
                                PreparedStatement p = conn.prepareStatement(updateStmt);
                                p.clearParameters();
                                p.setString(1, updateSalary);
                                p.setString(2, ex);
                                p.executeUpdate();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "NAME과 SSN에 대한 수정 작업을 진행하기 위해 NAME과 SSN를 체크해주세요");
                    }
                }
                showSelectedEmp.setText(" ");
            } catch (SQLException error) {
                System.out.println("actionPerformed err : " + error);
                error.printStackTrace();
            }
            panel = new JPanel();
            scrollPanel = new JScrollPane(table);
            table.getModel().addTableModelListener(event -> {
                int row = event.getFirstRow();
                int column = event.getColumn();
                if (column == selectCol) {
                    TableModel model = (TableModel) event.getSource();
                    String columnName = model.getColumnName(1);
                    Boolean checked = (Boolean) model.getValueAt(row, column);
                    if (columnName == "NAME") {
                        if (checked) {
                            departmentCol = "";
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                    departmentCol += (String) table.getValueAt(i, nameCol) + "    ";

                                }
                            }
                            showSelectedEmp.setText(departmentCol);
                        } else {
                            departmentCol = "";
                            for (int i = 0; i < table.getRowCount(); i++) {
                                if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                    departmentCol += (String) table.getValueAt(i, 1) + "    ";
                                }
                            }
                            showSelectedEmp.setText(departmentCol);
                        }
                    }
                }
            }
        );
        scrollPanel.setPreferredSize(new Dimension(1200, 500));
            panel.add(scrollPanel);
            add(panel, BorderLayout.CENTER);
            revalidate();
        }
        // Update Button 클릭시 동작하는 동작 설정 종료


        // Add Button 클릭시 동작하는 동작 설정 시작
        if(actionEvent.getSource() == addButton) {
            addAction = new AddAction();
            addAction.button.addActionListener(this);
        }

        if(this.addAction != null) {
            String ssn = null;
            if(actionEvent.getSource() == addAction.button) {
                String text_FirstName,text_MiddleInit,text_LastName,text_Ssn,
                        text_Birthdate,text_Address,box_Sex,text_Salary,text_Super_ssn,text_Dno = "NULL";
                ssn = addAction.inputSsn.getText();
                text_FirstName = "'" + addAction.inputFirstName.getText()+"'";
                text_MiddleInit = "'" + addAction.inputMiddleInit.getText()+"'";
                text_LastName = "'" + addAction.inputLastName.getText() + "'";
                text_Ssn = "'" + addAction.inputSsn.getText() + "'";
                text_Birthdate = "'" + addAction.inputBirthDate.getText() + "'";
                text_Address = "'" + addAction.inputAddress.getText() + "'";
                box_Sex = "'" + addAction.boxSex.getSelectedItem().toString() + "'";
                text_Salary = addAction.inputSalary.getText();
                text_Super_ssn = "'" + addAction.inputSuperSsn.getText() + "'";
                text_Dno = addAction.inputDno.getText();

                if(text_MiddleInit.equals("'" + "'")) {
                    text_MiddleInit = "null";
                }else if(text_Birthdate.equals("'" + "'")) {
                    text_Birthdate = "null";
                }else if(text_Address.equals("'" + "'")) {
                    text_Address = "null";
                }else if(text_Salary.equals("'" + "'")) {
                    text_Salary = "null";
                }else if(text_Super_ssn.equals("'" + "'")) {
                    text_Super_ssn = "null";
                }else if(text_Dno.equals("'" + "'")) {
                    text_Dno = "null";
                }

                if( isStringEmpty(text_FirstName) || isStringEmpty(text_LastName) ||
                        isStringEmpty(text_Ssn) || isStringEmpty(text_Dno)) {
                    JOptionPane.showMessageDialog(null, "FirstName, LastName,Ssn,Dno Should not be NULL");
                }else {
                    try {
                        String sql = "insert into Employee(Fname,Minit,Lname,Ssn,Bdate,Address,Sex,"
                                + "Salary,Super_ssn,Dno) "
                                + "values("+text_FirstName+","+
                                text_MiddleInit+","+
                                text_LastName+","+
                                text_Ssn+","+
                                text_Birthdate+","+
                                text_Address+","+
                                box_Sex+","+
                                Integer.parseInt(text_Salary)+","+
                                text_Super_ssn+","+
                                Integer.parseInt(text_Dno)+");";
                        Statement s = conn.createStatement();
                        int result = s.executeUpdate(sql);
                        addAction.dispose();
                        JOptionPane.showMessageDialog(null, "직원 추가 성공!");
                    } catch (Exception error) {
                        String error_string = error.getMessage().toString();
                        if(error_string.contains("Duplicate entry")) {
                            JOptionPane.showMessageDialog(null, "Ssn이 이미 존재합니다. 다른 번호로 입력해주세요.");
                        }else if(error_string.contains("Bdate")){
                            JOptionPane.showMessageDialog(null, "생일을 형식에 맞게 입력해주세요");
                        }else if(error_string.contains("For input string")){
                            JOptionPane.showMessageDialog(null, "Salary와 Dno는 숫자를 입력해야 합니다.");
                        }else if(error_string.contains("Minit")){
                            JOptionPane.showMessageDialog(null, "Minit에는 한 글자만 입력해주세요.");
                        }else if(error_string.contains("Ssn")){
                            JOptionPane.showMessageDialog(null, "Ssn은 9자리까지만 입력 가능합니다.");
                        }else {
                            JOptionPane.showMessageDialog(null, "다시 입력하세요.");
                        }

                    }

                    try {
                        String updateStmt = "update employee set created = current_timestamp(),modified = current_timestamp() where ssn = ?;";
                        PreparedStatement p = conn.prepareStatement(updateStmt);
                        p.clearParameters();
                        p.setString(1, addAction.inputSsn.getText());
                        p.executeUpdate();
                    }catch(SQLException e2) {
                        e2.printStackTrace();
                    }

                }
            }
        }
        // Add Button 클릭시 동작하는 동작 설정 종료
    }

    public boolean isStringEmpty(String str) {
        return str.isEmpty() || str == null;
    }

    private class AddAction extends JFrame {
        JButton button = new JButton("Add Employee");
        JLabel firstName = new JLabel("First Name :\t");
        JTextField inputFirstName = new JTextField(10);
        JLabel middleInit = new JLabel("Middle Init :\t");
        JTextField inputMiddleInit = new JTextField(10);
        JLabel lastName = new JLabel("Last Name :\t");
        JTextField inputLastName = new JTextField(10);
        JLabel ssn = new JLabel("Ssn :\t\t\t\t\t\t\t\t\t\t\t\t");
        JTextField inputSsn = new JTextField(10);
        JLabel birthDate = new JLabel("Birthdate :\t\t\t");
        JTextField inputBirthDate = new JTextField(10);
        JLabel address = new JLabel("Address :\t\t\t\t");
        JTextField inputAddress = new JTextField(10);
        JLabel Sex = new JLabel("Sex :\t\t\t\t\t\t\t\t\t\t\t\t\t ");
        String[] inputSex = {"F","M"};
        JComboBox boxSex = new JComboBox(inputSex);

        JLabel salary = new JLabel("Salary : \t\t\t\t\t\t\t");
        JTextField inputSalary = new JTextField(10);

        JLabel superSsn = new JLabel("Super_ssn :\t");
        JTextField inputSuperSsn = new JTextField(10);

        JLabel dno = new JLabel("Dno : \t\t\t\t\t\t\t\t\t");
        JTextField inputDno = new JTextField(10);


        public AddAction() {
            JPanel firstName = new JPanel();
            firstName.setLayout(new FlowLayout(FlowLayout.LEFT));
            firstName.add(this.firstName);
            firstName.add(inputFirstName);

            JPanel middleInit = new JPanel();
            middleInit.setLayout(new FlowLayout(FlowLayout.LEFT));
            middleInit.add(this.middleInit);
            middleInit.add(inputMiddleInit);

            JPanel lastName = new JPanel();
            lastName.setLayout(new FlowLayout(FlowLayout.LEFT));
            lastName.add(this.lastName);
            lastName.add(inputLastName);

            JPanel ssn = new JPanel();
            ssn.setLayout(new FlowLayout(FlowLayout.LEFT));
            ssn.add(this.ssn);
            ssn.add(inputSsn);

            JPanel birthDate = new JPanel();
            birthDate.setLayout(new FlowLayout(FlowLayout.LEFT));
            birthDate.add(this.birthDate);
            birthDate.add(inputBirthDate);

            JPanel address = new JPanel();
            address.setLayout(new FlowLayout(FlowLayout.LEFT));
            address.add(this.address);
            address.add(inputAddress);

            JPanel sex = new JPanel();
            sex.setLayout(new FlowLayout(FlowLayout.LEFT));
            sex.add(this.Sex);
            sex.add(boxSex);

            JPanel salary = new JPanel();
            salary.setLayout(new FlowLayout(FlowLayout.LEFT));
            salary.add(this.salary);
            salary.add(inputSalary);

            JPanel superSsn = new JPanel();
            superSsn.setLayout(new FlowLayout(FlowLayout.LEFT));
            superSsn.add(this.superSsn);
            superSsn.add(inputSuperSsn);

            JPanel dno = new JPanel();
            dno.setLayout(new FlowLayout(FlowLayout.LEFT));
            dno.add(this.dno);
            dno.add(inputDno);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(button);


            JPanel top = new JPanel();
            top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.add(firstName);
            top.add(middleInit);
            top.add(lastName);
            top.add(ssn);
            top.add(birthDate);
            top.add(address);
            top.add(sex);
            top.add(salary);
            top.add(superSsn);
            top.add(dno);
            top.add(buttonPanel);

            add(top, BorderLayout.CENTER);

            setTitle("Add Employee");
            setSize(500, 700);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}




