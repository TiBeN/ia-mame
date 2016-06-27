package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Software;

public class MessChdSaturn extends SoftwareListCollectionItem {

    private String[] softwareFileUrlPatterns = {
        "http://archive.org/download/MESS_0.149_CHD_saturn_1/" 
            + "MESS_0.149_CHD_saturn_1.zip/MESS_0.149_CHD_saturn_1/" 
            + "%1$s/%2$s.chd",
        "http://archive.org/download/MESS_0.149_CHD_saturn_2/" 
            + "MESS_0.149_CHD_saturn_2.zip/MESS_0.149_CHD_saturn_2/" 
            + "%1$s/%2$s.chd",
        "http://archive.org/download/MESS_0.149_CHD_saturn_3/" 
            + "MESS_0.149_CHD_saturn_3.zip/MESS_0.149_CHD_saturn_3/" 
            + "%1$s/%2$s.chd"
    };

    public MessChdSaturn (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public void download (Software software) 
            throws FileNotFoundInCollectionItem {
        
        String softwareName = software.getName();
        String chdName = software.getChdName();
       
        for (String sfup: this.softwareFileUrlPatterns) {
            String softwareFileUrl = String.format(
                sfup,
                softwareName,
                chdName);

            String destinationPath = this.writableRomPath.getAbsolutePath()
                + File.separator
                + software.getRelativeFilePath();

            try {
                this.downloadFile(softwareFileUrl, destinationPath);
                return;
            } catch (FileNotFoundInCollectionItem e) {
                // It's ok, it could be into another collection part
            }

        }

        throw new FileNotFoundInCollectionItem(
            "The files have not been found on collection");

    }

}
