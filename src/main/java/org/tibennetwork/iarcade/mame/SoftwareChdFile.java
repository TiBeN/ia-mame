package org.tibennetwork.iarcade.mame;

import java.io.File;

public class SoftwareChdFile {

  private String listName;
  private String softwareName;
  private String name;

  private String relativePath;

  public SoftwareChdFile(String listName, String softwareName, String name) {

    this.listName = listName;
    this.softwareName = softwareName;
    this.name = name;

    this.relativePath = listName + File.separator + softwareName
        + File.separator + name + ".chd";

  }

  public String getListName() {
    return listName;
  }

  public String getSoftwareName() {
    return softwareName;
  }

  public String getName() {
    return name;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public String toString() {
    return String.format(
        "SoftwareChdFile[listName: %s, softwareName: %s, name: %s]", listName,
        softwareName, name);
  }

  public boolean equals(Object obj) {
    if (obj instanceof SoftwareChdFile) {

      SoftwareChdFile sfToCompare = (SoftwareChdFile) obj;

      return listName.equals(sfToCompare.getListName())
          && softwareName.equals(sfToCompare.getSoftwareName())
          && name.equals(sfToCompare.getName());
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
