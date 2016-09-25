package org.tibennetwork.iamame.mame;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.Options;

/**
 * Generate Set of Mame Options used by MameArguments
 */
public class CommandLineOptionsFactory {

    public static String[] knownNonBooleanOptions = {
        "frameskip", 
        "seconds_to_run",
        "intoverscan",
        "intscalex",
        "intscaley",
        "beam_intensity_weight",
        "samples",
        "volume",
        "debugger_font_size",
        "coin_impulse",
        "numscreens",
        "prescale" }; 

    /**
     * Deduce options from the mame runtime by parsing
     * the output of the "-showusage" options
     */ 
    public CommandLineOptions deduceFromMameRuntime (MameRuntime mame) 
            throws IOException,
                   InterruptedException,
                   MameExecutionException {


        // Get options list as string from mame runtime

        Options opts = new Options();

        String[] showConfigArgs = {"-showconfig"};
        List<String> showConfigStdout 
            = mame.executeAndReturnStdout(showConfigArgs, false);
        
        // Loop through the stdout content, line by line

        Pattern showConfigPattern = Pattern.compile("^([a-z0-9_]+) +(.*)?$");
        for (String stdoutLine: showConfigStdout) {
            Matcher m = showConfigPattern.matcher(stdoutLine);
            if (!m.matches()) {
                continue;
            }

            String key = m.group(1);
            String defaultValue = m.group(2);

            // Boolean value ?

            if ((defaultValue.equals("0") || defaultValue.equals("1"))
                    && !this.isKnownAsNotABooleanOption(key)) {
                opts.addOption(key, false, "");

                // negative option
                
                opts.addOption("no" + key, false, "");

            } else {
                opts.addOption(key, true, "");
            }

        }

        // Get commands list as string from mame runtime

        Options commands = new Options();

        String[] showUsageArgs = {"-showusage"};
        List<String> showUsageStdout 
            = mame.executeAndReturnStdout(showUsageArgs, false);
        
        // Loop through the stdout content, line by line

        Pattern showUsagePattern = Pattern.compile("^-([a-z0-9_]{1,20}) +(.*)?$");
        for (String stdoutLine: showUsageStdout) {
            Matcher m = showUsagePattern.matcher(stdoutLine);
            if (!m.matches()) {
                continue;
            }

            String key = m.group(1);

            // -showusage command return a list of all Mame command line
            // options. We filter only commands kinds of options by 
            // searching if the option has already been deduced from 
            // the -showconfig.

            if (opts.hasOption(key)) {
                continue;
            }

            System.out.println(key);
            commands.addOption(key, false, "");

        }
            
        return new CommandLineOptions(commands, opts);

    }

    private boolean isKnownAsNotABooleanOption (String optionKey) {
        for (String notBooleanKey
                : CommandLineOptionsFactory.knownNonBooleanOptions) {
            if (optionKey.equals(notBooleanKey)) {
                return true;
            }
        }
        return false;
    }

}
