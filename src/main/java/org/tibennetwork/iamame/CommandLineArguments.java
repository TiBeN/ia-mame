package org.tibennetwork.iamame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.tibennetwork.iamame.mame.CommandLineOptions;
import org.tibennetwork.iamame.mame.InvalidMameArgumentsException;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.MachineDoesntExistException;
import org.tibennetwork.iamame.mame.MachineHasNoSoftwareListException;
import org.tibennetwork.iamame.mame.MachineRepository;
import org.tibennetwork.iamame.mame.MediaDevice;
import org.tibennetwork.iamame.mame.Software;
import org.tibennetwork.iamame.mame.SoftwareNotFoundInSoftwareListsException;
import org.tibennetwork.iamame.mame.SoftwareRepository;

/**
 * Set of Mame command line arguments
 */
public class CommandLineArguments {
    
    /**
     * Object-value which contains Machine and Software objects extracted 
     * and instanciated from the parsed command line arguments
     */
    public static class ExtractedMachineAndSoftwares {
        
        private Machine machine = null;

        private List<Software> softwares = new ArrayList<>();

        public boolean hasMachine () {
            return this.machine != null;
        }

        public Machine getMachine () {
            return machine;
        }

        public void setMachine (Machine m) {
            this.machine = m;
        }

        public List<Software> getSoftwares() {
            return softwares;
        }

        public boolean hasSoftwares () {
            return softwares != null;
        }

    }

    /**
     * IaMmame specific options
     */            
    private Options iaMameOptions;

    /**
     * Mame specific options
     */
    private CommandLineOptions mameOptions;

    private String[] rawArgs;

    private CommandLine commandLine = null;

    public CommandLineArguments (CommandLineOptions mameOptions, String[] rawArgs)
            throws InvalidMameArgumentsException {
        this.mameOptions = mameOptions;
        this.iaMameOptions = buildIaMameOptions();
        this.rawArgs = rawArgs;
    }

    /**
     * Tell whether the command line contains the given option
     */
    public boolean contains (String optionName) {
        return this.commandLine.hasOption(optionName);
    }
    
