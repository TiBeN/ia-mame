package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Machine;

public class MameChds extends MachineRomsCollectionItem {

    private String[] chdFileUrlPatterns = {
        "http://archive.org/download/MAME_0.149_CHDs_A-B/"
            + "MAME_0.149_CHDs_A-B.zip/MAME_0.149_CHDs_A-B/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_C/"
            + "MAME_0.149_CHDs_C.zip/MAME_0.149_CHDs_C/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_D-G/"
            + "MAME_0.149_CHDs_D-G.zip/MAME_0.149_CHDs_D-G/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_H-N/"
            + "MAME_0.149_CHDs_H-N.zip/MAME_0.149_CHDs_H-N/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_H-N/"
            + "MAME_0.149_CHDs_H-N.zip/MAME_0.149_CHDs_H-N/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_P-S/"
            + "MAME_0.149_CHDs_P-S.zip/MAME_0.149_CHDs_P-S/%1$s/%2$s.chd",
        "http://archive.org/download/MAME_0.149_CHDs_U-Z/"
            + "MAME_0.149_CHDs_U-Z.zip/MAME_0.149_CHDs_U-Z/%1$s/%2$s.chd",
    };
            
    public MameChds (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public void download (Machine machine) 
            throws FileNotFoundInCollectionItem {

        chdLoop: 
        for (String chdFile: machine.getMissingChdFiles(this.romsPaths)) {

            for (String chdFileUrlPattern: this.chdFileUrlPatterns) {

                String chdFileUrl = String.format(
                    chdFileUrlPattern,
                    machine.getName(),
                    chdFile);
                String destinationPath = this.writableRomPath.getAbsoluteFile()
                    + File.separator
                    + machine.getName()
                    + File.separator
                    + chdFile
                    + ".chd";

                try {
                    this.downloadFile(chdFileUrl, destinationPath);
                    continue chdLoop;
                } catch (FileNotFoundInCollectionItem e) {
                    //It's ok, it could be into another collection part
                }
            }
            
            throw new FileNotFoundInCollectionItem(
                "The chd file '" + chdFile 
                    + "' have not been found on collection");
        }
        
    }




}
