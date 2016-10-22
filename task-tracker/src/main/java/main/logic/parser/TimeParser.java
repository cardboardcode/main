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

        DateGroup group = groups.get(0);

        // if suffix of date is not a whitespace. Most likely incorrectly parsed
        try {
            if (!Character.isWhitespace(group.getSuffix(1).charAt(0))) { 
                return Triple.of(raw_input, new ArrayList<Date>(), ImmutableList.of(true, false));
            }
        } catch (StringIndexOutOfBoundsException e) {} // happens when the date is at the start of the string
                
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
    
    /*
     * corrects odd timings
     * 
     * natty parser defaults unannotated numbers to morning
     * e.g 4 is 4am by default
     * 
     * if time has no postfix (i.e pm or am), set a reasonable time, i.e 4pm instead of 4am
     * 
     */
    private static void correctTime(DateGroup group) {
        List<Date> dates = group.getDates();
        Map<String, List<ParseLocation>> parse_locations = group.getParseLocations();
        
        // maximum number of dates is 2
        if (dates.size() > 2) dates = dates.subList(0, 2);
        
        if (parse_locations.containsKey("int_00_to_23_optional_prefix") && !parse_locations.containsKey("simple_meridian_indicator")) {
            List<ParseLocation> hours = parse_locations.get("int_00_to_23_optional_prefix");
            
            for (int i = 0; i < hours.size() ; i++) {
                ParseLocation next = hours.get(i); 
                if (StringUtils.isNumeric(next.getText())) {
                    logger.info("correcting time");
                    
                    int hour = Integer.valueOf(next.getText());
                    if (hour < 7) hour += 12;
                    else if (hour > 22) hour -= 12;
                    
                    dates.set(i, DateUtil.setTime(dates.get(i), hour, false));
                }
            }
        }
    }
    
    private static Date setDefaultTime(Date date) {
        assert date != null;
        return DateUtil.setTime(date, 8, true);
    }


    
    private static String getProcessedString(String input, DateGroup group) {

        StringBuilder builder = new StringBuilder();
        
        // natty indexing starts from 1
        builder.append(input.substring(0, group.getPosition() - 1).trim())
               .append(" ")
               .append(input.substring(group.getPosition() - 1 + group.getText().length(), input.length()).trim());
       
        return builder.toString();
    }
    
}

