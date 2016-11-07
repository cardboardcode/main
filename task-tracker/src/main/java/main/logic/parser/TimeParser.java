//@@author A0144132W
package main.logic.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.joestelmach.natty.*;

import main.commons.core.LogsCenter;
import main.commons.util.DateUtil;

public class TimeParser {
    private static final Logger logger = LogsCenter.getLogger(TimeParser.class);
        
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

        DateGroup group = getLastGroup(groups);
        
        if(!isValidDate(raw_input, group)) {
            return Triple.of(raw_input, new ArrayList<Date>(), ImmutableList.of(true, false));
        }
                
        correctTime(group);

        List<Date> dates = group.getDates();
        boolean isInferred = group.isTimeInferred();
        
        if (isInferred) {
            setDefaultTime(dates);
        }

        String processed = getProcessedString(raw_input, group);      
        return Triple.of(processed.trim(),dates,ImmutableList.of(isInferred,group.isRecurring()));
    }

    /*
     * @returns the last DateGroup if there are multiple DateGroups
     */
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
    
    /*
     * checks whether a date is valid
     * 
     * date becomes invalid if suffix of date is not a whitespace, as this means
     * the date is likely to be part of another word.
     * 
     * @returns a boolean indicating the date's validity 
     */
    private static boolean isValidDate(String raw_input, DateGroup group) {
        if (dateAtExtremesOfInput(raw_input, group)) {
            return true;
        }
        else if (suffixOrPrefixNotWhitespace(group)) {
            logger.info("invalid date");
            return false;
        }
        else {
            return true;
        }
    }

    /*
     * checks if the immediate suffix or prefix are both not whitespaces 
     */
    private static boolean suffixOrPrefixNotWhitespace(DateGroup group) {
        return !Character.isWhitespace(group.getSuffix(1).charAt(0)) || !Character.isWhitespace(group.getPrefix(1).charAt(0));
    }

    /*
     * checks if group is at the start or end of the raw_input
     */
    private static boolean dateAtExtremesOfInput(String raw_input, DateGroup group) {
        return group.getPosition() == 1 || group.getPosition() + group.getText().length() - 1 == raw_input.length();
    }
    
    /*
     * corrects the time if need.
     * 
     * if time has no suffix (i.e pm or am), a reasonable time will be set,
     * i.e 4pm instead of 4am
     * 
     * @returns a DateGroup object with logical time
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
        return group;
    }
    
    /*
     * replaces the original odd date with one that makes more logical sense
     */
    private static void editDate(List<Date> dates, int i, ParseLocation next) {
        logger.info("correcting time");                    
        int hour = Integer.valueOf(next.getText());
        hour = correctHour(hour);
        dates.set(i, DateUtil.setTime(dates.get(i), hour, false));
    }

    /*
     * checks if input has time information but no merdian indicator (i.e am, pm)
     */
    private static boolean hasTimeWithoutMerdianIndicator(Map<String, List<ParseLocation>> parse_locations) {
        return parse_locations.containsKey("int_00_to_23_optional_prefix") && !parse_locations.containsKey("simple_meridian_indicator");
    }

    /*
     * corrects odd timings, which appears because natty parser defaults 
     * unannotated numbers to morning. e.g 4 is 4am by default
     * 
     * @returns the new hour of the day
     */
    private static int correctHour(int hour) {
        if (hour < 7) {
            hour += 12;
        }
        else if (hour > 22) {
            hour -= 12;
        }
        return hour;
    }
    
    /*
     * set the time of given date to be 8am
     */
    private static Date setDefaultTime(Date date) {
        assert date != null;
        return DateUtil.setTime(date, 8, true);
    }
    
    /*
     * sets the list of dates' time to 8am
     */
    private static void setDefaultTime(List<Date> dates) {
        for (int i = 0; i < dates.size(); i++) {
            dates.set(i, setDefaultTime(dates.get(i)));
        }
    }

    /*
     * @returns string without the date inside
     */
    private static String getProcessedString(String input, DateGroup group) {

        StringBuilder builder = new StringBuilder();
        
        // natty indexing starts from 1
        builder.append(input.substring(0, group.getPosition() - 1).trim())
               .append(" ")
               .append(input.substring(group.getPosition() - 1 + group.getText().length(), input.length()).trim());
       
        return builder.toString();
    }
    
}

