package visao.printer.pdf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import visao.printer.pdf.ImageManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import sun.misc.BASE64Encoder;


//imports formatacao
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PDFGenerator implements iPDFGenerator {

    private static int numberOfImagesCorrected = 0;
    private Vector<File> tempFilesCreated = new Vector<File>();

    public void createPDF(String sourceURL, String targetPath, String userName, String password, String header, String footer,String bottommargin,String footerspacing){
        execute(sourceURL, targetPath, userName, password,bottommargin);
        int intFooterSpacing=0;
        try{intFooterSpacing=Integer.valueOf(footerspacing);}catch(Exception e){intFooterSpacing=50;}
        addHeaderAndFooter(header, footer, targetPath,intFooterSpacing);
    }

    private void execute(String url, String pdfFileName, String userName, String password,String bottommargin) {
        try {
            // get the html code
            String filecontent = getPageHTMLCode(url, userName, password, pdfFileName,bottommargin);
            writeFile("C:\\result.html", filecontent);
            //convert the html to pdf and save it in a file.
            OutputStream os = new FileOutputStream(pdfFileName);
            convert(filecontent, os);
            os.close();
            this.deleteTempFiles();
        } catch (Exception e) {
            System.out.println("Error on execute function: " + e.getMessage());
        }
    }
    
    @Override
    public void createPDFBasedonHTMLCode(String HTMLCode, String targetPath) {
        try{
        //convert the html to pdf and save it in a file.
        OutputStream os = new FileOutputStream(targetPath);
        convert(HTMLCode, os);
        os.close();
        this.deleteTempFiles();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    
    private String getPageHTMLCode(String urltext, String userName, String password, String targetPath,String bottommargin) {
        String out = "";
        try {
            //create a authentication object
            BEASAuthenticator auth = new BEASAuthenticator(userName, password);
            Authenticator.setDefault(auth);
            //call function to accept auto sign certificates
            acceptSSL();
            //create a conection with the server
            System.out.println("Java:HTTPS: Trying to establish connection with Remote Server");
            URL url = new URL(urltext);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setDoOutput(true);
            huc.setRequestMethod("POST");
            huc.setRequestProperty("UserName", userName);
            huc.setRequestProperty("Password", password);
            BASE64Encoder encoder = new BASE64Encoder();
            huc.setRequestProperty("Authorization", "Basic " + encoder.encode((userName + ":" + password).getBytes()));
            huc.setRequestProperty("Host", url.getHost());
            huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            huc.setRequestProperty("Accept-charset","utf-8");


            //read the html code
            BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                out = out + line;
            }
            //solve relative paths
            out = solveRelativePath(out, url.getHost(), url.getProtocol());
            //solve imagens pasted inside of the rich text
            out = solveFakeImagens(out, targetPath, userName, password, "?OpenElement&amp;FieldElemFormat=gif");
            //solve images resources
            System.out.println("Downloding Image Resources:");
            out = solveFakeImagens(out, targetPath, userName, password, "?OpenImageResource");
            out=setUTF8(out);
            out=setBorderBottom(out,bottommargin);
            return out;
        } catch (Exception e) {
            System.out.println("Error getPageHTMLCode function: " + e.getMessage());
        }
        return out;
    }

    private String setUTF8(String content){
        String[] contentParts=content.split("<head>");
        return contentParts[0]+"<head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>"+contentParts[1];
    }

    private String setBorderBottom(String content, String cm){
        String[] contentParts=content.split("</head>");
        //return contentParts[0]+"<style>@page {margin-bottom:"+cm+"cm } @media print{ table {page-break-inside:avoid}}</style></head>"+contentParts[1];
        return contentParts[0]+"<style>@page {margin-bottom:"+cm+"cm; margin-top:2cm }</style></head>"+contentParts[1];

    }

    public void addHeaderAndFooter(String header, String footer, String filePath,int footerSpacing) {
        try {
            // we create a PdfReader object
            PdfReader reader = new PdfReader(filePath);
            String temp = filePath.substring(0, filePath.length() - 4) + "TEMP.pdf";
            System.out.println(temp); 
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(temp));

            // we create a Font for the text to add
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            // these are the canvases we are going to use
            PdfContentByte under, over;
            int total = reader.getNumberOfPages() + 1;
            Rectangle r = reader.getPageSize(1);

            for (int i = 1; i < total; i++) {
                // set footer on the existing page

                over = stamper.getOverContent(i);
                over.beginText();
                over.setFontAndSize(bf, 7);
                //over.setTextMatrix(30, 20);
                String[] footerLines=footer.split("<br>");
                if(footerLines.length==1){
                   over.showTextAligned(PdfContentByte.ALIGN_LEFT, footerLines[0], 0+50, footerSpacing, 0);
                }else{
                   over.showTextAligned(PdfContentByte.ALIGN_LEFT, footerLines[0], 0+50, footerSpacing, 0);
                   over.showTextAligned(PdfContentByte.ALIGN_LEFT, footerLines[1], 0+50, footerSpacing-10, 0);
                }
                
                
                //page number
                over.showTextAligned(PdfContentByte.ALIGN_RIGHT, String.valueOf(i), r.getWidth()-50, 50, 0);

                //over.showText(footer);
                over.endText();
                over.stroke();
            }

            for (int i = 1; i < total; i++) {
                // set header on the existing page
                under = stamper.getOverContent(i);//stamper.getUnderContent(i);
                under.beginText();
                under.setFontAndSize(bf, 7);
                //under.setTextMatrix(30, 815);
                //aling_center =r.getWidth() / 2;
                under.showTextAligned(PdfContentByte.ALIGN_CENTER, header, r.getWidth()-50, 815, 0);
                under.showText(header);
                under.stroke();
            }
            stamper.close();
            //delete the original file and rename the temp file to the original name
            File f = new File(filePath);
            f.delete();
            File f2 = new File(temp);
            f2.renameTo(f);

        } catch (Exception e) {
            System.out.println("Header and Footer:" + e.getMessage());
        }

    }

    private String solveRelativePath(String content, String source, String protocol) {
        content = content.replaceAll("src=" + (char) 34 + "/", "src=" + (char) 34 + protocol + "://" + source + "/");
        //here could be done more programming depending of the relatives paths
        return content;
    }

    private String solveFakeImagens(String content, String targetPath, String login, String password, String fakeImagesKey) throws IOException {
        boolean hasImageToSolve = true;
        int indexEnd = 0;
        int indexBegin = 0;
        String fakeImagePath = "";
        String trueImagePath = targetPath.substring(0, targetPath.lastIndexOf("\\") + 1);
        String baseFileName = targetPath.substring(targetPath.lastIndexOf("\\") + 1, targetPath.lastIndexOf("."));

        while (hasImageToSolve) {
            if (content.contains(fakeImagesKey)) {
                indexEnd = content.indexOf(fakeImagesKey);
                indexEnd = indexEnd + fakeImagesKey.length();
                //find the begin of the src
                indexBegin = findIndexOfSrc(content, indexEnd);
                if (indexBegin != -1) {
                    fakeImagePath = content.substring(indexBegin, indexEnd);

                    //create a local copy
                    numberOfImagesCorrected++;
                    System.out.println("Download URL:" + fakeImagePath);
                    new BEASAttachmentDownloader().downloadAttachment(fakeImagePath, trueImagePath + baseFileName + numberOfImagesCorrected + ".gif", login, password);
                    System.out.println("Downloaded to:" + trueImagePath + baseFileName + numberOfImagesCorrected + ".gif");
                    // create a new image in PNG format
                    File f = new File(trueImagePath + baseFileName + numberOfImagesCorrected + ".gif");
                    this.tempFilesCreated.add(f);
                    BufferedImage imagem = ImageIO.read(f);
                    if (imagem != null) {
                        f = new File(trueImagePath + baseFileName + numberOfImagesCorrected + ".png");
                        //solve image width
                        int width=imagem.getWidth();
                        int height=imagem.getHeight();
                        while(width>650){
                            width=(int)(width -(width*0.2));
                            height=(int)(height -(height*0.2));
                        }
                        imagem= new ImageManager().resizeTrick(imagem,width ,height);

                        ImageIO.write(imagem, "PNG", f);
                        this.tempFilesCreated.add(f);
                    }
                        //update the path to reference the image local copy
                        content = content.replace(content.substring(indexBegin, indexEnd), "file:///" + trueImagePath + baseFileName + numberOfImagesCorrected + ".png");
                    
                        //solve image width
                        try {
                            String imgKey = baseFileName + numberOfImagesCorrected + ".png";
                            imgKey = imgKey + (char) 34 + " width=" + (char) 34;
                            indexBegin = content.indexOf(imgKey) + imgKey.length();
                            int indexNextQuotes= content.indexOf(34,indexBegin+1);
                            String widthStr = content.substring(indexBegin, indexNextQuotes);
                            System.out.println("WIDTH:" + widthStr);
                            if (Integer.valueOf(widthStr.trim()) > 650) {
                                content = content.replace("width=" + (char) 34 + widthStr, "width=" + (char) 34 + "650");
                            }
                        } catch (Exception e) {}                                           
                }
            } else {
                hasImageToSolve = false;
            }
        }
        return content;
    }

    private int findIndexOfSrc(String content, int indexKey) {
        boolean found = false;
        int i = indexKey - 20;//this -20 is because the string always will be bigger than that.
        String testingValue = "";
        while (!found || i > 0) {
            testingValue = content.substring(i, i + 4);
            if (testingValue.contains("src=")) {
                found = true;
                return i + 5;
            }
            i--;
        }
        return -1;
    }

    private void writeFile(String filename, String filecontent) {
        FileOutputStream fout = null;
        try {
            File f = new File(filename);
            fout = new FileOutputStream(f);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.writeBytes(filecontent);
            dout.close();
            fout.close();
            this.tempFilesCreated.add(f);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void convert(String input, OutputStream out) throws DocumentException {
        // converts the html "input" into a pdf "output"
        convertWithFontSolver(new ByteArrayInputStream(input.getBytes()), out);
    }

    private void convertWithFontSolver(InputStream input, OutputStream out) throws DocumentException {
        try {
            ByteArrayOutputStream tidyOut = new ByteArrayOutputStream(); //we need this later
//  html->xhtml conversion
            Tidy tidy = new Tidy();
            tidy.setQuiet(true);//comment line when testing this method
            tidy.setXHTML(true);        
            tidy.setInputEncoding("UTF-8");
            tidy.setOutputEncoding("utf-8");
            tidy.parse(input, tidyOut);
//  now tidied xhtml can be parsed
            InputStream tidyIn = new ByteArrayInputStream(tidyOut.toByteArray());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

//  turn off external dtd loading, we don't need it
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
            dbf.setFeature("http://xml.org/sax/features/validation", Boolean.FALSE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(tidyIn);

//  to add custom font we need to specify it in style
            //Element style = doc.createElement("style");
            //style.setTextContent("body { font-family:arial; }");
//  and add it to <head>
            //Element root = doc.getDocumentElement();
            //root.getElementsByTagName("head").item(0).appendChild(style);
//  we've got document, now let's render it
            ITextRenderer renderer = new ITextRenderer();
//  embed arial unicode in document
            ITextFontResolver fr = renderer.getFontResolver();

            try{
            fr.addFontDirectory("C:\\Windows\\Fonts\\", true);
            }catch(Exception w){
                fr.addFont("C:\\Windows\\Fonts\\arial.ttf", true);
                fr.addFont("C:\\Windows\\Fonts\\arialbd.ttf", true);
                fr.addFont("C:\\Windows\\Fonts\\ariali.ttf", true);
                fr.addFont("C:\\Windows\\Fonts\\arialbi.ttf", true);
            }
            renderer.setDocument(doc, null);
//  do the layout
            renderer.layout();
//  and finally pdf creation
            renderer.createPDF(out);
        } catch (Exception e) {
            System.out.println("Error convert: " + e.getMessage());
        }
    }


    private void deleteTempFiles() {
        for (File f : tempFilesCreated) {
            f.delete();
        }
    }

    private void acceptSSL() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
    }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}

