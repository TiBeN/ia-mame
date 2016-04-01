package org.tibennetwork.iamame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.ParseException;
import org.tibennetwork.iamame.internetarchive.MachineRomFileNotFoundInCollection;
import org.tibennetwork.iamame.internetarchive.MessAndMameCollection;
import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;
import org.tibennetwork.iamame.internetarchive.collectionitem.SoftwareFileNotFoundInCollectionException;
import org.tibennetwork.iamame.mame.InvalidMameArgumentsException;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.MachineRepository;
import org.tibennetwork.iamame.mame.MameArguments;
import org.tibennetwork.iamame.mame.MameRuntime;
import org.tibennetwork.iamame.mame.Software;
import org.tibennetwork.iamame.mame.SoftwareRepository;

public class IaMame
{

    /**
     * ia-mame command-line entry-point
     */
    public static void main (String[] args)
    {

        IaMame.initConfiguration();            

        MameArguments mameArgs = null;
        MameRuntime mame = null;

        try {
            mame = new MameRuntime(
                System.getProperties().getProperty("iamame.mame.binary"));

        } catch (IOException | InterruptedException | ParseException e) {
            IaMame.errorAndExit(
                "An error occured while trying to execute Mame: " 
                    + e.getMessage());
        }

        MachineRepository mf = new MachineRepository(mame);
        SoftwareRepository sr = new SoftwareRepository(mame);

        try {
            mameArgs = new MameArguments(mf, sr, args);
            downloadFilesIfNeeded(mameArgs, mame);
        } catch (InvalidMameArgumentsException e) {
            IaMame.warn(
            "An error occured while trying to parse command line: " 
                + e.getMessage());
        } catch (IOException | InterruptedException e) {
            IaMame.errorAndExit(
                "An error occured while trying to parse command line: " 
                    + e.getMessage());
        }

        // Launch Mame if not in dry-run mode
        if (System.getProperty("iamame.dryrun").equals("0")) {
            try {
                mame.execute(args);
            } catch (IOException | InterruptedException e) {
                IaMame.errorAndExit(
                    "An error occured while trying to execute Mame: " 
                        + e.getMessage());
            }
        }

    }

    /**
     * Reads configuration file and store parameters on the 
     * system properties under the `iamame` namespace
     */
    public static void initConfiguration() {
        
        // Read and parse configuration file if exists
        if (System.getProperties().containsKey("iamame.configfile")) {

            String configFilePath 
                = System.getProperty("iamame.configfile");

            try {
                Properties configProperties = new Properties();
                configProperties.load(new FileInputStream( configFilePath ));

                for (Map.Entry<Object,Object> p : configProperties.entrySet()) {
                    System.getProperties().setProperty(
                        "iamame." + p.getKey(), 
                        (String) p.getValue());
                }

            } catch (IOException e) {
                IaMame.debug(String.format(
                    "Configuration file not found: %s",
                    configFilePath));
            }

        }

        // Search for Mame binary path on the configuration
        // and test if exists
        if (System.getProperties().containsKey("iamame.mame.binary")) {
            String mameBinaryPath 
                = System.getProperty("iamame.mame.binary");

            IaMame.debug(String.format("Try to find Mame at: %s", 
                mameBinaryPath));
            File mameBinary = new File(mameBinaryPath);
            if (mameBinary.exists() && mameBinary.canExecute()) {
                IaMame.debug(String.format(
                    "Mame binary found: %s", 
                    mameBinaryPath));
                return;
            }
        }

        // Search for Mame binary on the PATH Environment variable
        String pathEnvVar = System.getenv("PATH");
                IaMame.debug(String.format(
                    "Search for Mame binary on $PATH: %s", 
                    pathEnvVar));

        String[] possibleBinaryNames 
            = {"mame", "mame64", "mame.exe", "mame64.exe"};

        for (String p : pathEnvVar.split(":")) {
            for (String b: possibleBinaryNames) {
                String mbp = p + File.separator + b;
                File mb = new File(mbp);
                if (mb.exists() && mb.canExecute()) {
                    System.setProperty("iamame.mame.binary", mbp);
                    IaMame.debug(String.format("Mame binary found: %s", mbp));
                    return;
                }
            }

        }
        
        IaMame.errorAndExit("Mame executable as not been found.");

    }

    private static void downloadFilesIfNeeded (
            MameArguments mameArgs, 
            MameRuntime mame) {
    
        if (!mameArgs.containsCommand() && mameArgs.hasMachine()) {

            Machine machine = mameArgs.getMachine();

            MessAndMameCollection mamc = null;
            try {
                mamc = new MessAndMameCollection(
                    mame.getRomsPaths(),
                    mame.getWritableRomPath());
            } catch (NoWritableRomPathException e) {
                IaMame.errorAndExit(e.getMessage());
            }

            if (!machine.areRomFilesAvailable(mame.getRomsPaths())) {

                IaMame.info(String.format(
                    "Download from archive.org missing " 
                        + "rom files: %s for machine \"%s\"", 
                    machine.getMissingRomFiles(mame.getRomsPaths()), 
                    machine.getDescription()));

                try {
                    mamc.download(machine);
                } catch (MachineRomFileNotFoundInCollection e) {
                    IaMame.warn(e.getMessage());
                }
            } 

            if (mameArgs.hasSoftwares()) {
            
                List<Software> softwares = mameArgs.getSoftwares();

                for (Software s: softwares) {
                    if (!s.isRegularFile()
                           && !s.isAvailable(mame.getRomsPaths())) {

                        IaMame.info(String.format(
                            "Download from archive.org missing " 
                                + "software file: %s)",
                            s));

                        try {
                            mamc.download(s);
                        } catch (SoftwareFileNotFoundInCollectionException e) {
                            IaMame.warn(e.getMessage());
                        }

                    }

                }
            
            }

        }
    
    }

    public static void errorAndExit (String message) {
        System.err.println("ERROR: " + message);
        System.exit(1);
    }

    public static void debug (String message) {
        if (System.getProperties().containsKey("iamame.debug")
                && System.getProperty("iamame.debug").equals("1")) {
            System.err.println("DEBUG: " + message);
        }
    }

    public static void warn (String message) {
        System.err.println("WARN: " + message);
    }

    public static void info (String message) {
        System.out.println("INFO: " + message);
    }

}
