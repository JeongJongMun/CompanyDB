import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class Delete_Update extends JFrame implements ActionListener {

	public Connection conn;
	public Statement s;
	public ResultSet r;

	private JComboBox Update;
	private JTable resultTable = new JTable();


	private Vector<String> Head = new Vector<String>();

	private JTable table;
	private DefaultTableModel model;
	private static final int BOOLEAN_COLUMN = 0;
	private int NAME_COLUMN = 0;
	private int SALARY_COLUMN = 0;		// salary 업데이트 시 사용
	private int ADDRESS_COLUMN = 0;		// address 업데이트 시 사용
	private int SEX_COLUMN = 0;			// sex 업데이트 시 사용
	private String dShow;


	private JButton Search_Button = new JButton("검색");
	Container me = this;


	final JLabel totalCount = new JLabel();
	JPanel panel;
	JScrollPane ScPane;

	private JLabel ShowSelectedEmp = new JLabel();
	private JLabel Setlabel_Update = new JLabel("Ssn 입력: ");
	private JTextField Ssn_Update = new JTextField(10);
	private JTextField update_Salary_Address_Sex = new JTextField(10);
	private JButton Update_Button = new JButton("UPDATE");
	private JLabel Setlabel_Delete = new JLabel("Ssn 입력: ");
	private JTextField Ssn_Delete = new JTextField(10);
	private JButton Delete_Button = new JButton("데이터 삭제");
	int count = 0;
	JPanel ComboBoxPanel = new JPanel();


	public Delete_Update() {

		String[] update = {"Address","Sex","Salary" }; // 8번 추가
		
		//연봉, 생일, 부하직원은 입력 칸을 만들어준다.

		Update = new JComboBox(update);
		

		Update.addActionListener(this);



		//ComboBoxPanel.add(Dept);

		JPanel CheckBoxPanel = new JPanel();
		CheckBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		CheckBoxPanel.add(Search_Button);
		
		JPanel InsertPanel = new JPanel();
		InsertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel CheckBoxInsertPanel = new JPanel();
		CheckBoxInsertPanel.setLayout(new BoxLayout(CheckBoxInsertPanel, BoxLayout.X_AXIS));
		CheckBoxInsertPanel.add(CheckBoxPanel);
		CheckBoxInsertPanel.add(InsertPanel);

		JPanel ShowSelectedPanel = new JPanel();
		ShowSelectedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		ShowSelectedEmp.setFont(new Font("Dialog", Font.BOLD, 16));
		dShow = "";

		ShowSelectedPanel.add(ShowSelectedEmp);



		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		UpdatePanel.add(Setlabel_Update);
		UpdatePanel.add(Ssn_Update);
		UpdatePanel.add(Update);
		UpdatePanel.add(update_Salary_Address_Sex);
		UpdatePanel.add(Update_Button);

		JPanel DeletePanel = new JPanel();
		DeletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		DeletePanel.add(Setlabel_Delete);
		DeletePanel.add(Ssn_Delete);
		DeletePanel.add(Delete_Button);

		JPanel Top = new JPanel();
		Top.setLayout(new BoxLayout(Top, BoxLayout.Y_AXIS));
		Top.add(ComboBoxPanel);
		Top.add(CheckBoxInsertPanel);
		
		JPanel Halfway = new JPanel();
		Halfway.setLayout(new BoxLayout(Halfway, BoxLayout.X_AXIS));
		Halfway.add(ShowSelectedPanel);

		JPanel Bottom = new JPanel();
		Bottom.setLayout(new BoxLayout(Bottom, BoxLayout.X_AXIS));
		Bottom.add(UpdatePanel);
		Bottom.add(DeletePanel);

		JPanel ShowVertical = new JPanel();
		ShowVertical.setLayout(new BoxLayout(ShowVertical, BoxLayout.Y_AXIS));
		ShowVertical.add(Halfway);
		ShowVertical.add(Bottom);

		add(Top, BorderLayout.NORTH);
		add(ShowVertical, BorderLayout.SOUTH);

		add(resultTable);
		Search_Button.addActionListener(this);
		Delete_Button.addActionListener(this);
		Update_Button.addActionListener(this);

		setTitle("Company Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1300, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		// DB연결
		try {
//			Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 연결

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

		// ------------------------------------------------------------------------ //
		if (e.getSource() == Search_Button) {
			try {
				s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				r = s.executeQuery("SELECT * FROM EMPLOYEE ");

				resultTable.setModel(new ResultSetTableModel(r));
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

		// DELETE
		if (e.getSource() == Delete_Button) {
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
					JOptionPane.showMessageDialog(null, "삭제 작업을 진행하시려면 NAME, SSN 항목을 모두 체크해주세요.");
				}

				ShowSelectedEmp.setText(" ");

			} catch (SQLException e1) {
				System.out.println("actionPerformed err : " + e1);
				e1.printStackTrace();
			}
			panel = new JPanel();
			ScPane = new JScrollPane(table);
			table.getModel().addTableModelListener(new CheckBoxModelListener());
			ScPane.setPreferredSize(new Dimension(1100, 400));
			panel.add(ScPane);
			add(panel, BorderLayout.CENTER);
			revalidate();

		} // DELETE 끝

		// UPDATE

		if (e.getSource() == Update_Button) {

			Vector<String> update_ssn = new Vector<String>();
			try {
				String columnName = model.getColumnName(2);

				System.out.println(columnName);
				if (columnName == "SSN") {
					if (Update.getSelectedItem().toString() == "Salary") {
						if (model.getColumnName(SALARY_COLUMN) == "SALARY") {
							for (int i = 0; i < table.getRowCount(); i++) {
								if (table.getValueAt(i, 0) == Boolean.TRUE) {
									String updateSalary = update_Salary_Address_Sex.getText();
									table.setValueAt(Double.parseDouble(updateSalary), i, SALARY_COLUMN);
									String updateStmt = "UPDATE EMPLOYEE SET Salary=? , modified=SYSDATE() WHERE Ssn=?";
									PreparedStatement p = conn.prepareStatement(updateStmt);
									p.clearParameters();
									p.setString(1, updateSalary);
									p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
									p.executeUpdate();
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "수정 작업을 위해 SALARY를 체크해주세요");
						}

					} else if (Update.getSelectedItem().toString() == "Sex") {
						if (model.getColumnName(SEX_COLUMN) == "SEX") {
							for (int i = 0; i < table.getRowCount(); i++) {
								if (table.getValueAt(i, 0) == Boolean.TRUE) {
									//update_ssn.add((String) table.getValueAt(i, 2));
									String updateSalary = update_Salary_Address_Sex.getText();
									table.setValueAt(updateSalary, i, SEX_COLUMN);
									String updateStmt = "UPDATE EMPLOYEE SET SEX=? , modified=SYSDATE() WHERE Ssn=?";
									PreparedStatement p = conn.prepareStatement(updateStmt);
									p.clearParameters();
									p.setString(1, String.valueOf(updateSalary));
									p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
									p.executeUpdate();
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "수정 작업을 위해 SEX를 체크해주세요");
						}

					} else if (Update.getSelectedItem().toString() == "Address") {
						if (model.getColumnName(ADDRESS_COLUMN) == "ADDRESS") {
							for (int i = 0; i < table.getRowCount(); i++) {
								if (table.getValueAt(i, 0) == Boolean.TRUE) {
									//update_ssn.add((String) table.getValueAt(i, 2));
									String updateSalary = update_Salary_Address_Sex.getText();
									table.setValueAt(updateSalary, i, ADDRESS_COLUMN);
									String updateStmt = "UPDATE EMPLOYEE SET Address=? , modified=SYSDATE() WHERE Ssn=?";
									PreparedStatement p = conn.prepareStatement(updateStmt);
									p.clearParameters();
									p.setString(1, String.valueOf(updateSalary));
									p.setString(2, String.valueOf((String) table.getValueAt(i, 2)));
									p.executeUpdate();
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "수정 작업을 위해 ADDRESS를 체크해주세요");
						}
					} else if (Update.getSelectedItem().toString() == "Dept_Salary_R") {// 부서별로 월급 일괄 수정_Research
						for (int i = 0; i < table.getRowCount(); i++) {
							if (table.getValueAt(i, 8).toString().equals("Research")) {
								String updateSalary = update_Salary_Address_Sex.getText();
								table.setValueAt(Double.parseDouble(updateSalary), i, SALARY_COLUMN);
								String ex = "Research";
								String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
								PreparedStatement p = conn.prepareStatement(updateStmt);
								p.clearParameters();
								p.setString(1, updateSalary);
								p.setString(2, ex);
								// System.out.println(updateSalary);
								p.executeUpdate();
							}
						}
					} else if (Update.getSelectedItem().toString() == "Dept_Salary_A") { // 부서별로 월급 일괄 수정_Administration
						for (int i = 0; i < table.getRowCount(); i++) {
							if (table.getValueAt(i, 8).toString().equals("Administration")) {
								String updateSalary = update_Salary_Address_Sex.getText();
								table.setValueAt(Double.parseDouble(updateSalary), i, SALARY_COLUMN);
								String ex = "Administration";
								String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
								PreparedStatement p = conn.prepareStatement(updateStmt);
								p.clearParameters();
								p.setString(1, updateSalary);
								p.setString(2, ex);
								// System.out.println(updateSalary);
								p.executeUpdate();

							}
						}
					} else if (Update.getSelectedItem().toString() == "Dept_Salary_H") { // 부서별로 월급 일괄 수정_Headquarters
						for (int i = 0; i < table.getRowCount(); i++) {
							if (table.getValueAt(i, 8).toString().equals("Headquarters")) {
								String updateSalary = update_Salary_Address_Sex.getText();
								table.setValueAt(Double.parseDouble(updateSalary), i, SALARY_COLUMN);
								String ex = "Headquarters";
								String updateStmt = "UPDATE EMPLOYEE JOIN DEPARTMENT ON Dno = Dnumber SET Salary=? , modified=SYSDATE() WHERE Dname=?";
								PreparedStatement p = conn.prepareStatement(updateStmt);
								p.clearParameters();
								p.setString(1, updateSalary);
								p.setString(2, ex);
								// System.out.println(updateSalary);
								p.executeUpdate();
							}
						}

					} else {
						JOptionPane.showMessageDialog(null, "수정 작업을 위해 Name과 SSN열을 체크해주시기 바랍니다.");
					}
				}
				ShowSelectedEmp.setText(" ");

			} catch (SQLException e1) {
				System.out.println("actionPerformed err : " + e1);
				e1.printStackTrace();
			}
			panel = new JPanel();
			ScPane = new JScrollPane(table);
			table.getModel().addTableModelListener(new CheckBoxModelListener());
			ScPane.setPreferredSize(new Dimension(1100, 400));
			panel.add(ScPane);
			add(panel, BorderLayout.CENTER);
			revalidate();
		} // UPDATE 끝
	}

	public class CheckBoxModelListener implements TableModelListener {
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int column = e.getColumn();
			if (column == BOOLEAN_COLUMN) {
				TableModel model = (TableModel) e.getSource();
				String columnName = model.getColumnName(1);
				Boolean checked = (Boolean) model.getValueAt(row, column);
				if (columnName == "NAME") {
					if (checked) {
						dShow = "";
						for (int i = 0; i < table.getRowCount(); i++) {
							if (table.getValueAt(i, 0) == Boolean.TRUE) {
								dShow += (String) table.getValueAt(i, NAME_COLUMN) + "    ";

							}
						}
						ShowSelectedEmp.setText(dShow);
					} else {
						dShow = "";
						for (int i = 0; i < table.getRowCount(); i++) {
							if (table.getValueAt(i, 0) == Boolean.TRUE) {
								dShow += (String) table.getValueAt(i, 1) + "    ";
							}
						}
						ShowSelectedEmp.setText(dShow);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new Delete_Update();
	}
}