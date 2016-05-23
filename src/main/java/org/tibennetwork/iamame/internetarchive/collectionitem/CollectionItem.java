package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Item (ie subcollection) of the Internet Archive 
 * Mame & Mess Collection. 
 */
public abstract class CollectionItem {

    protected List<File> romsPaths;

    protected File writableRomPath;

    /**
     * Runnable class for fetching the size length of a
     * collection item file in a separate thread.
     */
    private static class FileLengthFetcher implements Runnable {

        public int length = 0;

        private String romFileUrl;

        public FileLengthFetcher (String romFileUrl) {
            super();
            this.romFileUrl = romFileUrl;
        }

        public void run () {
            try {
                this.length = getFileLength(this.romFileUrl);
            } catch (IOException | NullPointerException e) {
                this.length = 0;
            }
        }

    }    

    /**
     * Length of the file to download in kB
     * This attribute is set here because it is shared 
     * between threads
     */
    int length = 0; 

    public CollectionItem (
            List<File> romsPaths, 
            File writableRomPath) {
    
        this.romsPaths = romsPaths;
        this.writableRomPath = writableRomPath;
    
    };

    protected void downloadFile (String romFileUrl, String destinationPath) 
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
            int downloadedPercent = 0;

            byte[] bytes = new byte[1024];

            // Try to obtain the length in a separate thread
            FileLengthFetcher flf = new FileLengthFetcher(romFileUrl);
            Thread fileLengthFetchingThread = new Thread(flf);
            fileLengthFetchingThread.start();

            // Download the file
            while ((readBytes = urlInputStream.read(bytes)) != -1) {
                destinationOutputStream.write(bytes, 0, readBytes);

                // Update the download progress output display
                downloadedBytes += readBytes;
                if (flf.length != 0) {
                    downloadedPercent 
                        = (int)((float)((float)(downloadedBytes / 1024) / flf.length) * 100);
                }
                System.out.print(String.format(
                    "INFO: Downloading %s - %skB / %skB, progress: %s\r",
                    destination.getName(),
                    (downloadedBytes / 1024),
                    flf.length != 0 ? flf.length : "??",
                    (readBytes = urlInputStream.read(bytes)) != -1
                        ? (downloadedPercent + "%") 
                        : "...COMPLETED\n"));
            }

            // Stop the length fetching thread
            fileLengthFetchingThread.interrupt();

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

    /**
     * Get the file length a collection item
     * This makes use of the "zipview" feature of archive.org
     */
    static int getFileLength (String romFileUrl) 
            throws IOException {

        // The rom file url is splitted to extract the zipview url
        // of the collection and the name of the file on the zip 
        // file
        int splitIndex = (romFileUrl.indexOf(".zip/") + 5);
        String zipViewUrl = romFileUrl.substring(0, splitIndex);
        String fileTitle = romFileUrl.substring(splitIndex, romFileUrl.length());

        // Parse and extract the size
        Document zipView = Jsoup.connect(zipViewUrl).get();
        String size = zipView.select("a:contains(" + fileTitle + ")")
            .first()
            .parent()
            .parent()
            .select("#size")
            .first()
            .text();

        return (Integer.parseInt(size)/1024);

    }

}
