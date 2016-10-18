//A0144132W
package main.commons.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    /*
     * convenient constructor for Date, with time
     * 
     * returns a Date object
     */
    public static Date getDate(int year, int month, int day, int hour, int min){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        return cal.getTime();
    }

    /*
     * convenient constructor for Date, without time
     * 
     * returns a Date object
     */
    public static Date getDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }
    
    /*
     * checks if the date is within start and end
     * 
     * @returns true if the given date is within the start and end date
     */
    public static boolean dateWithin(Date start, Date end, Date date) {
        if (start == null || end == null || date == null) return false;
        if (start.before(date) && end.after(date)) return true;
        else return false;
    }
    
    /*
     * compares the 2 dates, not including the time
     * 
     * @returns true if both dates are on the same day
     */
    public static boolean areSameDay(Date date1, Date date2) {
        if (date1 == null | date2 == null) return false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        
        if ((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) 
            && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) 
            && (cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE))) {
            return true;
        }
        else return false;
    }
}
