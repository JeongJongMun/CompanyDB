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

	private JButton Search_Button = new JButton("검색");

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

		String[] update = {"Address", "Sex", "Salary"};

		Update = new JComboBox(update);

		Update.addActionListener(this);

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
		setSize(1300, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		// DB연결
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
			String Ssn = Ssn_Delete.getText();
			if(isStringEmpty(Ssn)){
				JOptionPane.showMessageDialog(null, "공백 없이 작성해주세요");
			}else{
				try{
					String sql = "DELETE FROM EMPLOYEE" +  " WHERE Ssn = " + Ssn + ";";
					System.out.print(sql);
					Statement s = conn.createStatement();
					int result = s.executeUpdate(sql);
					if (result == 0) {
						JOptionPane.showMessageDialog(null, "삭제 실패");
					} else {
						JOptionPane.showMessageDialog(null, "삭제 성공");
					}
				}catch (Exception e1){
					JOptionPane.showMessageDialog(null, "형식에 맞게 입력하세요");
				}
			}
		}
		// UPDATE

		if (e.getSource() == Update_Button) {
			String Ssn = Ssn_Update.getText();
			String Att = update_Salary_Address_Sex.getText();
			String Col = Update.getSelectedItem().toString();
			if(isStringEmpty(Ssn) || isStringEmpty(Att)){
				JOptionPane.showMessageDialog(null, "공백 없이 작성해주세요");
			}else {
				try {
						String sql = "UPDATE Employee set " + Col + "=" + "\"" + Att + "\"" + " WHERE Ssn =" + Ssn + ";";
						System.out.print(sql);
						Statement s = conn.createStatement();
						int result = s.executeUpdate(sql);
						if (result == 0) {
							JOptionPane.showMessageDialog(null, "업데이트 실패");
						} else {
							JOptionPane.showMessageDialog(null, "업데이트 성공");
						}
					} catch(Exception e1){
						JOptionPane.showMessageDialog(null, "형식에 맞게 입력하세요");
					}
				}
			}
	}
	static boolean isStringEmpty(String str) {
		return str == null || str.isEmpty();
	}
	public static void main(String[] args) {
		new Delete_Update();
	}
}