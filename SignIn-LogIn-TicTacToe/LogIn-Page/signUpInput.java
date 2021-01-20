package GUI.LoginPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static GUI.LoginPage.GUISetUp.*;
import static GUI.LoginPage.SignUpPage.*;

public class signUpInput implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String newUserName = userText.getText();
        String newPassword = passwordText.getText();
        String confirmPassword = confirmPasswordText.getText();

        int i = 0;
        for (String key : loginInformation.keySet()){
            if (newUserName.equals(key)){
                JOptionPane.showMessageDialog(null, "Username already exist. Please try another one");
                i++;
                break;
            }
        }
        if (i ==0){
            if (newPassword.equals(confirmPassword)){
                if (newPassword.equals("")){
                    JOptionPane.showMessageDialog(null,"Password cannot be null");
                }else{
                    JOptionPane.showMessageDialog(null,"Account created! Return to Login");
                    loginInformation.put(newUserName,newPassword);
                    try {
                        writeLoginInformation.writeLogin();
                        System.out.println(loginInformation);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        loginFrame();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println(ex);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Passwords do not match");
            }
        }
        userText.setText("");
        passwordText.setText("");
        confirmPasswordText.setText("");
    }
}
