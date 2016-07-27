package org.tibennetwork.iamame.mame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public boolean isAvailable (Set<File> romsPaths) {
    
        String rfp = this.getRelativeFilePath();

        for (File rp: romsPaths) {
            File softwareFile = new File(rp + File.separator + rfp);
            if(softwareFile.exists()) {
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
     * Return the partial file path relative to the rompath.
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

    /**
     * Generate and return a Set of needed files to
     * run this software
     */
    private Set<SoftwareFile> getNeededChdFiles () {

        Set<SoftwareFile> neededFiles = new HashSet<>();

        // Search for CD-ROM items on the software parts.
        // Theses media types are stored on chd files.
        for (Part p: this.parts) {
            if (p.getName().matches("^cdrom[0-9]+$")) {
                String chdFileName = this.softwareList.getName()
                    + File.separator
                    + this.name
                    + File.separator
                    + p.getDiskarea().getDisk().getName();
                neededFiles.add(new SoftwareFile(chdFileName, true));
            }
            
        }

        // Declare the software media(s) file in zip format.
        // For a Software, chd or zip files seems to be exclusive 
        // (ie: if there is CHD files on the parts of the software, 
        // there is no zip files associated.)
        //if (!isChdFormat) {
            //String romFileName = this.softwareList.getName()
                //+ File.separator
                //+ this.name;
            //neededFiles.add(new SoftwareFile(romFileName, false));
        //}

        return neededFiles;

    }

    /**
     * Determine missing CHD files on the given rom path
     */
    public Set<SoftwareFile> getMissingChdFiles (Set<File> romPaths) {

        Set<SoftwareFile> missingFiles = new HashSet<>();

        fileloop: for (SoftwareFile sf: this.getNeededChdFiles()) {
            
            for (File romPath: romPaths) {

                //if (sf.isChdFile()) {
                    File chdFileInRomPath = new File(romPath.getAbsolutePath()
                        + File.separator
                        + sf.getRelativeFilePath());
                    if (chdFileInRomPath.exists()) {
                        continue fileloop;
                    }

                //} else {
                    //File zippedFileInRomPath 
                        //= new File(romPath.getAbsolutePath()
                            //+ File.separator
                            //+ sf.getRelativeFilePathWithoutExtension()
                            //+ ".zip");                                                         

                    //File sevenZippedFileInRomPath 
                        //= new File(romPath.getAbsolutePath()
                            //+ File.separator
                            //+ sf.getRelativeFilePathWithoutExtension()
                            //+ ".7z");
                    
                    //if (zippedFileInRomPath.exists() 
                            //|| sevenZippedFileInRomPath.exists()) {
                        //continue fileloop;                    
                    //}
                //}

            }

            missingFiles.add(sf);

        }

        return missingFiles;

    }
}
