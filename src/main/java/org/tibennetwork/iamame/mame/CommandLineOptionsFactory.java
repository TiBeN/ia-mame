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

    /**
     * Force theses option to be non boolean
     * (it is not deductible from the -showconfig command)
     */
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
     * Hold a static list of known media types here because discovering 
     * them through mame runtime using -listmedia command takes too long 
     */
    private static String[] mediaTypes = {
        "bitb",
        "brief",
        "card",
        "card1",
        "card10",
        "card11",
        "card12",
        "card13",
        "card14",
        "card15",
        "card16",
        "card2",
        "card3",
        "card4",
        "card5",
        "card6",
        "card7",
        "card8",
        "card9",
        "cart",
        "cart1",
        "cart10",
        "cart11",
        "cart12",
        "cart13",
        "cart14",
        "cart15",
        "cart16",
        "cart17",
        "cart18",
        "cart2",
        "cart3",
        "cart4",
        "cart5",
        "cart6",
        "cart7",
        "cart8",
        "cart9",
        "cass",
        "cass1",
        "cass2",
        "cdrm",
        "cdrm1",
        "cdrm2",
        "cdrm3",
        "ct",
        "cyln",
        "disk1",
        "disk2",
        "dump",
        "flop",
        "flop1",
        "flop2",
        "flop3",
        "flop4",
        "flop5",
        "flop6",
        "hard",
        "hard1",
        "hard2",
        "hard3",
        "hard4",
        "hard5",
        "hard6",
        "hard7",
        "incart60p",
        "magt",
        "magt1",
        "magt2",
        "magt3",
        "magt4",
        "mc1",
        "mc2",
        "memc",
        "min",
        "mout",
        "mout1",
        "mout2",
        "ni",
        "p1",
        "p2",
        "prin",
        "prin1",
        "prin2",
        "prin3",
        "ptap1",
        "ptap2",
        "quik",
        "quik1",
        "quik2",
        "sasi",
        "serl"
    };

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

        Pattern showUsagePattern 
            = Pattern.compile("^-([a-z0-9_]{1,20}) +(.*)?$");
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

            commands.addOption(key, false, "");

        }

        // Build media type options from static list 

        Options mediaTypes = new Options();

        for (String mt: CommandLineOptionsFactory.mediaTypes) {
            mediaTypes.addOption(mt, true, "");
        }
            
        return new CommandLineOptions(commands, opts, mediaTypes);

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
