package visao.printer.pdf;
/**
 *
 * @author Rafael Queiroz Gonçalves
 */
public interface iPDFGenerator {
    public void createPDF(String sourceURL,String targetPath,String userName,String password, String header, String footer,String bottommargin, String footerspacing);
    public void createPDFBasedonHTMLCode(String HTMLCode,String targetPath);
}
