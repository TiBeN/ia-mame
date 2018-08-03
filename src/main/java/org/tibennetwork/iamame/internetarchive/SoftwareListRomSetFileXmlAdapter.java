package org.tibennetwork.iamame.internetarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.tibennetwork.iamame.internetarchive.SoftwareListRomSetFileXmlAdapter.SoftwareListRomSetFiles;

public class SoftwareListRomSetFileXmlAdapter extends
    XmlAdapter<SoftwareListRomSetFiles, Map<String, Map<String, SoftwareListRomSetFile>>> {

  @XmlRootElement
  public static class SoftwareListRomSetFiles {

    @XmlElement(name = "file")
    public List<SoftwareListRomSetFile> files = new ArrayList<>();

  }

  public SoftwareListRomSetFiles marshal(
      Map<String, Map<String, SoftwareListRomSetFile>> files) throws Exception {

    SoftwareListRomSetFiles container = new SoftwareListRomSetFiles();

    for (Map.Entry<String, Map<String, SoftwareListRomSetFile>> entry : files
        .entrySet()) {

      for (Map.Entry<String, SoftwareListRomSetFile> subEntry : entry.getValue()
          .entrySet()) {
        container.files.add(subEntry.getValue());
      }
    }

    return container;
  }

  public Map<String, Map<String, SoftwareListRomSetFile>> unmarshal(
      SoftwareListRomSetFiles container) throws Exception {
    Map<String, Map<String, SoftwareListRomSetFile>> files = new HashMap<>();
    for (SoftwareListRomSetFile file : container.files) {

      if (!files.containsKey(file.getMachineName())) {
        files.put(file.getMachineName(),
            new HashMap<String, SoftwareListRomSetFile>());
      }

      files.get(file.getMachineName()).put(file.getSoftwareName(), file);
    }
    return files;
  }

}
