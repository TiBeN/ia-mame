package org.tibennetwork.iamame.mame;

import org.apache.commons.cli.Options;

/**
 * Bag for Mame command line scheme options 
 */
public class CommandLineOptions {

    private Options commands;

    private Options options;

    public CommandLineOptions (Options commands, Options options) {
        this.commands = commands;
        this.options = options;        
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

}
