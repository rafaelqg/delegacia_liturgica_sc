/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visao.printer.pdf;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
/**
 *
 * @author HP
 */
public class BEASAuthenticator extends Authenticator {
private String username, password;

    public BEASAuthenticator(String user, String pass) {
      username = user;
      password = pass;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }


}
