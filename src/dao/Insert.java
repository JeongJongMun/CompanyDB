package dao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Insert extends JFrame {
	JButton button = new JButton("정보 추가하기");
	
	JLabel set_FirstName = new JLabel("First Name :\t");
	JTextField text_FirstName = new JTextField(10);
	
	JLabel set_MiddleInit = new JLabel("Middle Init :\t");
	JTextField text_MiddleInit = new JTextField(10);
	
	JLabel set_LastName = new JLabel("Last Name :\t");
	JTextField text_LastName = new JTextField(10);
	
	JLabel set_Ssn = new JLabel("Ssn :\t\t\t\t\t\t\t\t\t\t\t\t");
	JTextField text_Ssn = new JTextField(10);
	
	JLabel set_Birthdate = new JLabel("Birthdate :\t\t\t");
	JTextField text_Birthdate = new JTextField(10);
	
	JLabel set_Address = new JLabel("Address :\t\t\t\t");
	JTextField text_Address = new JTextField(10);
	
	JLabel set_Sex = new JLabel("Sex :\t\t\t\t\t\t\t\t\t\t\t\t\t ");
	String[] sex = {"F","M"};
	JComboBox box_Sex = new JComboBox(sex);
	
	JLabel set_Salary = new JLabel("Salary : \t\t\t\t\t\t\t");
	JTextField text_Salary = new JTextField(10);
	
	JLabel set_Super_ssn = new JLabel("Super_ssn :\t");
	JTextField text_Super_ssn = new JTextField(10);
	
	JLabel set_Dno = new JLabel("Dno : \t\t\t\t\t\t\t\t\t");
	JTextField text_Dno = new JTextField(10);
	
	public static void main(String[] args) {
		new Insert();
	}
	

	public Insert() {
		JPanel FirstName = new JPanel();
		FirstName.setLayout(new FlowLayout(FlowLayout.LEFT));
		FirstName.add(set_FirstName);
		FirstName.add(text_FirstName);
		
		JPanel MiddleInit = new JPanel();
		MiddleInit.setLayout(new FlowLayout(FlowLayout.LEFT));
		MiddleInit.add(set_MiddleInit);
		MiddleInit.add(text_MiddleInit);
		
		JPanel LastName = new JPanel();
		LastName.setLayout(new FlowLayout(FlowLayout.LEFT));
		LastName.add(set_LastName);
		LastName.add(text_LastName);
		
		JPanel Ssn = new JPanel();
		Ssn.setLayout(new FlowLayout(FlowLayout.LEFT));
		Ssn.add(set_Ssn);
		Ssn.add(text_Ssn);
		
		JPanel Birthdate = new JPanel();
		Birthdate.setLayout(new FlowLayout(FlowLayout.LEFT));
		Birthdate.add(set_Birthdate);
		Birthdate.add(text_Birthdate);
		
		JPanel Address = new JPanel();
		Address.setLayout(new FlowLayout(FlowLayout.LEFT));
		Address.add(set_Address);
		Address.add(text_Address);
		
		JPanel Sex = new JPanel();
		Sex.setLayout(new FlowLayout(FlowLayout.LEFT));
		Sex.add(set_Sex);
		Sex.add(box_Sex);
		
		JPanel Salary = new JPanel();
		Salary.setLayout(new FlowLayout(FlowLayout.LEFT));
		Salary.add(set_Salary);
		Salary.add(text_Salary);
		
		JPanel Super_ssn = new JPanel();
		Super_ssn.setLayout(new FlowLayout(FlowLayout.LEFT));
		Super_ssn.add(set_Super_ssn);
		Super_ssn.add(text_Super_ssn);
		
		JPanel Dno = new JPanel();
		Dno.setLayout(new FlowLayout(FlowLayout.LEFT));
		Dno.add(set_Dno);
		Dno.add(text_Dno);
		
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		button_panel.add(button);
		
		
		JPanel Top = new JPanel();
		Top.setLayout(new BoxLayout(Top, BoxLayout.Y_AXIS));
		Top.add(FirstName);
		Top.add(MiddleInit);
		Top.add(LastName);
		Top.add(Ssn);
		Top.add(Birthdate);
		Top.add(Address);
		Top.add(Sex);
		Top.add(Salary);
		Top.add(Super_ssn);
		Top.add(Dno);
		Top.add(button_panel);
		
		
		add(Top, BorderLayout.CENTER);
		
		setTitle("직원 추가");
		setSize(300, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
