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

public class MachineChdSet extends RomSet {

  static {
    MachineChdSet.versions = new TreeSet<>();
    MachineChdSet.versions.add(new MameVersion("0.149"));
    MachineChdSet.versions.add(new MameVersion("0.161"));
    MachineChdSet.versions.add(new MameVersion("0.185"));
    MachineChdSet.versions.add(new MameVersion("0.193"));
  }

  public static TreeSet<MameVersion> versions;

  /**
   * First Map String index: machineName Second Map String index: chd
   */
  @XmlJavaTypeAdapter(MachineChdSetFileXmlAdapter.class)
  private final Map<String, Map<String, MachineChdSetFile>> files;

  public MachineChdSet(MameVersion version, Set<Collection> collections,
      Map<String, Map<String, MachineChdSetFile>> files) {
    super(version, collections);
    this.files = files;
  }

  /**
   * Get the best MachineChdSet for the given Mame version.
   *
   * "Best" means equal or greatest inferior to given Mame version.
   */
  public static MachineChdSet findBest(MameVersion version) {

    MameVersion bestVersion;

    if (versions.contains(version)) {
      bestVersion = version;
    } else {
      bestVersion = versions.floor(version);
      StringBuilder builder = new StringBuilder("Your Mame version (");
      builder.append(version);
      builder.append(") does not match any of available machine CHD sets"
          + " available at archive.org");
      IaMame.warn(builder.toString());
      IaMame.warn(
          "IaMame will try to download files on the closest machine CHD set.");
      IaMame.warn("Some files might not be available");
    }

    // Unmarshal MachineRomSet from XML data stored in classpath

    StringBuilder builder = new StringBuilder("machine-chd-set/");
    builder.append(bestVersion);
    builder.append(".xml");

    InputStream stream = MachineChdSet.class.getClassLoader()
        .getResourceAsStream(builder.toString());

    MachineChdSet chdSet = JAXB.unmarshal(stream, MachineChdSet.class);

    return chdSet;

  }

  public boolean contains(String machine, String chd) {
    if (files.containsKey(machine)) {
      if (files.get(machine).containsKey(chd)) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void download(File writableRomPath, String machineName,
      String chdName) {

    MachineChdSetFile chd = files.get(machineName).get(chdName);

    Collection col = getCollection(chd.getCollectionId());
    StringBuilder builder;

    builder = new StringBuilder("Found ");
    builder.append(chd.getChd());
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
  public MachineChdSet() {
    super(null, null);
    this.files = null;
  }

}
