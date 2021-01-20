package GUI.SignInPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import static GUI.LoginPage.GUISetUp.setBackgronudImage;

public class SignInPage{
    static XOButton[] buttons = new XOButton[9];

    public static void signInframe() throws IOException {
        JFrame frameSignIn = new JFrame("Tic Tac Toe");
        setBackgronudImage(frameSignIn);
        frameSignIn.setSize(800,800);
        frameSignIn.setLayout(new GridBagLayout());
        frameSignIn.setBackground(Color.BLACK);
        frameSignIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3,3));
        panel.setPreferredSize(new Dimension(600,600));

        //Tic Tac Toe Buttons
        for (int i=0; i<9; i++){
            buttons[i]  = new XOButton();
            panel.add(buttons[i]);
        }

        frameSignIn.add(panel);
        frameSignIn.setVisible(true);
    }

}
