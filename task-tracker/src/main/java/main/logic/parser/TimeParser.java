package main.logic.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

import javafx.util.Pair;


public class TimeParser {
    
    Parser parser = new Parser();
    
    public TimeParser() {}
    
    public static Pair<String,List<Date>> extractTime(String input) {
        List<DateGroup> groups = new Parser().parse(input);
        
        if (groups.size() == 0) {
            return new Pair<String,List<Date>>(input,new ArrayList<Date>());
        }
        
        DateGroup group = groups.get(0);
        
        String matchedText = group.getText();
        
        input = input.substring(0,group.getPosition() - 1);

        List<Date> dates = group.getDates();
        
        // max number of dates is 2
        if (dates.size() > 2) {
            dates = dates.subList(0, 1);
        }
        
        if (group.isTimeInferred()) {
            for (int i = 0; i < dates.size(); i++) {
                dates.set(i, setDefaultTime(dates.get(i)));
            }
        }
        else {
            for (int i = 0; i < dates.size(); i++) {
                dates.set(i, correctTime(dates.get(i),matchedText));
            }
        }

        
        return new Pair<String,List<Date>>(input,dates);
                
    }
    
    private static Date setDefaultTime(Date date) {
        assert date != null;
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
        assert date!= null;
        assert extracted != null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int index = extracted.indexOf(hour);
        
        // check if next character is empty
        if ((extracted.length() == index + 1) | extracted.charAt(index + 1) == ' ') {
            if (hour < 7) {
                hour += 12;
            }
            else if (hour > 22) {
                hour -= 12;
            }
            cal.set(Calendar.HOUR_OF_DAY, hour);
        }
        
        return cal.getTime();
    }
    
}

