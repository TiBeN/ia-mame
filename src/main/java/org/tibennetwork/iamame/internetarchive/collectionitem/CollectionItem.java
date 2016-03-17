package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Item (ie subcollection) of the Internet Archive 
 * Mame & Mess Collection. 
 */
public abstract class CollectionItem {

    protected List<File> romsPaths;

    protected File writableRomPath;

    public CollectionItem (
            List<File> romsPaths, 
            File writableRomPath) {
    
        this.romsPaths = romsPaths;
        this.writableRomPath = writableRomPath;
    
    };

    protected void downloadFile(String romFileUrl, String destinationPath) 
            throws FileNotFoundInCollectionItem {

        URL romFile = null;
        File destination = new File(destinationPath);

        try {
            romFile = new URL(romFileUrl);
        } catch (MalformedURLException e) {
            throw (RuntimeException) new RuntimeException(
                    "Runtime error: Can't instanciate URL:")
                .initCause(e);
        }

        try {
            FileUtils.copyURLToFile(romFile, destination);
        } catch (IOException e) {
            throw (RuntimeException) new RuntimeException()
                .initCause(e);
        }

        // Instead of returning a 4xx error when the file didn't exists
        // Internet Archive return a 200 response resulting an
        // empty file. To know if the file really exists, we check
        // if the downloaded file is empty.
        if (destination.length() == 0) {
            destination.delete();
            throw new FileNotFoundInCollectionItem(
                "The File at given URL doesn\'t exist: " + romFileUrl);
        }
    
    }

}
