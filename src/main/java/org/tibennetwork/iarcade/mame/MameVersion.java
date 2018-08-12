package org.tibennetwork.iarcade.mame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Version of Mame binary
 */
  @XmlJavaTypeAdapter(MameVersionXmlAdapter.class)
public class MameVersion implements Comparable<MameVersion> {

  private String version;

  private int major;

  private int minor;

  public MameVersion(String version) {
    Pattern p = Pattern.compile("^([0-9]+)\\.([0-9]+).*$");
    Matcher m = p.matcher(version);
    if (!m.matches()) {
      StringBuilder builder = new StringBuilder("'");
      builder.append(version);
      builder.append("' is not a correct Mame version String format");
      throw new IllegalArgumentException(builder.toString());
    }
    this.version = version;
    this.major = Integer.parseInt(m.group(1));
    this.minor = Integer.parseInt(m.group(2));
  }

  /**
   * Factory method that create a new MameVersion by parsing the version of Mame
   * from the first line of the output of the -help command
   */
  public static MameVersion parseFromHelpOutput(String helpCommandFirstLine)
      throws UnhandledMameVersionPatternException {
    Pattern p = Pattern.compile("^.*v([0-9]+)\\.([0-9]+).*$");
    Matcher m = p.matcher(helpCommandFirstLine);
    if (!m.matches()) {
      throw new UnhandledMameVersionPatternException(
          "Can't parse version from output: \"" + helpCommandFirstLine + "\"");
    }

    return new MameVersion(m.group(1) + "." + m.group(2));

  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getMajor() {
    return major;
  }

  public void setMajor(int major) {
    this.major = major;
  }

  public int getMinor() {
    return minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public int compareTo(MameVersion version) {
    if (major == version.getMajor()) {
      return minor - version.getMinor();
    }
    return major - version.getMajor();
  }

  public String toString() {
    return version;
  }

}
