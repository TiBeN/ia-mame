package org.tibennetwork.iamame.internetarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.tibennetwork.iamame.internetarchive.MachineChdSetFileXmlAdapter.MachineChdSetFiles;

public class MachineChdSetFileXmlAdapter extends
    XmlAdapter<MachineChdSetFiles, Map<String, Map<String, MachineChdSetFile>>> {

  @XmlRootElement
  public static class MachineChdSetFiles {

    @XmlElement(name = "file")
    public List<MachineChdSetFile> files = new ArrayList<>();

  }

  public MachineChdSetFiles marshal(
      Map<String, Map<String, MachineChdSetFile>> files) throws Exception {

    MachineChdSetFiles container = new MachineChdSetFiles();

    for (Map.Entry<String, Map<String, MachineChdSetFile>> entry : files
        .entrySet()) {
      for (Map.Entry<String, MachineChdSetFile> chdEntry : entry.getValue()
          .entrySet()) {
        container.files.add(chdEntry.getValue());
      }
    }

    return container;
  }

  public Map<String, Map<String, MachineChdSetFile>> unmarshal(
      MachineChdSetFiles container) throws Exception {
    Map<String, Map<String, MachineChdSetFile>> files = new HashMap<>();
    for (MachineChdSetFile file : container.files) {

      String machineName = file.getMachine();

      if (!files.containsKey(machineName)) {
        files.put(machineName, new HashMap<String, MachineChdSetFile>());
      }

      files.get(machineName).put(file.getChd(), file);

    }
    return files;
  }

}
