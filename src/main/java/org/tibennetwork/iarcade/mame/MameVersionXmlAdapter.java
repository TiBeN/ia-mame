package org.tibennetwork.iarcade.mame;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB XmlAdapter that transform a MameVersion object to String and vice-versa
 */
public class MameVersionXmlAdapter extends XmlAdapter<String, MameVersion> {

  public String marshal(MameVersion version) {
    return version.toString();
  }

  public MameVersion unmarshal(String version) {
    return new MameVersion(version);
  }

}
