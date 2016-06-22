package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Software;

public class MessChdCd32 extends SoftwareListCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_cd32/" 
            + "MESS_0.149_CHD_cd32.zip/MESS_0.149_CHD_cd32/%1$s/%2$s.chd";

    public MessChdCd32 (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public void download (Software software) 
            throws FileNotFoundInCollectionItem {
        
        String softwareName = software.getName();
        String chdName = software.getChdName();
        
        String softwareFileUrl = String.format(
            this.softwareFileUrlPattern,
            softwareName,
            chdName);

        String destinationPath = this.writableRomPath.getAbsolutePath()
            + File.separator
            + software.getRelativeFilePath();

        this.downloadFile(softwareFileUrl, destinationPath);

    }
}
