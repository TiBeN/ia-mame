package org.tibennetwork.iamame.internetarchive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An Internet Archive collection
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Collection {

  @XmlElement(name = "id")
  private final String id;

  @XmlElement(name = "name")
  private final String name;

  @XmlElement(name = "url")
  private final String url;

  public Collection(String id, String name, String url) {
    this.id = id;      
    this.name = name;
    this.url = url;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public String toString() {
    String format = "Collection: [id: %s, name: %s, url: %s]";
    return String.format(format, getId(), getName(), getUrl());
  }

  /**
   * Empty constructor needed by JAXB
   */
  public Collection() {
    this.id = null;
    this.name = null;
    this.url = null;    
  }

}
