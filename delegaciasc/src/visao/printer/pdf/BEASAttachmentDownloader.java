/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visao.printer.pdf;


import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import sun.misc.BASE64Encoder;

/**
 *
 * @author HP
 */

public class BEASAttachmentDownloader implements iAttachmentDownloader {

    public void downloadAttachment(String sourceURL, String targetPath, String userName, String password) {
        execute(sourceURL, targetPath,userName,password);
    }

      private void execute(String urltext, String targetPath,String userName, String password) {
        String out = "";
        try {
            //create a authentication object
            BEASAuthenticator auth = new BEASAuthenticator(userName, password);
            Authenticator.setDefault(auth);
            // call function to accept auto sign certificates
            acceptSSL();
            //create a conection with the server
            System.out.println("Java:HTTPS: Trying to establish connection with Remote Server");
            URL url = new URL(urltext.replaceAll(" ","%20"));
            System.out.println("Absolute URL "+ url.getFile());
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();


            huc.setDoOutput(true);
            huc.setRequestMethod("GET");
            huc.setRequestProperty("UserName", userName);
            huc.setRequestProperty("Password", password);
            BASE64Encoder encoder = new BASE64Encoder();
            huc.setRequestProperty("Authorization", "Basic " + encoder.encode((userName + ":" + password).getBytes()));
            huc.setRequestProperty("Host", url.getHost());
            huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //read file content
            InputStream stream = huc.getInputStream();
            RandomAccessFile file = new RandomAccessFile(targetPath, "rw");
            byte[] buffer;
            int read = 0;
            while (read != -1) {
                buffer = new byte[1];
                read = stream.read(buffer);
                if (read != -1) {
                    file.write(buffer);
                }
            }
            file.close();
            stream.close();
        } catch (Exception e) {
            System.out.println("Error getFile Content function: " + e.getMessage());
        }
    }

   


    private void acceptSSL() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }

}

