package org.tibennetwork.iarcade.mame;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Mame SoftwareList
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SoftwareList {

    @XmlAttribute(name="name")
    private String name;

    @XmlAttribute(name="description")
    private String description;

    @XmlElement(name="software")
    public List<Software> softwares;

    public String toString() {
        return String.format("SoftwareList: [name: %s]", name);
    }

    public String getName() {
        return name;
    }

    public List<Software> getSoftwares() {
        return softwares;
    }

}
