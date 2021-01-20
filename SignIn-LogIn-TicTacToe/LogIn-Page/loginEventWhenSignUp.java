package GUI.LoginPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static GUI.LoginPage.GUISetUp.loginFrame;

public class loginEventWhenSignUp implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            loginFrame();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
