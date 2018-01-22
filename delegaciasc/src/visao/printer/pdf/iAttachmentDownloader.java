/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visao.printer.pdf;

/**
 *
 * @author HP
 */
public interface iAttachmentDownloader {
    public void downloadAttachment(String sourceURL,String targetPath,String userName,String password);
}
