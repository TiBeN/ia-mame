package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Software;
import org.tibennetwork.iamame.mame.SoftwareFile;

/**
 * Abstract for Generic Archive Mess Chd Collections.
 */ 
public abstract class GenericChdCollectionItem 
        extends SoftwareListCollectionItem {

    public GenericChdCollectionItem (Set<File> romsPaths, 
            File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    /**
     * Return the pattern of the url to download software
     * from archive.org
     */
    abstract public String getSoftwareFileUrlPattern ();

    public void download (Software software) 
            throws FileNotFoundInCollectionItem {

        for (SoftwareFile sf: software.getMissingChdFiles(romsPaths)) {

            String softwareFileUrl = String.format(
                this.getSoftwareFileUrlPattern(),
                software.getName(),
                sf.getName());

            String destinationPath = this.writableRomPath.getAbsolutePath()
                + File.separator
                + sf.getRelativeFilePath();

            this.downloadFile(softwareFileUrl, destinationPath);

        }

    }
}
