package org.tibennetwork.iamame.internetarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.tibennetwork.iamame.internetarchive.SoftwareListChdSetFileXmlAdapter.SoftwareListChdSetFiles;

public class SoftwareListChdSetFileXmlAdapter extends
    XmlAdapter<SoftwareListChdSetFiles, Map<String, Map<String, Map<String, SoftwareListChdSetFile>>>> {

  @XmlRootElement
  public static class SoftwareListChdSetFiles {

    @XmlElement(name = "file")
    public List<SoftwareListChdSetFile> files = new ArrayList<>();

  }

  public SoftwareListChdSetFiles marshal(
      Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> files)
      throws Exception {

    SoftwareListChdSetFiles container = new SoftwareListChdSetFiles();

    for (Map.Entry<String, Map<String, Map<String, SoftwareListChdSetFile>>> entry : files
        .entrySet()) {

      for (Map.Entry<String, Map<String, SoftwareListChdSetFile>> subEntry : entry
          .getValue().entrySet()) {

        for (SoftwareListChdSetFile subSubEntry : subEntry.getValue()
            .values()) {
          container.files.add(subSubEntry);
        }

      }
    }

    return container;
  }

  public Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> unmarshal(
      SoftwareListChdSetFiles container) throws Exception {
    Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> files =
        new HashMap<>();
    for (SoftwareListChdSetFile file : container.files) {

      String softwareList = file.getSoftwareList();
      String software = file.getSoftware();

      if (!files.containsKey(softwareList)) {
        files.put(softwareList,
            new HashMap<String, Map<String, SoftwareListChdSetFile>>());
      }

      if (!files.get(softwareList).containsKey(software)) {
        files.get(softwareList).put(software,
            new HashMap<String, SoftwareListChdSetFile>());
      }

      files.get(softwareList).get(software).put(file.getChd(), file);
    }
    return files;
  }

}
