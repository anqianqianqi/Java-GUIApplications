package GUI.LoginPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import static GUI.LoginPage.GUISetUp.*;

public class writeLoginInformation {
    static void  writeLogin() throws IOException {
        FileOutputStream f = new FileOutputStream(GUISetUp.file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(loginInformation);
        s.close();
    }
}
