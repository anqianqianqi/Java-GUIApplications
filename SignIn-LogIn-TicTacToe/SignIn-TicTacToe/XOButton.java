package GUI.SignInPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XOButton extends JButton implements ActionListener {
    ImageIcon X,O;
    byte value = 0;
    public XOButton(){
        X = new ImageIcon(this.getClass().getResource("avocado.png"));
        O = new ImageIcon(this.getClass().getResource("peach.png"));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        value++;
        value %= 3; //if it is more than include 3, keep subtract 3 from it
        switch (value) {
            case 0:
                setIcon(null);
                break; // if you dont break it will keep moving down
            case 1:
                setIcon(X);
                break;
            case 2:
                setIcon(O);
                break;
        }
    }
}
