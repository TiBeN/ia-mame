package org.tibennetwork.iamame.mame;

/**
 * Model a file needed by a Software from softwarelist
 * rom file needed by a system (zip or chd)
 */
public class SoftwareFile {

    /**
     * File path relative to the rompath
     */
    private String relativeFilePathWithoutExtension;

    private boolean isChdFile;

    public SoftwareFile (String relativeFilePathWithoutExtension, 
            boolean isChdFile) {
        
        this.relativeFilePathWithoutExtension 
            = relativeFilePathWithoutExtension;
        this.isChdFile = isChdFile;
    
    }

    public String getRelativeFilePathWithoutExtension() {
        return relativeFilePathWithoutExtension;
    }

    public boolean isChdFile() {
        return isChdFile;
    }

    public String getRelativeFilePath () {
        return this.relativeFilePathWithoutExtension
            + (isChdFile ? ".chd" : ".zip");
    }

    public String toString() {
        return String.format("SoftwareFile[file: %s, chd: %s]",
            this.relativeFilePathWithoutExtension, 
            this.isChdFile);
    }

    public boolean equals (Object obj) {
        if (obj instanceof SoftwareFile) {
            
            SoftwareFile sfToCompare = (SoftwareFile) obj;
            
            return this.getRelativeFilePath().equals(
                sfToCompare.getRelativeFilePath())
                    && this.isChdFile() == sfToCompare.isChdFile();
        }
        else {
            return false;
        }
    }

    public int hashCode () {
        int hash = (this.isChdFile ? 7 : 0);
        for (int i = 0; i < relativeFilePathWithoutExtension.length(); i++) {
            hash = hash*31 + relativeFilePathWithoutExtension.charAt(i);
        }
        return hash;
    }

}
