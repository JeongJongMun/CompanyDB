package view;

import dao.Delete_Update;
import dao.Search_Insert;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMaster extends JFrame implements ActionListener {
    private JButton Search_Insert_Button = new JButton("검색/추가");
    private JButton Delete_Update_Button = new JButton("삭제/수정");
    public Search_Insert search_insert;
    public Delete_Update delete_update;


    public MainMaster() {

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(Search_Insert_Button);
        mainPanel.add(Delete_Update_Button);

        Search_Insert_Button.addActionListener(this);
        Delete_Update_Button.addActionListener(this);

        Search_Insert_Button.setPreferredSize(new Dimension(600, 500)); // 크기 조정
        Delete_Update_Button.setPreferredSize(new Dimension(600, 500)); // 크기 조정

        Search_Insert_Button.setFont(new Font("Dialog", Font.BOLD, 25));
        Delete_Update_Button.setFont(new Font("Dialog", Font.BOLD, 25));

        add(mainPanel);

        setTitle("CompanyDB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == Search_Insert_Button) {
            search_insert = new Search_Insert();
        } else if (e.getSource() == Delete_Update_Button) {
            delete_update = new Delete_Update();
        }
    }

    public static void main(String[] args) {
        new MainMaster();
    }
}
