package org.tibennetwork.iamame.internetarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.tibennetwork.iamame.internetarchive.MachineRomSetFileXmlAdapter.MachineRomSetFiles;

/**
 * JAXB XmlAdapter that transform a XML list of MachineSetRomFiles into a
 * Map<String, MachineRomSetFile>
 */
public class MachineRomSetFileXmlAdapter
    extends XmlAdapter<MachineRomSetFiles, Map<String, MachineRomSetFile>> {

  @XmlRootElement
  public static class MachineRomSetFiles {

    @XmlElement(name = "file")
    public List<MachineRomSetFile> files = new ArrayList<>();

  }

  public MachineRomSetFiles marshal(Map<String, MachineRomSetFile> files)
      throws Exception {

    MachineRomSetFiles container = new MachineRomSetFiles();

    for (Map.Entry<String, MachineRomSetFile> entry : files.entrySet()) {
      container.files.add(entry.getValue());
    }

    return container;
  }

  public Map<String, MachineRomSetFile> unmarshal(MachineRomSetFiles container)
      throws Exception {
    Map<String, MachineRomSetFile> files = new HashMap<>();
    for (MachineRomSetFile file : container.files) {
      files.put(file.getMachineName(), file);
    }
    return files;
  }
}
