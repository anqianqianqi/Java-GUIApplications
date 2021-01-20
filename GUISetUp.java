package GUI.LoginPage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GUISetUp {
    static JFrame frameLogin = new JFrame("Login");
    static JTextField userText = new JTextField(20);
    static JPasswordField passwordText = new JPasswordField(20);
    static HashMap<String, String> loginInformation = new HashMap<>();
    static File file = new File("/Users/anqiluo/IdeaProjects/java/src/GUI/LoginPage/loginInformation.txt");

    static void placeButtoms(JPanel panel) {
        JLabel userLabel = new JLabel("User name");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        userText.setBounds(100,20,50,25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        passwordText.setBounds(100,50,50,25);
        panel.add(passwordText);
    }

    static void placeLoginButtom(JPanel panel){
        JButton loginButton = new JButton("login");
        loginButton.setBounds(180, 80, 80, 25);
        panel.add(loginButton);

        loginEvent login = new loginEvent();
        loginButton.addActionListener(login);
    }

    static void placeSinUpButtom(JPanel panel){
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(100, 80, 80, 25);
        panel.add(signupButton);

        signUpEvent signup = new signUpEvent();
        signupButton.addActionListener(signup);
    }

    public static void setBackgronudImage(JFrame frame)throws IOException {
        final Image backgroundImage = javax.imageio.ImageIO.read(new File("/Users/anqiluo/IdeaProjects/java/src/GUI/LoginPage/Blurred.jpg"));
        frame.setContentPane(new JPanel(new BorderLayout()) {
            @Override public void paintComponent(Graphics g) {
                g.drawImage(backgroundImage, 0, 0, null);
            }
        });
    }

    static void loginFrame() throws IOException {
        frameLogin.setSize(800, 800);
        setBackgronudImage(frameLogin);
        frameLogin.setLayout(new GridBagLayout());
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(330,180)); //330,180
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        frameLogin.add(loginPanel);
        placeButtoms(loginPanel);
        placeSinUpButtom(loginPanel);
        placeLoginButtom(loginPanel);
        frameLogin.setVisible(true);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        loginFrame();
        readLoginInformation.readLogin();
        System.out.println("Existing login information");
        System.out.println(loginInformation);
    }
}


