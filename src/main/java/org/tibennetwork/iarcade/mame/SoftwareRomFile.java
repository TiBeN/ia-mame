package org.tibennetwork.iarcade.mame;

import java.io.File;

/**
 * Model a rom file needed by a Software from softwarelist rom file needed by a
 * system.
 */
public class SoftwareRomFile {

  private String listName;

  private String name;

  private String zipRelativePath;
  private String sevenZipRelativePath;

  public SoftwareRomFile(String listName, String name) {
    this.listName = listName;
    this.name = name;
    String relativePath = listName + File.separator + name;
    this.zipRelativePath = relativePath + ".zip";
    this.sevenZipRelativePath = relativePath + ".7z";
  }

  public String getName() {
    return name;
  }

  public String getListName() {
    return listName;
  }

  public String getZipRelativePath() {
    return zipRelativePath;
  }

  public String getSevenZipRelativePath() {
    return sevenZipRelativePath;
  }

  public String toString() {
    return String.format("SoftwareRomFile[listName: %s, name: %s]", listName,
        name);
  }

  public boolean equals(Object obj) {
    if (obj instanceof SoftwareRomFile) {

      SoftwareRomFile sfToCompare = (SoftwareRomFile) obj;

      return name.equals(sfToCompare.getName())
          && listName.equals(sfToCompare.getListName());
    } else {
      return false;
    }
  }

  public int hashCode() {
    int hash = 4;
    for (int i = 0; i < name.length(); i++) {
      hash = hash * 31 + name.charAt(i);
    }
    return hash;
  }

}
