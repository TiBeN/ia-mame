package org.tibennetwork.iamame.mame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

/**
 * Set of Mame command line arguments
 */
public class MameArguments {
    
    /**
     * Object-value which contains Machine and Software 
     * objects extracted and instanciated from the 
     * parsed command line arguments
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

    private CommandLineOptions mameOptions;

    private String[] rawArgs;

    private CommandLine commandLine = null;

    public MameArguments (CommandLineOptions mameOptions, String[] rawArgs)
            throws InvalidMameArgumentsException {
        this.mameOptions = mameOptions;            
        this.rawArgs = rawArgs;
    }
    
    public String[] getRawArgs () {
        return this.rawArgs;
    }

    /**
     * Tell whether the command line contains a 
     * Mame command (-listxml, -createconfig etc.)
     */
    public boolean containsCommand () {

        for (Option cmd: this.mameOptions.getCommands().getOptions()) {
            if (this.commandLine.hasOption(cmd.getOpt())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return non command options args
     */
    public String[] getRawOptionsArgs () {
        List<String> roa = new ArrayList<>();
        optionsLoop: for (Option o: this.commandLine.getOptions()) {
            
            // Discard mame commands
            for (Option cmd: this.mameOptions.getCommands().getOptions()) {
                if (o.getOpt().equals(cmd.getOpt())) {
                    continue optionsLoop;
                }
            }

            // Discard media types options (ex -cart mario)
            for (String mt: mediaTypes) {
                if (o.getOpt().equals(mt)) {
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
        
        try {            
            CommandLineParser parser = new DefaultParser();
            this.commandLine = parser.parse(
                mameOptions.getMergedCommandsAndOptions(), 
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

}
