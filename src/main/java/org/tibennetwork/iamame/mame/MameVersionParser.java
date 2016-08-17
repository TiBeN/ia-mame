package org.tibennetwork.iamame.mame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MameVersionParser {

    /**
     * Parse and return the version of Mame from the first line
     * of the output of the -help command
     */
    public String parse (String helpCommandFirstLine) 
            throws UnhandledMameVersionPatternException {
        Pattern p = Pattern.compile("^.*v([0-9]+)\\.([0-9]+).*$");
        Matcher m = p.matcher(helpCommandFirstLine); 
        if (!m.matches()) {
            throw new UnhandledMameVersionPatternException (
                "Can't parse version from string: \"" 
                    + helpCommandFirstLine 
                    +"\"");
        }

        return m.group(1) + "." + m.group(2);

    }

}
