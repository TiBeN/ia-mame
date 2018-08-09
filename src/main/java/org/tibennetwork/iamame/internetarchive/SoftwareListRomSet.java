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

/**
 * A RomSet containing roms for softwares of emulated machines (cartridges,
 * disks etc.)
 */
public class SoftwareListRomSet extends RomSet {

  static {
    SoftwareListRomSet.versions = new TreeSet<>();
    SoftwareListRomSet.versions.add(new MameVersion("0.149"));
    SoftwareListRomSet.versions.add(new MameVersion("0.151"));
    SoftwareListRomSet.versions.add(new MameVersion("0.185"));
    SoftwareListRomSet.versions.add(new MameVersion("0.193"));
  }

  public static TreeSet<MameVersion> versions;

  @XmlJavaTypeAdapter(SoftwareListRomSetFileXmlAdapter.class)
  private final Map<String, Map<String, SoftwareListRomSetFile>> files;

  public SoftwareListRomSet(MameVersion version, Set<Collection> collections,
      Map<String, Map<String, SoftwareListRomSetFile>> files) {
    super(version, collections);
    this.files = files;
  }

  /**
   * Get the best SoftwareListRomSet for the given Mame version.
   *
   * "Best" means equal or greatest inferior to given Mame version.
   */
  public static SoftwareListRomSet findBest(MameVersion version) {

    MameVersion bestVersion;

    if (versions.contains(version)) {
      bestVersion = version;
    } else {
      bestVersion = versions.floor(version);
      IaMame.warn("No archive.org softlist ROM set matches your Mame version");
      IaMame.warn("Some files might not be available.");
    }

    // Unmarshal MachineRomSet from XML data stored in classpath

    StringBuilder builder = new StringBuilder("softwarelist-rom-set/");
    builder.append(bestVersion);
    builder.append(".xml");

    InputStream stream = SoftwareListRomSet.class.getClassLoader()
        .getResourceAsStream(builder.toString());

    SoftwareListRomSet romSet =
        JAXB.unmarshal(stream, SoftwareListRomSet.class);

    return romSet;

  }

  public Map<String, Map<String, SoftwareListRomSetFile>> getFiles() {
    return files;
  }

  /**
   * Determine if this rom set contains software roms
   */
  public boolean contains(String listName, String name) {
    Map<String, SoftwareListRomSetFile> softFiles = files.get(listName);
    if (softFiles == null) {
      return false;
    }
    return softFiles.containsKey(name);
  }

  public void download(File writableRomPath, String listName, String name) {

    SoftwareListRomSetFile file = files.get(listName).get(name);
    Collection col = getCollection(file.getCollectionId());
    StringBuilder builder;

    builder = new StringBuilder("Found ");
    builder.append(file.getName());
    IaMame.info(builder.toString());

    builder = new StringBuilder("Collection: ");
    builder.append(col.getName());
    IaMame.info(builder.toString());

    builder = new StringBuilder("URL: ");
    builder.append(col.getUrl());

    IaMame.info(builder.toString());

    // Determine destination path

    builder = new StringBuilder(writableRomPath.getAbsolutePath());
    builder.append(File.separator);
    builder.append(file.getName());
    String destPath = builder.toString();

    downloadFile(file.getUrl(), destPath, file.getSize());

  }

  /**
   * Empty constructor needed by JAXB
   */
  public SoftwareListRomSet() {
    super(null, null);
    this.files = null;
  }

}
