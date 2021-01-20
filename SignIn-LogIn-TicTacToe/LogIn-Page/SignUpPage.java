package GUI.LoginPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import static GUI.LoginPage.GUISetUp.*;

public class SignUpPage {
    static  JFrame frameSignUp = new JFrame("Sign Up");
    static JPasswordField confirmPasswordText = new JPasswordField(20);

    static void placeSinUpButtomWhenSignUp(JPanel panel) throws IOException {
        JButton signupButtonWhenSignUp = new JButton("Sign Up");
        panel.add(signupButtonWhenSignUp);

        signUpInput signupInput = new signUpInput();
        signupButtonWhenSignUp.addActionListener(signupInput);
    }

    static void placeLoginButtomWhenSignUp(JPanel panel){
        JButton loginButtonWhenSignUp = new JButton("Back to login");
        loginButtonWhenSignUp.setBounds(180, 80, 80, 25);
        panel.add(loginButtonWhenSignUp);

        loginEventWhenSignUp loginWhenSignUp = new loginEventWhenSignUp();
        loginButtonWhenSignUp.addActionListener(loginWhenSignUp);
    }

    static void placeConfirmPassword(JPanel panel){
        JLabel confirmPasswordLabel = new JLabel("Confirm");
        confirmPasswordLabel.setBounds(10,80,80,25);
        panel.add(confirmPasswordLabel);

        confirmPasswordText.setBounds(100,80,50,25);
        panel.add(confirmPasswordText);
    }

    static void signUpFrame() throws IOException {
        frameSignUp.setSize(800, 800);
        setBackgronudImage(frameSignUp);
        frameSignUp.setLayout(new GridBagLayout());
        frameSignUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel signUpPanel = new JPanel();
        signUpPanel.setBorder(BorderFactory.createTitledBorder("Sign up"));
        signUpPanel.setPreferredSize(new Dimension(320,250));
        frameSignUp.add(signUpPanel);
        placeButtoms(signUpPanel);
        placeConfirmPassword(signUpPanel);
        placeSinUpButtomWhenSignUp(signUpPanel);
        placeLoginButtomWhenSignUp(signUpPanel);
        frameSignUp.setVisible(true);
    }

}
