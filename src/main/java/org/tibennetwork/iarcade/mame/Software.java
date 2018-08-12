package org.tibennetwork.iarcade.mame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Software used with a Machine
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Software {

  @XmlRootElement
  static class Part {

    static class DiskArea {

      static class Disk {

        @XmlAttribute(name = "name")
        private String name;

        public String getName() {
          return this.name;
        }

      }

      @XmlElement(name = "disk")
      private Disk disk;

      public Disk getDisk() {
        return this.disk;
      }

    }

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "interface")
    private String deviceInterface;

    @XmlElement
    private DiskArea diskarea;

    public String getName() {
      return name;
    }

    public String getDeviceInterface() {
      return deviceInterface;
    }

    public DiskArea getDiskarea() {
      return diskarea;
    }

  }

  @XmlAttribute(name = "name")
  private String name;

  @XmlElement(name = "description")
  private String description;

  @XmlElement(name = "year")
  private String year;

  @XmlElement(name = "publisher")
  private String publisher;

  @XmlElement(name = "part")
  private List<Part> parts = new ArrayList<>();

  @XmlAttribute(name = "cloneof")
  private String cloneof;

  private Machine machine;

  private MediaDevice mediaDevice;

  private SoftwareList softwareList;

  private String regularFileName;

  /**
   * The software this software is a clone of
   */
  private Software original;

  public Software() {}

  /**
   * constructor for non softwarelist files.
   */
  public Software(Machine m, MediaDevice md, String name) {
    this.machine = m;
    this.mediaDevice = md;
    this.regularFileName = name;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getYear() {
    return year;
  }

  public String getPublisher() {
    return publisher;
  }

  public List<Part> getParts() {
    return parts;
  }

  public String getCloneof() {
    return cloneof;
  }

  public void setCloneof(String cloneof) {
    this.cloneof = cloneof;
  }

  public Machine getMachine() {
    return machine;
  }

  public void setMachine(Machine machine) {
    this.machine = machine;
  }

  public MediaDevice getMediaDevice() {
    return mediaDevice;
  }

  public SoftwareList getSoftwareList() {
    return softwareList;
  }

  public void setSoftwareList(SoftwareList softwareList) {
    this.softwareList = softwareList;
  }

  public Software getOriginal() {
    return original;
  }

  public void setOriginal(Software original) {
    this.original = original;
  }

  public boolean isRegularFile() {
    return this.regularFileName != null;
  }

  public String getMediaInterface() {

    if (this.isRegularFile()) {
      return this.mediaDevice.getMediaInterface();
    }

    for (Part p : this.parts) {
      return p.getDeviceInterface();
    }

    return null;
  }

  public boolean isAClone() {
    return this.cloneof != null;
  }

  public String toString() {
    return this.isRegularFile()
        ? String.format("Software [device: %s, file: %s]",
            this.getMediaInterface(), this.regularFileName)
        : String.format(
            "Software: [device: %s, name: %s (%s), publisher: %s, "
                + "machine: %s]",
            this.getMediaInterface(), this.description, this.name,
            this.publisher, this.machine.getDescription());
  }

  /**
   * Generate and return a Set of needed CHD files to run this software
   */
  private Set<SoftwareChdFile> getNeededChdFiles() {

    Set<SoftwareChdFile> neededFiles = new HashSet<>();

    // Search for CD-ROM or HDD items on the software parts.
    // Theses media types are stored on chd files.

    for (Part p : this.parts) {
      if (p.getName().matches("^cdrom[0-9]*$")
          | p.getName().matches("^hdd[0-9]*$")) {
        neededFiles.add(new SoftwareChdFile(this.softwareList.getName(),
            this.name, p.getDiskarea().getDisk().getName()));
      }
    }

    return neededFiles;

  }

  /**
   * Determine missing CHD files on the given rom path
   */
  public Set<SoftwareChdFile> getMissingChdFiles(Set<File> romPaths) {

    Set<SoftwareChdFile> missingFiles = new HashSet<>();

    fileloop: for (SoftwareChdFile chd : this.getNeededChdFiles()) {

      for (File romPath : romPaths) {

        File chdFileInRomPath = new File(
            romPath.getAbsolutePath() + File.separator + chd.getRelativePath());
        if (chdFileInRomPath.exists()) {
          continue fileloop;
        }

      }

      missingFiles.add(chd);

    }

    return missingFiles;

  }

  /**
   * Generate and return needed rom file to run this software.
   *
   * If the software contains any chd files of cd-rom type, then it should not
   * contain any rom file
   */
  public Set<SoftwareRomFile> getNeededRomFiles() {

    // Search for CD-ROM items on the software parts.
    // If that is the case, return empty because
    // it seems a cd-rom software doesn't contain
    // any rom files.
    for (Part p : this.parts) {
      if (p.getName().matches("^cdrom[0-9]*$")) {
        return new HashSet<>();
      }
    }

    Set<SoftwareRomFile> neededRomFiles = new HashSet<>();

    neededRomFiles.add(new SoftwareRomFile(softwareList.getName(), name));
    if (isAClone()) {
      neededRomFiles.addAll(original.getNeededRomFiles());
    }

    return neededRomFiles;

  }

  /**
   * Determine missing rom file on the given rom path
   */
  public Set<SoftwareRomFile> getMissingRomFiles(Set<File> romPaths) {

    Set<SoftwareRomFile> missingRomFiles = new HashSet<>();

    softwareFilesLoop: for (SoftwareRomFile file : this.getNeededRomFiles()) {

      for (File romPath : romPaths) {

        File zippedFileInRomPath = new File(romPath.getAbsolutePath()
            + File.separator + file.getZipRelativePath());

        File sevenZippedFileInRomPath = new File(romPath.getAbsolutePath()
            + File.separator + file.getSevenZipRelativePath());

        if (zippedFileInRomPath.exists() || sevenZippedFileInRomPath.exists()) {
          continue softwareFilesLoop;
        }

      }

      // Here we tried all rompaths and not found any file

      missingRomFiles.add(file);

    }

    return missingRomFiles;

  }

  /**
   * Determines if some files are missing on the given set of
   *
   * romspaths to launch this software
   */
  public boolean areFilesAvailable(Set<File> romPaths) {
    return this.getMissingRomFiles(romPaths) == null
        && this.getMissingChdFiles(romPaths) == null;
  }

}
