package GUI.LoginPage;

import GUI.SignInPage.SignInPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static GUI.LoginPage.GUISetUp.*;
import static GUI.LoginPage.SignUpPage.signUpFrame;

public class loginEvent implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String userName = userText.getText();
        String password = passwordText.getText();
        int i = 0;
        for (String key : loginInformation.keySet()) {
            if (userName.equals(key)) {
                i++;
                if (password.equals(loginInformation.get(key))) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    try {
                        SignInPage.signInframe();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username or password incorrect.");
                }
                break;
            }
        }
        if (i == 0) {
            JOptionPane.showMessageDialog(null, "Username does not exist. Go sign up!");
            try {
                signUpFrame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        userText.setText("");
        passwordText.setText("");
    }
}
