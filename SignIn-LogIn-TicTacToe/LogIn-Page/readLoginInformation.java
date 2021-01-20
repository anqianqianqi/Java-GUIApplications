package GUI.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class readLoginInformation extends HashMap<String, String> {

    static void readLogin() throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(GUISetUp.file);
        ObjectInputStream s = null;
        s = new ObjectInputStream(f);
        GUISetUp.loginInformation = (HashMap<String, String>) s.readObject();
        s.close();
    }

}
