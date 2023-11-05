package dao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Search_Insert extends JFrame implements ActionListener {

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

	private JButton insertButton = new JButton("직원 추가");
	public Insert insert;

	public Search_Insert() {

		JPanel InsertPanel = new JPanel();
		InsertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		InsertPanel.add(insertButton);

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
		insertButton.addActionListener(this);

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
		else if (e.getSource() == insertButton) {
			insert = new Insert();
			insert.button.addActionListener(this);
		}

		if(this.insert != null) {
			if(e.getSource() == insert.button) {
				String text_FirstName,text_MiddleInit,text_LastName,text_Ssn,
						text_Birthdate,text_Address,box_Sex,text_Salary,text_Super_ssn,text_Dno = "NULL";
				text_FirstName = "'" + insert.text_FirstName.getText()+"'";
				text_MiddleInit = "'" + insert.text_MiddleInit.getText()+"'";
				text_LastName = "'" + insert.text_LastName.getText() + "'";
				text_Ssn = "'" + insert.text_Ssn.getText() + "'";
				text_Birthdate = "'" + insert.text_Birthdate.getText() + "'";
				text_Address = "'" + insert.text_Address.getText() + "'";
				box_Sex = "'" + insert.box_Sex.getSelectedItem().toString() + "'";
				text_Salary = insert.text_Salary.getText();
				text_Super_ssn = "'" + insert.text_Super_ssn.getText() + "'";
				text_Dno = insert.text_Dno.getText();

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
					JOptionPane.showMessageDialog(null, "FirstName, LastName,Ssn,Dno는 비어있으면 안됩니다.");
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
						insert.dispose();
						JOptionPane.showMessageDialog(null, "성공적으로 직원이 추가되었습니다!");
					} catch (Exception e1) {
						String error_string = e1.getMessage().toString();

						if(error_string.contains("Duplicate entry")) {
							JOptionPane.showMessageDialog(null, "Ssn이 존재합니다! 다른 Ssn을 입력하세요!");
						}else if(error_string.contains("Bdate")){
							JOptionPane.showMessageDialog(null, "생일을 올바르게 입력하세요!");
						}else if(error_string.contains("For input string")){
							JOptionPane.showMessageDialog(null, "급여와 부서 번호는 숫자를 입력하세요!");
						}else if(error_string.contains("Minit")){
							JOptionPane.showMessageDialog(null, "Minit에는 한 글자만 입력하세요!");
						}else if(error_string.contains("Ssn")){
							JOptionPane.showMessageDialog(null, "Ssn은 9자리까지만 입력 가능합니다!");
						}else {
							JOptionPane.showMessageDialog(null, "다시 입력해주시기 바랍니다!");
						}

					}

					try {
						String updateStmt = "update employee set created = current_timestamp(),modified = current_timestamp() where ssn = ?;";

						PreparedStatement p = conn.prepareStatement(updateStmt);
						p.clearParameters();
						p.setString(1, insert.text_Ssn.getText());
						p.executeUpdate();
					}catch(SQLException e2) {
						e2.printStackTrace();
					}

				}
			}
		}
	}

	static boolean isStringEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static void main(String[] args) {
		new Search_Insert();
	}
}