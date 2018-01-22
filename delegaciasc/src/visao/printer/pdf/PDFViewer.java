/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.printer.pdf;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import visao.JPanelObreiro;

/**
 *
 * @author Rafael
 */
public class PDFViewer {
    public static void show(File f){
         String command = "start \"\" /max "+"\""+f.getAbsolutePath()+"\"";//just write file name on cmd console that it will opens on windows
        try {
             Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",command});
         } catch (IOException ex) {
             Logger.getLogger(JPanelObreiro.class.getName()).log(Level.SEVERE, null, ex);
         }  
    }
}
