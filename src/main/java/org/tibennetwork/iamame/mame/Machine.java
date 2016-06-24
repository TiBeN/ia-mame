package org.tibennetwork.iamame.mame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Emulated System by Mame
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Machine {

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Rom {

        @XmlAttribute(name="name")
        private String name;

        public String getName() {
            return name;
        }
        
    }

    /**
     * This as been renamed to MachineDisk because of JAXB clash with
     * Software.DiskArea.Disk
     */
    @XmlRootElement(name="disk")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class MachineDisk {
        
        @XmlAttribute(name="name")
        String name;

        public String getName() {
            return name;
        }

    }

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="description")
    private String description;

    @XmlAttribute(name="runnable")
    private Boolean isRunnable;

    @XmlAttribute(name="romof")
    private String romof = null;

    @XmlElement(name="softwarelist")
    private List<SoftwareList> softwareLists;

    @XmlElement(name="rom")
    private List<Rom> roms = new ArrayList<>();

    @XmlElement(name="disk")
    private List<MachineDisk> disks = new ArrayList<>();

    @XmlElement(name="device")
    private List<MediaDevice> devices = new ArrayList<>();

    private Machine parentMachine;

    public Boolean IsRunnable() {
        return isRunnable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRomof() {
        return romof;
    }

    public List<SoftwareList> getSoftwareLists() {
        return softwareLists;
    }

    public Machine getParentMachine() {
        return parentMachine;
    }

    public void setParentMachine(Machine parentMachine) {
        this.parentMachine = parentMachine;
    }

    /**
     * Generate and return a list of supported medias
     * based on the machine/device data
     */
    public List<MediaDevice> getMediaDevices () {

        List<MediaDevice> mediaDevices = new ArrayList<>();

        for (MediaDevice d: this.devices) {
            if (d.getMediaInterface() != null) {
                mediaDevices.add(d);
            }
        }

        return mediaDevices;
    }

    /**
     * Generate and return a list of needed rom files
     */
    public Set<String> getNeededRomFiles () {
        Set<String> romSets = new HashSet<>();
        if (!this.roms.isEmpty()) {
            romSets.add(this.name);
        }
        if (this.parentMachine != null) {
            Set<String> psRomSets = this.parentMachine.getNeededRomFiles();
            if (!psRomSets.isEmpty()) {
                romSets.addAll(psRomSets);                                
            }
        }
        return romSets;
    }

    /**
     * Generate and return a list of needed chd files
     */
    public Set<String> getNeededChdFiles () {
        Set<String> chds = new HashSet<>();
        if (!this.disks.isEmpty()) {
            for (MachineDisk d: this.disks) {
                chds.add(d.getName());
            }
        }
        return chds;
    }

    /**
     * Determine missing roms files on the given rom path
     */
    public Set<String> getMissingRomFiles (Set<File> romPaths) {

        Set<String> missingRomFiles = new HashSet<>();

        romfileloop: for (String romFile: this.getNeededRomFiles()) {

            for (File romPath: romPaths) {
                File romFileInRomPath = new File(
                    romPath.getAbsolutePath() 
                        + File.separator 
                        + romFile
                        + ".zip");

                if (romFileInRomPath.exists()) {
                    continue romfileloop;
                }
            }

            missingRomFiles.add(romFile);

        }

        return missingRomFiles;
    
    }

    /**
     * Determines whether the required rom files for this system 
     * are present on the given rompaths
     */
    public boolean areRomFilesAvailable (Set<File> romPaths) {
        return this.getMissingRomFiles(romPaths).size() == 0;
    }

    /**
     * Return the SoftwareList matching the given MediaDevice
     */
    public SoftwareList getSoftwareList (MediaDevice md) 
            throws MachineHasNoSoftwareListException {

        String softwareListName = String.format("%s_%s",
            this.name,
            md.getBriefname());

        for (SoftwareList sl: this.softwareLists) {
            if (sl.getName() == softwareListName) {
                return sl;
            }
        }

        // Last chance, search for a list matching name

        throw new MachineHasNoSoftwareListException(String.format(
            "There is no %s softwarelist available for machine %s",
            md.getBriefname(),
            this.getDescription())); 
        
    }

    public String toString() {
        return String.format("Machine: [name: %s]", name);
    }

}
