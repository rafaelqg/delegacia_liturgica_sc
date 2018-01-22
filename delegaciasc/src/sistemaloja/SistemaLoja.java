/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaloja;

import visao.JFrameLoadingInitial;
import visao.JFramePrincipal;

/**
 *
 * @author Rafael
 */
public class SistemaLoja {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
       //apresenta janela de loading
       JFrameLoadingInitial frameInitial= new JFrameLoadingInitial();
       frameInitial.setVisible(true);
       JFramePrincipal frame= new JFramePrincipal();
       frameInitial.setVisible(false);
       frame.setVisible(true);
    }
}
