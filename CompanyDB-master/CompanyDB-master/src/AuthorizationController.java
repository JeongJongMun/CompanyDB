import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationController {
    public static String masterId = "master";
    public static String masterPw = "1234";
    private static JFrame loginFrame;
    private static JFrame signUpFrame;
    private static Map<String, String> userDatabase = new HashMap<>();

    public static void main(String[] args) {
        // 로그인 화면 생성
        loginFrame = createLoginFrame();
        // 회원가입 화면 생성
        signUpFrame = createSignUpFrame();
    }
    private static JFrame createLoginFrame() {
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("User ID ");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("User PW ");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputId = usernameField.getText();
                String inputPw = new String(passwordField.getPassword());

                if (userDatabase.containsKey(inputId) && userDatabase.get(inputId).equals(inputPw)) {
                    JOptionPane.showMessageDialog(frame, "Login Success!\n User Type : General Employee");
                    // General Employee 기능 보여주기
                    new MainMaster();
                } else if (inputId.equals(masterId) && inputPw.equals(masterPw)){
                    JOptionPane.showMessageDialog(frame, "Login Success!\n User Type : Master Employee");
                    // Master Employee 기능 보여주기
                    new MainMaster();
                } else {
                    JOptionPane.showMessageDialog(frame, "로그인 실패. 다시 시도하세요.");
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpFrame.setVisible(true);
                frame.setVisible(false);
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }

    private static JFrame createSignUpFrame() {
        JFrame frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("User ID ");
        JTextField nameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("User PW ");
        JPasswordField passwordField = new JPasswordField(20);

        JButton signUpButton = new JButton("Sign Up");

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nameField.getText();
                String password = new String(passwordField.getPassword());
                if(!(username.isEmpty() && password.isEmpty())) {
                    userDatabase.put(username, password);
                    JOptionPane.showMessageDialog(frame, "Sign Up Success!");
                    signUpFrame.setVisible(false);
                    loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "User ID and User PW should not be null");
                }
            }
        });
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signUpButton);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}