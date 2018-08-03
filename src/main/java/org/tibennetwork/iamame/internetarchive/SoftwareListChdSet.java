package org.tibennetwork.iamame.internetarchive;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.tibennetwork.iamame.IaMame;
import org.tibennetwork.iamame.mame.MameVersion;

public class SoftwareListChdSet extends RomSet {

  static {
    SoftwareListChdSet.versions = new TreeSet<>();
    SoftwareListChdSet.versions.add(new MameVersion("0.149"));
  }

  public static TreeSet<MameVersion> versions;

  /**
   * Get the best SoftwareListChdSet for the given Mame version.
   *
   * "Best" means equal or greatest inferior to given Mame version.
   */
  public static SoftwareListChdSet findBest(MameVersion version) {

    MameVersion bestVersion;

    if (versions.contains(version)) {
      bestVersion = version;
    } else {
      bestVersion = versions.floor(version);
      StringBuilder builder = new StringBuilder("Your Mame version (");
      builder.append(version);
      builder.append(") does not match any of available softwarelist CHD sets"
          + " available at archive.org");
      IaMame.warn(builder.toString());
      IaMame.warn(
          "IaMame will try to download files on the closest CHD set.");
      IaMame.warn("Some files might not be available");
    }

    // Unmarshal SoftwareListChdSet from XML data stored in classpath

    StringBuilder builder = new StringBuilder("softwarelist-chd-set/");
    builder.append(bestVersion);
    builder.append(".xml");

    InputStream stream = SoftwareListChdSet.class.getClassLoader()
        .getResourceAsStream(builder.toString());

    SoftwareListChdSet chdSet =
        JAXB.unmarshal(stream, SoftwareListChdSet.class);

    return chdSet;

  }

  /**
   * First Map String index: software list Second Map String index: software
   * third: chd
   */
  @XmlJavaTypeAdapter(SoftwareListChdSetFileXmlAdapter.class)
  private final Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> files;

  public SoftwareListChdSet(MameVersion version, Set<Collection> collections,
      Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> files) {
    super(version, collections);
    this.files = files;
  }

  public boolean contains(String softwareList, String software, String chd) {
    if (files.containsKey(softwareList)) {
      if (files.get(softwareList).containsKey(software)) {
        if (files.get(softwareList).get(software).containsKey(chd)) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void download(File writableRomPath, String softwareList,
      String software, String chdName) {

    SoftwareListChdSetFile chd =
        files.get(softwareList).get(software).get(chdName);

    Collection col = getCollection(chd.getCollectionId());
    StringBuilder builder;

    builder = new StringBuilder("Found ");
    builder.append(chd.getName());
    builder.append(" on IA collection ");
    builder.append(col.getName());
    builder.append(" (");
    builder.append(col.getUrl());
    builder.append(")");

    IaMame.info(builder.toString());

    // Determine destination path

    builder = new StringBuilder(writableRomPath.getAbsolutePath());
    builder.append(File.separator);
    builder.append(chd.getName());
    String destPath = builder.toString();

    downloadFile(chd.getUrl(), destPath, chd.getSize());
  }

  /**
   * Empty constructor needed by JAXB
   */
  public SoftwareListChdSet() {
    super(null, null);
    this.files = null;
  }

}
