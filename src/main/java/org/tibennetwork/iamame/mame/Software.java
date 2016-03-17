package org.tibennetwork.iamame.mame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Software used with a Machine
 */
@XmlRootElement
public class Software {

    @XmlRootElement
    static class Part {
        
        static class DiskArea {
            
            static class Disk {
                
                @XmlAttribute(name="name")
                private String name;

                public String getName () {
                    return this.name;
                }

            }

            @XmlElement(name="disk")
            private Disk disk;

            public Disk getDisk () {
                return this.disk;
            }

        }

        @XmlAttribute(name="name")
        private String name;

        @XmlAttribute(name="interface")
        private String deviceInterface;

        @XmlElement
        private DiskArea diskarea;

        public String getName() {
            return name;
        }

        public String getDeviceInterface() {
            return deviceInterface;
        }

        
        public DiskArea getDiskarea () {
            return diskarea;
        }

    }

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="description")
    private String description;

    @XmlElement(name="year")
    private String year;

    @XmlElement(name="publisher")
    private String publisher;

    @XmlElement(name="part")
    private List<Part> parts = new ArrayList<>();

    private Machine machine;

    private MediaDevice mediaDevice;

    private SoftwareList softwareList;

    private String regularFileName;

    public Software () {}

    /**
     * constructor for non softwarelist files.
     */
    public Software (Machine m, MediaDevice md, String name) {
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

    public boolean isRegularFile () {
        return this.regularFileName != null;
    }

    public String getMediaInterface () {
        
        if (this.isRegularFile()) {
            return this.mediaDevice.getMediaInterface();
        }

        for (Part p : this.parts) {
            return p.getDeviceInterface();
        }
        
        return null;
    }

    /**
     * Determines whether the files of this software 
     * are available on the given romPath
     */
    public boolean isAvailable (List<File> romsPaths) {
    
        String rfp = this.getRelativeFilePath();

        for (File rp: romsPaths) {
            File softwareFile = new File(rp + File.separator + rfp);
            System.out.println(softwareFile);
            if(softwareFile.exists()) {
                System.out.println(softwareFile);
                return true;
            }
        }

        return false;

    }
    
    public String toString() {
        return this.isRegularFile() 
            ? String.format("Software [device: %s, file: %s]", 
                this.getMediaInterface(),
                this.regularFileName)
            : String.format(
                "Software: [device: %s, name: %s (%s), publisher: %s, " 
                    + "machine: %s]", 
                this.getMediaInterface(),    
                this.description, 
                this.name, 
                this.publisher, 
                this.machine.getDescription());
    }

    /**
     * Return the partial file path relative to the
     * rompath.
     */
    public String getRelativeFilePath() {

        // Its a CDROM ? so its a CHD
        if (this.parts.get(0).getName().equals("cdrom")) {
            return this.softwareList.getName() 
                + File.separator
                + this.name
                + File.separator
                + this.getChdName()
                + ".chd";
        } else {
            return this.softwareList.getName() 
                + File.separator
                + this.name
                + ".zip";
        }

    }

    public String getChdName () {
        return this.parts.get(0).getDiskarea().getDisk().getName();
    }

}
