package org.tibennetwork.iarcade.internetarchive;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.tibennetwork.iarcade.IaRcade;
import org.tibennetwork.iarcade.mame.MameVersion;

/**
 * A RomSet containing roms for emulated machines (arcade, console and device
 * bios etc.)
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MachineRomSet extends RomSet {

  static {
    MachineRomSet.versions = new TreeSet<>();
    MachineRomSet.versions.add(new MameVersion("0.149"));
    MachineRomSet.versions.add(new MameVersion("0.151"));
    MachineRomSet.versions.add(new MameVersion("0.161"));
    MachineRomSet.versions.add(new MameVersion("0.185"));
    MachineRomSet.versions.add(new MameVersion("0.193"));
    MachineRomSet.versions.add(new MameVersion("0.197"));
  }

  public static enum MachineRomSetFormat {
    SPLIT, MERGED
  }

  public static TreeSet<MameVersion> versions;

  @XmlElement(name = "format")
  private final MachineRomSetFormat format;

  @XmlJavaTypeAdapter(MachineRomSetFileXmlAdapter.class)
  private final Map<String, MachineRomSetFile> files;

  public MachineRomSet(MameVersion version, Set<Collection> collections,
      MachineRomSetFormat format, Map<String, MachineRomSetFile> files) {
    super(version, collections);
    this.format = format;
    this.files = files;
  }

  /**
   * Instanciate a MachineRomSet for the given MameVersion
   *
   * The class is unmarshalled from XML data in 'machine-rom-set' directory on
   * the classpath.
   */
  public static MachineRomSet createFromVersion(MameVersion version) {

    StringBuilder builder = new StringBuilder("machine-rom-set/");
    builder.append(version);
    builder.append(".xml");

    InputStream stream = MachineRomSet.class.getClassLoader()
        .getResourceAsStream(builder.toString());

    return JAXB.unmarshal(stream, MachineRomSet.class);

  }

  /**
   * Return an iterator of rom sets sort by version desc starting from equal or
   * closest matching given Mame version
   */
  public static Iterator<MachineRomSet> getRomSets(MameVersion version) {

    // No archive.org ROM set collection matches your Mame version (
    if (!MachineRomSet.versions.contains(version)) {
      IaRcade.warn("No archive.org machine ROM set matches your Mame version");
      IaRcade.warn("Some files might not be available.");
    }

    final Iterator<MameVersion> iter =
        MachineRomSet.versions.headSet(version, true).descendingIterator();

    return new Iterator<MachineRomSet>() {

      public boolean hasNext() {
        return iter.hasNext();
      }

      public MachineRomSet next() {
        return MachineRomSet.createFromVersion(iter.next());
      }
    };

  }

  public MachineRomSetFormat getFormat() {
    return format;
  }

  public Map<String, MachineRomSetFile> getFiles() {
    return files;
  }

  /**
   * Determine if this rom set contains machine roms
   */
  public boolean contains(String machineName) {
    return files.containsKey(machineName);
  }

  public void download(File writableRomPath, String machineName) {

    MachineRomSetFile file = files.get(machineName);

    Collection col = getCollection(file.getCollectionId());
    StringBuilder builder;

    builder = new StringBuilder("Found ");
    builder.append(file.getName());
    IaRcade.info(builder.toString());

    builder = new StringBuilder("Collection: ");
    builder.append(col.getName());
    IaRcade.info(builder.toString());

    builder = new StringBuilder("URL: ");
    builder.append(col.getUrl());

    IaRcade.info(builder.toString());

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
  public MachineRomSet() {
    super(null, null);
    this.format = null;
    this.files = null;
  }

}
