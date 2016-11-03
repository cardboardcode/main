//@@author A0144132W
package main.logic.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.joestelmach.natty.*;

import main.commons.core.LogsCenter;
import main.commons.util.DateUtil;


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

        
        for (int i=0; i<groups.size(); i++) {
            System.out.println(groups.get(i).getDates());
        }

        DateGroup group = getLastGroup(groups);
        
        if(!isValidDate(raw_input, group)) {
            return Triple.of(raw_input, new ArrayList<Date>(), ImmutableList.of(true, false));
        }
                
        correctTime(group);

        List<Date> dates = group.getDates();
        boolean isInferred = group.isTimeInferred();
        
        if (isInferred) {
            for (int i = 0; i < dates.size(); i++) {
                dates.set(i, setDefaultTime(dates.get(i)));
            }
        }
        
        String processed = getProcessedString(raw_input, group);
        
        return Triple.of(processed.trim(),dates,ImmutableList.of(isInferred,group.isRecurring()));
    }

    private static DateGroup getLastGroup(List<DateGroup> groups) {
        DateGroup group;
        if (groups.size() > 1) {
            group = groups.get(groups.size() - 1);
        }
        else {
            group = groups.get(0);
        }
        return group;
    }

    private static boolean isValidDate(String raw_input, DateGroup group) {
        // if suffix of date is not a whitespace. Most likely incorrectly parsed
        try {
            if (!Character.isWhitespace(group.getSuffix(1).charAt(0))) {
                logger.info("invalid date");
                return false;
            }
        } catch (StringIndexOutOfBoundsException e) {
            return true; // happens when the date is at the start of the string
        } 
        return true;
    }
    
    /*
     * corrects odd timings and takes the dates nearing to 
     * 
     * natty parser defaults unannotated numbers to morning
     * e.g 4 is 4am by default
     * 
     * if time has no postfix (i.e pm or am), set a reasonable time, i.e 4pm instead of 4am
     * 
     * @returns a DateGroup object with only the relevant dates
     * 
     */
    private static DateGroup correctTime(DateGroup group) {
        List<Date> dates = group.getDates();
        Map<String, List<ParseLocation>> parse_locations = group.getParseLocations();
        
        if (hasTimeWithoutMerdianIndicator(parse_locations)) {
            List<ParseLocation> hours = parse_locations.get("int_00_to_23_optional_prefix");
            
            for (int i = 0; i < hours.size() ; i++) {
                ParseLocation next = hours.get(i); 
                if (StringUtils.isNumeric(next.getText())) {
                    editDate(dates, i, next);
                }
            }
        }
        System.out.println("group" + group.getDates());
        return group;
    }

    /*
    private static DateGroup formNewDateGroup(DateGroup group, int start_index, int end_index) {
        DateGroup new_group = new DateGroup();
        group.getDates().subList(start_index, end_index).stream().forEach(date -> new_group.addDate(date));
        System.out.println("sublist " + group.getDates().subList(start_index, end_index));
        System.out.println("dates saved " + new_group.getDates());
        new_group.setDateInferred(group.isDateInferred());
        new_group.setRecurring(group.isRecurring());
        new_group.setRecurringUntil(group.getRecursUntil());
        
        Map<String, List<ParseLocation>> parse_locations = group.getParseLocations();
        Map<String, List<ParseLocation>> new_parse_locations = new HashMap<String, List<ParseLocation>>();

        System.out.println("old parser " + group.getParseLocations());

//        for (String param : parse_locations.keySet()) {
//            new_parse_locations.put(param, parse_locations.get(param).subList(start_index, end_index));
//        }
        
//        new_group.setParseLocations(new_parse_locations);
        new_group.setParseLocations(parse_locations);
        
        System.out.println("new parser " + new_group.getParseLocations());
        
        return new_group;
    }
*/
    private static void editDate(List<Date> dates, int i, ParseLocation next) {
        logger.info("correcting time");                    
        int hour = Integer.valueOf(next.getText());
        hour = correctHour(hour);
        dates.set(i, DateUtil.setTime(dates.get(i), hour, false));
    }

    private static Pair<Integer, Integer> getValidDateIndex(List<Date> dates) {
        int numDates = dates.size();
        if (numDates > 2) {
            return Pair.of(numDates - 2, numDates);
        }
        else {
            return Pair.of(0, numDates);
        }
    }

    private static boolean hasTimeWithoutMerdianIndicator(Map<String, List<ParseLocation>> parse_locations) {
        return parse_locations.containsKey("int_00_to_23_optional_prefix") && !parse_locations.containsKey("simple_meridian_indicator");
    }

    private static int correctHour(int hour) {
        if (hour < 7) {
            hour += 12;
        }
        else if (hour > 22) {
            hour -= 12;
        }
        return hour;
    }
    
    private static Date setDefaultTime(Date date) {
        assert date != null;
        return DateUtil.setTime(date, 8, true);
    }


    /*
     * @returns string without the date inside
     */
    private static String getProcessedString(String input, DateGroup group) {

        StringBuilder builder = new StringBuilder();
        System.out.println("position " + group.getPosition());
        
        // natty indexing starts from 1
        builder.append(input.substring(0, group.getPosition() - 1).trim())
               .append(" ")
               .append(input.substring(group.getPosition() - 1 + group.getText().length(), input.length()).trim());
       
        return builder.toString();
    }
    
}

