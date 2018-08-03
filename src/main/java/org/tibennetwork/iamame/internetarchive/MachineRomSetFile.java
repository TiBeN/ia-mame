package org.tibennetwork.iamame.internetarchive;

import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A rom file in a machine rom set
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MachineRomSetFile {

  @XmlElement(name = "collectionId")
  private final String collectionId;

  @XmlElement(name = "machineName")
  private final String machineName;

  @XmlElement(name = "url")
  private final URL url;

  @XmlElement(name = "size")
  private final long size;

  public MachineRomSetFile(String collectionId, String machineName, URL url,
      long size) {
    this.collectionId = collectionId;
    this.machineName = machineName;
    this.url = url;
    this.size = size;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public String getMachineName() {
    return machineName;
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

    Pattern p = Pattern.compile(".+/([^/]+\\.zip)");
    Matcher m = p.matcher(decodedUrl);
    if (m.matches()) {
      return m.group(1);
    } else {
      throw new RuntimeException("Can't extract file name from url: " + url);
    }

  }

  public String toString() {
    StringBuilder builder =
        new StringBuilder("MachineRomSetFile: [machineName: ");
    builder.append(machineName);
    builder.append(", collectionId: ");
    builder.append(collectionId);
    builder.append(", url: ");
    builder.append(url);
    builder.append(", size: ");
    builder.append(size);
    return builder.toString();
  }

  /**
   * Empty constructor needed by JAXB
   */

  public MachineRomSetFile() {
    this.collectionId = null;
    this.machineName = null;
    this.url = null;
    this.size = 0;
  }

}
