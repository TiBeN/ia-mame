package org.tibennetwork.iamame;

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
     * Reads configuration file and store parameters on the 
     * system properties under the `iamame` namespace
     */
    static {
        
        if(!System.getProperties().containsKey("iamame.configfile")) {
            IaMame.errorAndExit("iamame.configfile System property not set");
        }

        String configFilePath 
            = System.getProperties().getProperty("iamame.configfile");
        
        try {
            Properties configProperties = new Properties();
            configProperties.load(new FileInputStream( configFilePath ));

            for (Map.Entry<Object,Object> p : configProperties.entrySet()) {
                System.getProperties().setProperty(
                    "iamame." + p.getKey(), 
                    (String) p.getValue());
            }

        } catch (IOException e) {
            IaMame.errorAndExit( 
                String.format("Filename: %s not found or can't be read", 
                    configFilePath));
        }

    }

    /**
     * ia-mame command-line entry-point
     */
    public static void main (String[] args)
    {
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
        } catch (InvalidMameArgumentsException
                | IOException
                | InterruptedException e) {
            IaMame.errorAndExit(
                "An error occued while trying to parse command line: " 
                    + e.getMessage());
        }

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

                System.out.println(String.format(
                    "Download from archive.org missing " 
                        + "rom files: %s for machine \"%s\"", 
                    machine.getMissingRomFiles(mame.getRomsPaths()), 
                    machine.getDescription()));

                try {
                    mamc.download(machine);
                } catch (MachineRomFileNotFoundInCollection e) {
                    IaMame.errorAndExit(e.getMessage());
                }
            } 

            if (mameArgs.hasSoftwares()) {
            
                List<Software> softwares = mameArgs.getSoftwares();

                for (Software s: softwares) {
                    if (!s.isRegularFile()
                           && !s.isAvailable(mame.getRomsPaths())) {

                        System.out.println(String.format(
                            "Download from archive.org missing " 
                                + "software file: %s)",
                            s));

                        try {
                            mamc.download(s);
                        } catch (SoftwareFileNotFoundInCollectionException e) {
                            IaMame.errorAndExit(e.getMessage());
                        }

                    }

                }
            
            }

        }

        // Launch Mame if not in dry-run mode
        if (System.getProperties().getProperty("iamame.dryrun").equals("0")) {
            try {
                mame.execute(mameArgs.getRawArgs());
            } catch (IOException | InterruptedException e) {
                IaMame.errorAndExit(
                    "An error occured while trying to execute Mame: " 
                        + e.getMessage());
            }
        }

    }

    public static void errorAndExit (String message) {
        System.err.println("ERROR: " + message);
        System.exit(1);
    }

}
