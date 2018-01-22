/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.IOManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author RafaelQG
 */
public class DateManager {

    /**
     * Inform if the date is valid or not, considering not just the format but
     * confirms the existing of informed date.
     *
     * @param dateTxt: a date in format dd/MM/yyyy
     * @return true if it is a valid date.
     */
    public boolean isValidDate(String dateTxt) {

        boolean isValid = false;
        if (dateTxt != null && !dateTxt.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = sdf.parse(dateTxt);
                isValid = sdf.format(date).equals(dateTxt); //após o parse, caso a data esteja errada, ela é aproximada para data mais próxima, logo difere do texto de entrada, e consta-se que a data é inválida
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                isValid = false;
            }
        }
        return isValid;
    }

    
    
    
    /**
     * @param dt
     * @return a string containing the date formated dd/MM/YYYY
     */
    public String setDateField(Date dt) {
        String result = null;
        if (dt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            result = sdf.format(dt);
        }else{
            result="";
        }
        return result;
    }

    public String cleanLeftZero(String number) {
        String result = number;
        if (number.charAt(0) == '0') {
            result = String.valueOf(number.charAt(1));
        }
        return result;
    }

    public Date getDateField(String text) {
        Date result = null;
        if (this.isValidDate(text)) {
            try {
                String parts[] = text.split("/");
                if (parts.length == 3) {
                    int day = Integer.parseInt(cleanLeftZero(parts[0]));
                    int month = Integer.parseInt(cleanLeftZero(parts[1]));
                    int year = Integer.parseInt(parts[2]);
                    result = new Date(year - 1900, month - 1, day);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
        public Date getDateField(Object obj) {
        String text="";//invalid date
        if(obj!=null){
            text=obj.toString();
        }
        return getDateField(text);
    }
}
