package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.List;

import org.tibennetwork.iamame.mame.Machine;

public class MessBios extends MachineRomsCollectionItem {

    private String romFileUrlPattern
        = "https://archive.org/download/MESS-0.151.BIOS.ROMs/" 
            + "MESS-0.151.BIOS.ROMs.zip/MESS 0.151 ROMs/%s.zip";

    public MessBios (List<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public void download (Machine machine) 
            throws FileNotFoundInCollectionItem {

        for (String r: machine.getMissingRomFiles(this.romsPaths)) {
            String romFileUrl = String.format(this.romFileUrlPattern, r);
            String destinationPath = this.writableRomPath.getAbsoluteFile()
                + File.separator
                + r
                + ".zip";
            this.downloadFile(romFileUrl, destinationPath);
        }
        
    }
}
