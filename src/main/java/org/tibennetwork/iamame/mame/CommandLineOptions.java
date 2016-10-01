package org.tibennetwork.iamame.mame;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Bag for Mame command line scheme options 
 */
public class CommandLineOptions {

    private Options commands;

    private Options options;

    private Options mediaTypes;

    public CommandLineOptions (
            Options commands, 
            Options options, 
            Options mediaTypes) {
        this.commands = commands;
        this.options = options;        
        this.mediaTypes = mediaTypes;
    }

   /**
     * Return "command" type Mame options.
     * Mame commands are options which does not execute Mame normally but 
     * do a specific task such as -listxml, -help, -showusage etc.
     */
    public Options getCommands() {
        return commands;
    }

    /**
     * Return non-command Mame options. Theses are configuration options 
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Return media type specific options. 
     * Theses are system specific options targeting an image file specific 
     * to a system media type (cartridge, floppy drive etc.)
     */
    public Options getMediaTypes() {
        return mediaTypes;
    }

    /**
     * Return a bag of all the options available (command, options and
     * media types)
     */
    public Options getAllOptions () {
        Options mergedOpts = new Options();
        for (Option opt: this.getOptions().getOptions()) {
            mergedOpts.addOption(opt);
        }
        for (Option cmd: this.getCommands().getOptions()) {
            mergedOpts.addOption(cmd);
        }
        for (Option mtype: this.getMediaTypes().getOptions()) {
            mergedOpts.addOption(mtype);
        }
        return mergedOpts;
    }

}