    /**
     * Tell whether the command line contains a 
     * Mame command (-listxml, -createconfig etc.)
     */
    public boolean containsMameCommand () {

        for (Option cmd: this.mameOptions.getCommands().getOptions()) {
            if (this.commandLine.hasOption(cmd.getOpt())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return Mame only args
     */
    public String[] getMameRawArgs () {

        List<String> mra = new ArrayList<>();

        optionsLoop: for (Option o: this.commandLine.getOptions()) {

            // Discard IaMame options

            for (Option ia: this.iaMameOptions.getOptions()) {
                if (o.getOpt().equals(ia.getOpt())) {
                    continue optionsLoop;
                }
            } 
            
            mra.add('-' + o.getOpt());

            if (o.hasArg() || o.hasArgs()) {
                for (String a: this.commandLine.getOptionValues(o.getOpt())) {
                    mra.add(a);
                }
            }
        }

        // Add non option args

        for (String arg: this.commandLine.getArgs()) {
            mra.add(arg);
        }

        return mra.toArray(new String[mra.size()]);

    }

    /**
     * Return non command Mame options args
     */
    public String[] getMameOptionsRawArgs () {

        List<String> roa = new ArrayList<>();
        optionsLoop: for (Option o: this.commandLine.getOptions()) {
            
            // Discard mame commands
            
            for (Option cmd: this.mameOptions.getCommands().getOptions()) {
                if (o.getOpt().equals(cmd.getOpt())) {
                    continue optionsLoop;
                }
            }

            // Discard media types Mame options (ex -cart mario)

            for (Option mt: this.mameOptions.getMediaTypes().getOptions()) {
                if (o.getOpt().equals(mt.getOpt())) {
                    continue optionsLoop;
                }
            } 

            // Discard IaMame options

            for (Option ia: this.iaMameOptions.getOptions()) {
                if (o.getOpt().equals(ia.getOpt())) {
                    continue optionsLoop;
                }
            } 
            
            
            roa.add('-' + o.getOpt());

            if (o.hasArg() || o.hasArgs()) {
                for (String a: this.commandLine.getOptionValues(o.getOpt())) {
                    roa.add(a);
                }
            }
        }
        return roa.toArray(new String[roa.size()]);
    }

    /**
     * Extract and instanciate Machine and Software objects from 
     * the parsed command-line arguments.
     */
    public ExtractedMachineAndSoftwares extractMachineAndSoftwares (
        MachineRepository mr,
        SoftwareRepository sr)
            throws InvalidMameArgumentsException,
                IOException,
                InterruptedException {

        ExtractedMachineAndSoftwares ems = new ExtractedMachineAndSoftwares();

        // Retrieve non option arguments
        // There must be at most 2 non arguments : 
        // <machine> [<software> | -<media> <software|softwarepath>]
        String[] cmdArgs = this.commandLine.getArgs();

        if (cmdArgs.length > 2) {
            throw new InvalidMameArgumentsException(
                "Too many non option args");
        }

        Machine m = null;

        if (cmdArgs.length >= 1) {
            try {
                m = mr.findByName(cmdArgs[0]);
                ems.setMachine(m);
            } catch (MachineDoesntExistException e) {
                throw (InvalidMameArgumentsException) 
                    new InvalidMameArgumentsException(e.getMessage())
                        .initCause(e);
            }

            if (cmdArgs.length > 1) {

                if(this.isRegularSoftwareFile(cmdArgs[1])) {
                    throw new InvalidMameArgumentsException(
                        "The software must be a software name from software"
                            + " list and not a regular file");
                }

                try {
                    ems.getSoftwares().add(
                        sr.findByMachineAndByName(m, cmdArgs[1]));
                } catch (MachineHasNoSoftwareListException 
                        | SoftwareNotFoundInSoftwareListsException 
                        | MachineDoesntExistException e) {
                    throw (InvalidMameArgumentsException) 
                        new InvalidMameArgumentsException(e.getMessage())
                            .initCause(e);
                }
            }
            else {
                for (MediaDevice md: m.getMediaDevices()) {
                    if (this.commandLine.hasOption(md.getBriefname())) {

                        String softwareName = this.commandLine
                            .getOptionValue(md.getBriefname());

                        Software s;

                        if(this.isRegularSoftwareFile(softwareName)) {
                            s = new Software(m, md, softwareName);
                        } else {
                            try {
                                s = sr.findByMachineAndAndByMediaTypeAndByName(
                                    m,
                                    md,
                                    softwareName);
                            } catch (MachineHasNoSoftwareListException 
                                    | SoftwareNotFoundInSoftwareListsException 
                                    | MachineDoesntExistException e) {
                                throw (InvalidMameArgumentsException) 
                                    new InvalidMameArgumentsException(
                                            e.getMessage())
                                        .initCause(e);
                            }
                        }
                        
                        ems.getSoftwares().add(s);

                    }
                }
            }
        }

        return ems;

    }

    /**
     * Parse args in raw string and store usefull Arguments.
     */
    public void validate () 
            throws InvalidMameArgumentsException {
        
        // Merge IaMame and Mame options

        Options mergedOptions = new Options();
        for (Option o: mameOptions.getAllOptions().getOptions()) {
            mergedOptions.addOption(o);
        }
        for (Option o: iaMameOptions.getOptions()) {
            mergedOptions.addOption(o);
        } 

        try {
            CommandLineParser parser = new DefaultParser();
            this.commandLine = parser.parse(
                mergedOptions,
                rawArgs);
        } catch (ParseException e) {
            throw (InvalidMameArgumentsException) 
                new InvalidMameArgumentsException(
                        e.getMessage())
                    .initCause(e);
        }

    }

    /**
     * Determine whether the given file is a regular
     * software file (ie is not a softwarelist name)
     */
    private boolean isRegularSoftwareFile (String name) {
        return !FilenameUtils.getExtension(name).isEmpty()
                || new File(name).exists();
    }

    /**
     * Create Options bag for ia-mame specific options
     */
    private Options buildIaMameOptions () {

        // Build IaMame specific options

        Options opts = new Options();
        
        opts.addOption("noexecmame", false, "");

        return opts;

    }

}
