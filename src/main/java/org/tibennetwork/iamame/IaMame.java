package org.tibennetwork.iamame;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.tibennetwork.iamame.internetarchive.MachineRomFileNotFoundInCollection;
import org.tibennetwork.iamame.internetarchive.MessAndMameCollection;
import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;
import org.tibennetwork.iamame.internetarchive.collectionitem.SoftwareFileNotFoundInCollectionException;
import org.tibennetwork.iamame.mame.InvalidMameArgumentsException;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.MachineRepository;
import org.tibennetwork.iamame.mame.MameArguments;
import org.tibennetwork.iamame.mame.MameArguments.ExtractedMachineAndSoftwares;
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

        MameArguments mameArgs = null;

        try {
            mameArgs = new MameArguments(args);
        } catch (InvalidMameArgumentsException e) {
            IaMame.warn(
            "An error occured while trying to parse command line: " 
                + e.getMessage());
        }
            
        MameRuntime mame = null;
        String mameBinary = null;       

        try {
            mameBinary = IaMame.findMameBinary();
        } catch (MameBinaryNotFoundException e) {
            IaMame.errorAndExit(e.getMessage());
        }

        try {
            mame = new MameRuntime(mameBinary, mameArgs.getRawOptionsArgs());
        } catch (IOException | InterruptedException | ParseException e) {
            IaMame.errorAndExit(
                "An error occured while trying to execute Mame: " 
                    + e.getMessage());
        }

        downloadFilesIfNeeded(mameArgs, mame);

        // Launch Mame if not in dry-run mode
        String dryRun = System.getProperty("iamame.dryrun");
        if (dryRun == null || !dryRun.equals("1")) {
            try {
                mame.execute(args, false);
            } catch (IOException | InterruptedException e) {
                IaMame.errorAndExit(
                    "An error occured while trying to execute Mame: " 
                        + e.getMessage());
            }
        }

    }

    /**
     * Find and return as String the path to the MAME executable
     */
    public static String findMameBinary()
            throws MameBinaryNotFoundException {

        // Check the MAME_EXEC environment variable
        String mameExecEnvVar = System.getenv("MAME_EXEC");
        IaMame.debug(String.format("$MAME_EXEC: %s", mameExecEnvVar));
        if (mameExecEnvVar != null) {
            File mameBinary = new File(mameExecEnvVar);
            if (mameBinary.exists() && mameBinary.canExecute()) {
                IaMame.debug("MAME binary found on MAME_EXEC" 
                    + " environment variable");
                return mameExecEnvVar;
            }
        }

        String[] possibleBinaryNames 
            = {"mame", "mame64", "mame.exe"};
        String candidateMameBinaryPath;
        File candidateMameBinary;
        String containingJarPath = null;

        // Search for mame binary on the directory containing the
        // containing jar.         
        try {
            containingJarPath = new File(
                IaMame.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath())
                .getParent();
            IaMame.debug(String.format(
                "IaMame directory: %s",
                containingJarPath));
        } catch (URISyntaxException e) {
            // If entering in, its a bug.
            IaMame.debug(e.getMessage());
        }

        for (String b: possibleBinaryNames) {
            candidateMameBinaryPath = containingJarPath + File.separator + b;
            candidateMameBinary = new File(candidateMameBinaryPath);
            if (candidateMameBinary.exists() 
                    && candidateMameBinary.canExecute()) {
                IaMame.debug(String.format(
                    "Mame binary found on the same directory than IaMame: %s", 
                    candidateMameBinaryPath));
                return candidateMameBinaryPath;
            }
        }

        // Search for Mame binary on the PATH environment variable
        String pathEnvVar = System.getenv("PATH");
        IaMame.debug(String.format("$PATH: %s", pathEnvVar));

        for (String p : pathEnvVar.split(":")) {
            for (String b: possibleBinaryNames) {
                candidateMameBinaryPath = p + File.separator + b;
                candidateMameBinary = new File(candidateMameBinaryPath);
                if (candidateMameBinary.exists() 
                        && candidateMameBinary.canExecute()) {
                    IaMame.debug(String.format(
                        "Mame binary found on $PATH : %s", 
                        candidateMameBinaryPath));
                    return candidateMameBinaryPath;
                }
            }

        }

        throw new MameBinaryNotFoundException(
            "MAME executable has not been found");

    }

    private static void downloadFilesIfNeeded (
            MameArguments mameArgs, 
            MameRuntime mame) {

    
        if (!mameArgs.containsCommand() ) {

            ExtractedMachineAndSoftwares ems = null;
            
            try {
                ems = mameArgs.extractMachineAndSoftwares(
                    new MachineRepository(mame), 
                    new SoftwareRepository(mame));
            } catch (InvalidMameArgumentsException e) {
                IaMame.errorAndExit(
                    "An error occured while trying to parse command-line: " 
                        + e.getMessage());
            }  
            catch (IOException | InterruptedException e) {
                IaMame.errorAndExit(
                    "An error occured " + e.getMessage());
            }  

            Machine machine = ems.getMachine();

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
                    "Download missing rom files: %s", 
                    machine.getMissingRomFiles(mame.getRomsPaths())));

                IaMame.info(String.format("Machine: %s", 
                    machine.getDescription()));

                try {
                    mamc.download(machine);
                } catch (MachineRomFileNotFoundInCollection e) {
                    IaMame.warn(e.getMessage());
                }
            } 

            if (ems.hasSoftwares()) {
            
                List<Software> softwares = ems.getSoftwares();

                for (Software s: softwares) {
                    if (!s.isRegularFile()
                           && !s.isAvailable(mame.getRomsPaths())) {

                        IaMame.info(String.format(
                            "Download missing software file: %s",
                            s.getName()));

                        IaMame.info(String.format("Name: %s", 
                            s.getDescription()));

                        IaMame.info(String.format("Publisher: %s", 
                            s.getPublisher()));

                        IaMame.info(String.format("Media interface: %s", 
                            s.getMediaInterface()));

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
