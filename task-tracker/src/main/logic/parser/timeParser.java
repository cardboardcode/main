package main.logic.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

import javafx.util.Pair;


public class timeParser {
    
    Parser parser = new Parser();
    
    public timeParser() {}
    
    public static Pair<String,List<Date>> extractTime(String input) {
        List<DateGroup> groups = new Parser().parse(input);
        DateGroup group = groups.get(0);
        
        String matchedText = group.getText();
        
        input = input.substring(0,group.getPosition() - 1);

        List<Date> dates = group.getDates();
        dates = dates.subList(0, 1);
        
        if (group.isTimeInferred()) {
            for (int i = 0; i < dates.size(); i++)
                dates.set(i, setDefaultTime(dates.get(i)));
        }
        else {
            for (int i = 0; i < dates.size(); i++) 
                dates.set(i, correctTime(dates.get(i),matchedText);
        }

        
        return new Pair<String,List<Date>>(input,dates);
                
    }
    
    private static Date setDefaultTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        return cal.getTime();
    }
    
    /*
     * converts odd timings like 4am to 4pm
     * 
     * due to the parser defaulting unannotated numbers to morning
     * e.g 4 is 4am by default
     * 
     */
    private static Date correctTime(Date date, String extracted) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int postfix = extracted.indexOf(hour);
        
        if (extracted.charAt(postfix+1) == ' ') {
            if (hour < 7)
                hour += 12;
            else if (hour > 22)
                hour -= 12;
            cal.set(Calendar.HOUR_OF_DAY, hour);
        }
        
        return cal.getTime();
    }
    
    public static void extractTime_test(String input) {
        List<DateGroup> groups = new Parser().parse(input);
        for(DateGroup group:groups) {
            List<Date> dates = group.getDates();
            int line = group.getLine();
            int column = group.getPosition();
            String matchingValue = group.getText();
            boolean isRecurreing = group.isRecurring();
            Date recursUntil = group.getRecursUntil();
            boolean infertime = group.isTimeInferred();
            System.out.println(dates + " " + line + " " + column + " " + matchingValue + " " + isRecurreing + " " + recursUntil + " " + infertime);
        }
    }
    

}

