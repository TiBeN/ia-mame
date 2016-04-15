package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

        // Create containing directories if needed
        destination.getParentFile().mkdirs();

        InputStream urlInputStream = null;
        OutputStream destinationOutputStream = null;

        try {
        
            urlInputStream = romFile.openStream();
            destinationOutputStream 
                = new FileOutputStream(destination);
        
            int readBytes = 0;
            int downloadedBytes = 0; // Accumulated read bytes

            byte[] bytes = new byte[1024];

            while ((readBytes = urlInputStream.read(bytes)) != -1) {
                downloadedBytes += readBytes;
                destinationOutputStream.write(bytes, 0, readBytes);
                System.out.print(String.format(
                    "Downloading %skB / ??kB, progress: ??\r",
                    (downloadedBytes / 1024)));
            }

            urlInputStream.close();
            destinationOutputStream.close();

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
