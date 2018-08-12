package org.tibennetwork.iarcade.internetarchive;

import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;

/**
 * A CHD file in a machine CHD set
 */
public class MachineChdSetFile {

  @XmlElement(name = "collectionId")
  private final String collectionId;

  @XmlElement(name = "machine")
  private final String machine;

  @XmlElement(name = "chd")
  private final String chd;

  @XmlElement(name = "url")
  private final URL url;

  @XmlElement(name = "size")
  private final long size;

  public MachineChdSetFile(String collectionId, String machine, String chd,
      URL url, long size) {
    this.collectionId = collectionId;
    this.machine = machine;
    this.chd = chd;
    this.url = url;
    this.size = size;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public String getMachine() {
    return machine;
  }

  public String getChd() {
    return chd;
  }

  public URL getUrl() {
    return url;
  }

  public long getSize() {
    return size;
  }

  /**
   * Return the filename
   */
  public String getName() {

    String decodedUrl = null;

    try {

      decodedUrl = URLDecoder.decode(url.toString(), "UTF-8");

    } catch (Exception e) {
      RuntimeException re = new RuntimeException();
      re.initCause(e);
    }

    Pattern p = Pattern.compile(".+(/[^/]+/[^/]+\\.chd)");
    Matcher m = p.matcher(decodedUrl);
    if (m.matches()) {
      return m.group(1);
    } else {
      throw new RuntimeException("Can't extract file name from url: " + url);
    }

  }

  public String toString() {
    StringBuilder builder = new StringBuilder("MachineRomSetFile: [machine: ");
    builder.append(machine);
    builder.append(", chd: ");
    builder.append(chd);
    builder.append(", url: ");
    builder.append(url);
    builder.append(", size: ");
    builder.append(size);
    return builder.toString();
  }

  /**
   * Empty constructor needed by JAXB
   */

  public MachineChdSetFile() {
    this.collectionId = null;
    this.machine = null;
    this.chd = null;
    this.url = null;
    this.size = 0;
  }

}
