/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.IOManagement;
import java.text.NumberFormat;
/**
 *
 * @author RafaelQG
 */
public class NumericManager {
    
    public String getCurrencyFormat(double value){
        return NumberFormat.getCurrencyInstance().format(value);
    }
    
     public double getCurrencyField(String text) {
        while (text.contains(".")) {
            text = text.replace(".", "");
        }
        text = text.replace(",", ".");
        double result = 0;
        try {
            result = Double.parseDouble(text);
        } catch (Exception e) {
        }
        return result;
    }

         
    public int getIntField(String text) {
        int result = -1;
        try {
            result = Integer.parseInt(text);
        } catch (Exception e) {
        }
        return result;
    }
    
     public int getIntField(Object obj) {
        String txt="";
         if(obj!=null){
            txt=obj.toString();
        }
        return getIntField(txt);
    }

    public double getDoubleField(String text) {
        double result = 0;
        try {
            result = Double.parseDouble(text);
        } catch (Exception e) {
        }
        return result;
    }
    
     public double getDoubleField(Object obj) {
         String text="";
         if (obj!=null){
             text=obj.toString();
        }
        return getDoubleField(text);
    }
}
