package org.tibennetwork.iamame.mame;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * Set of methods which scan mame roms paths
 * to determine if files are presents or not
 */
public class RomsPathsScanner {

    private List<File> romsPaths;

    public RomsPathsScanner (List<File> romsPaths) {
        this.romsPaths = romsPaths;
    }

    /**
     * Determines whether the rom file is present on 
     * the rompath
     */
    public Boolean containsRomSet (String romSet) {

        for (File romPath: this.romsPaths) {
            File romSetFile = new File(
                romPath.getAbsolutePath() 
                    + File.separator 
                    + romSet
                    + ".zip");

            if (romSetFile.exists()) {
                return true;
            }

        }

        return false;
                    
    }

    /**
     * Determines if the given file name is 
     * a regular software file (not a softwarelist name)
     */
    public Boolean isARegularSoftwareFile (String softwareFilename) {

        // Check file extension or
        if (!FilenameUtils.getExtension(softwareFilename).isEmpty()
                || new File(softwareFilename).exists()) {
            return true;
        } 

        return false;
    }

    public Boolean containsSoftware (Machine mameSystem, String software) {
        
        for (File romPath: this.romsPaths) {

            for (SoftwareList sl: mameSystem.getSoftwareLists()) {
                String softwareFile = romPath.getAbsolutePath() 
                    + File.separator
                    + sl.getName()
                    + File.separator
                    + software
                    + ".zip";

                if (new File(softwareFile).exists()) {
                    return true;                    
                }

            }

        }

        return false;

    }

}
