package GUI.LoginPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static GUI.LoginPage.SignUpPage.signUpFrame;

public class signUpEvent implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        try {
            signUpFrame();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
