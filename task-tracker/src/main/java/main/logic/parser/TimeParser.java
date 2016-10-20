package main.logic.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.joestelmach.natty.*;

import main.commons.core.LogsCenter;


public class TimeParser {
    private static final Logger logger = LogsCenter.getLogger(TimeParser.class);

    
    Parser parser = new Parser();
    
    public TimeParser() {}
    
    /*
     * extracts the relevant time information
     * 
     * @returns a triple of consisting of the task description, list of dates detected (max 2),
     * and list of boolean representing isTimeInferred and isRecurring.
     *  
     */
    public static Triple<String, List<Date>, List<Boolean>> extractTime(String raw_input) {
        List<DateGroup> groups = new Parser().parse(raw_input);
        
        if (groups.size() == 0) {
            return Triple.of(raw_input,new ArrayList<Date>(),ImmutableList.of(true,false));
        }
        
        DateGroup group = groups.get(0);
        
        String matchedText = group.getText();
        
//        System.out.println(matchedText);
//        System.out.println(raw_input);
//        System.out.println(group.getPosition());
        
        
        String processed = raw_input.substring(0, group.getPosition() - 1);
        processed = processed.trim() + " " + raw_input.substring(group.getPosition() + matchedText.length() - 1, raw_input.length()).trim(); 

        List<Date> dates = group.getDates();
        
//        String processed = validateDates(raw_input, dates, group.getParseLocations(), group.getPosition() - 1);
       
        boolean isInferred = group.isTimeInferred();
        if (isInferred) {
            for (int i = 0; i < dates.size(); i++) {
                dates.set(i, setDefaultTime(dates.get(i)));
            }
        }
        
        return Triple.of(processed.trim(),dates,ImmutableList.of(isInferred,group.isRecurring()));
    }
    
//    private static String validateDates(String input, List<Date> dates, Map<String, List<ParseLocation>> parse_locations, int pos) {
////        System.out.println(parse_locations);
////        System.out.println(parse_locations.get("hours"));
////        System.out.println("size: " + parse_locations.size());
//        String out = "";
//
//        // if start of time not a whitespace --> time is invalid, remove
//        if (parse_locations.containsKey("explicit_time") && parse_locations.containsKey("int_00_to_23_optional_prefix")) {
//            
//            List<ParseLocation> hours = parse_locations.get("int_00_to_23_optional_prefix");
//            
//            for (int i = 0; i < hours.size() ; i++) {
//                ParseLocation next = hours.get(i); 
////                System.out.println(next.getStart() + " " + next.getEnd() + "\n" + next);
////                System.out.println(input);
////                System.out.println(input.substring(next.getStart() - 1, next.getEnd() - 1));
//                
//                if (next.getStart() - 1 > 0 && !Character.isWhitespace(input.charAt(next.getStart() - 2))) {
//                    logger.info("removing invalid time");
//                    hours.remove(next);
//                    dates.remove(i);
//                }
//            }
//            
//            // max number of dates is 2
//            if (dates.size() > 2) dates = dates.subList(0, 1);
//
//            
//            StringBuilder builder = new StringBuilder();
//            // truncate away the dates portion, so left the task description or other relevant portions
//            if (hours.size() > 0) {
//                builder.append(input.substring(0,hours.get(0).getStart() - 1).trim());
//                System.out.println("in1: " + input);
//
//                builder.append(" ");
//                System.out.println("in2: " + input);
//
//                builder.append(input.substring(hours.get(0).getEnd(), input.length()));
//                
//                out = builder.toString();
//                System.out.println("out1: " + out);
//
////                
////                StringBuffer text = new StringBuffer(input);
////                text.replace(StartIndex ,EndIndex ,String);
//            }
//            else {
//                out = input;
//            }
//            
//        }
//        
//        correctTime(dates, parse_locations);
//        
//        System.out.println("in3: " + input);
////        System.out.println("pos: " + pos);
////        System.out.println("len " + input.length());
////        System.out.println("parse location size " + parse_locations.size());
//        
//        System.out.println(out);
//        
//        if (!out.equals("")) return out;
//        else if (dates.size() == 0) return input;
//        else if (pos == 0) return input.substring(parse_locations.size(), input.length());
//        else return input.substring(0, pos);
//    }

    
    /*
     * corrects odd timings
     * 
     * natty parser defaults unannotated numbers to morning
     * e.g 4 is 4am by default
     * 
     * if time has no postfix (i.e pm or am), set a reasonable time, i.e 4pm instead of 4am
     * 
     */
    private static void correctTime(List<Date> dates, Map<String, List<ParseLocation>> parse_locations) {
        if (parse_locations.containsKey("int_00_to_23_optional_prefix") && !parse_locations.containsKey("simple_meridian_indicator")) {
            List<ParseLocation> hours = parse_locations.get("int_00_to_23_optional_prefix");
            
            for (int i = 0; i < hours.size() ; i++) {
                ParseLocation next = hours.get(i); 
                Integer hour;
                if (StringUtils.isNumeric(next.getText())) {
                    logger.info("correcting time");
                    hour = Integer.valueOf(next.getText());
                    if (hour < 7) hour += 12;
                    else if (hour > 22) hour -= 12;
                    
                    dates.set(i, setHour(dates.get(i), hour));
                }
            }
        }
    }

    private static Date setDefaultTime(Date date) {
        assert date != null;
        return setHour(date, 8);
    }

    private static Date setHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    
    /*
     * converts odd timings like 4am to 4pm
     * 
     * due to the parser defaulting unannotated numbers to morning
     * e.g 4 is 4am by default
     * 
     */
  /*  private static Date correctTime(Date date, String extracted) {
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
            cal.set(Calendar.SECOND, 0);
        }
        
        return cal.getTime();
    }
    */
}

