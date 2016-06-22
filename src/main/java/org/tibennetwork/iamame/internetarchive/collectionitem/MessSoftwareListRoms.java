package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Software;

public class MessSoftwareListRoms extends SoftwareListCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.151_Software_List_ROMs/" 
            + "%1$s.zip/MESS 0.151 Software List ROMs/%1$s/%2$s.zip";

    public MessSoftwareListRoms (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public void download (Software software) 
            throws FileNotFoundInCollectionItem {
        
        String softwareListName = software.getSoftwareList().getName();
        String softwareName = software.getName();
        
        String softwareFileUrl = String.format(
            this.softwareFileUrlPattern,
            softwareListName,
            softwareName);

        String destinationPath = this.writableRomPath.getAbsolutePath()
            + File.separator
            + software.getRelativeFilePath();

        this.downloadFile(softwareFileUrl, destinationPath);

    }

}
