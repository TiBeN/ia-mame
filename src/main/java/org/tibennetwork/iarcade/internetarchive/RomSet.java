package org.tibennetwork.iarcade.internetarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.tibennetwork.iarcade.IaRcade;
import org.tibennetwork.iarcade.mame.MameVersion;

/**
 * Base class for differents kins of Mame Romsets (ROMs, CHDs, softwarelist..)
 */
public abstract class RomSet {

  @XmlElement(name = "version")
  protected final MameVersion version;

  /**
   * Internet Archive collections
   */
  @XmlElementWrapper(name = "collections")
  @XmlElement(name = "collection")
  protected final Set<Collection> collections;

  public RomSet(MameVersion version, Set<Collection> collections) {
    this.version = version;
    this.collections = collections;
  }

  public MameVersion getVersion() {
    return version;
  }

  public Set<Collection> getCollections() {
    return collections;
  }

  protected Collection getCollection(String collectionId) {
    Collection col = null;
    for (Collection c : collections) {
      if (c.getId().equals(collectionId)) {
        col = c;
        break;
      }
    }
    return col;
  }

  /**
   * Download the url file to the dest path. Size is the file size in octet. It
   * allows to compute download progression.
   */
  protected void downloadFile(URL url, String destPath, long size) {

    File dest = new File(destPath);

    IaRcade.debug("URL: " + url);

    // Create containing directories if needed

    dest.getParentFile().mkdirs();

    InputStream urlStream = null;
    OutputStream destStream = null;

    try {

      urlStream = url.openStream();
      destStream = new FileOutputStream(dest);

      int readBytes = 0;
      int downloadedBytes = 0; // Accumulated read bytes
      int downloadedPercent = 0;

      byte[] bytes = new byte[1024];

      boolean downloadingMessageDisplayed = false;

      // Convert size to kB

      size = size / 1024;

      // Download the file

      String destName = dest.getName();
      String ellipsisedName =
          destName.length() > 18 ? destName.substring(0, 15) + "..." : destName;
      String downloadPrefix = "[ia-rcade info]: Downloading " + ellipsisedName;

      while ((readBytes = urlStream.read(bytes)) != -1) {
        destStream.write(bytes, 0, readBytes);

        // Update the download progress status message

        downloadedBytes += readBytes;
        downloadedPercent =
            (int) ((float) ((float) (downloadedBytes / 1024) / size) * 100);

        System.out
            .print(String.format("%s - %skB / %skB (%s)\r", downloadPrefix,
                (downloadedBytes / 1024), size, (downloadedPercent + "%")));

        downloadingMessageDisplayed = true;

      }

      if (downloadingMessageDisplayed) {
        System.out.println(downloadPrefix
            + " ...DONE                                          ");
      }

      urlStream.close();
      destStream.close();

    } catch (IOException e) {
      throw (RuntimeException) new RuntimeException().initCause(e);
    }

  }
}
