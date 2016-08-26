package org.tibennetwork.iamame.mame;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mame Software Repository
 * (softwarelist abstraction layer)
 */
public class SoftwareRepository {

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    static class SoftwareListsXmlContainer {

        @XmlElement(name="softwarelist")
        private List<SoftwareList> softwareLists;

        public List<SoftwareList> getSoftwareLists() {
            return softwareLists;
        }
                
    }

    private MameRuntime mame;

    public SoftwareRepository (MameRuntime mame) {
        this.mame = mame;
    }

    /**
     * Find a Software by machine and software name from the software list
     */
    public Software findByMachineAndByName (
            Machine machine, 
            String softwareName) 
            throws IOException, 
               InterruptedException,
               MachineDoesntExistException,
               MachineHasNoSoftwareListException,
               SoftwareNotFoundInSoftwareListsException {
    
        SoftwareListsXmlContainer slxc = this.getSoftwareLists(machine);

        for (SoftwareList sl: slxc.getSoftwareLists()) {
            for (Software s: sl.getSoftwares()) {
                if(s.getName().equals(softwareName)) {
                    s.setMachine(machine);
                    s.setSoftwareList(sl);
                    return s;                    
                }
            }
        }

        throw new SoftwareNotFoundInSoftwareListsException(
            String.format(
                "The software '%s' has not been found on '%s' " 
                    +" software lists",
                softwareName,
                machine.getDescription()));

    }
    
    /**
     * Find a Software by machine, mediatype and software name
     * from the software list
     */
    public Software findByMachineAndAndByMediaTypeAndByName (
        Machine machine,
        MediaDevice mediaDevice,
        String softwareName)
            throws IOException, 
               InterruptedException,
               MachineDoesntExistException,
               MachineHasNoSoftwareListException,
               SoftwareNotFoundInSoftwareListsException {
        
        SoftwareListsXmlContainer slxc = this.getSoftwareLists(machine);

        for (SoftwareList sl: slxc.getSoftwareLists()) {
            for (Software s: sl.getSoftwares()) {
                if(s.getName().equals(softwareName)
                    && s.getMediaInterface()
                            .equals(mediaDevice.getMediaInterface())) {
                    s.setMachine(machine);
                    s.setSoftwareList(sl);
                    //if (s.isAClone()) {
                        //s.setOriginal(
                            //this.findByMachineAndAndByMediaTypeAndByName(
                                //machine,
                                //mediaDevice,
                                //s.getOriginalName()));
                    //}

                     
                    return s;                   

                }
            }
        }

        throw new SoftwareNotFoundInSoftwareListsException(
            String.format(
                "The software '%s' has not been found on '%s' " 
                    +" software lists",
                softwareName,
                machine.getDescription()));
        
    }

    private SoftwareListsXmlContainer getSoftwareLists (Machine m) 
            throws MachineDoesntExistException,
                MachineHasNoSoftwareListException,
                IOException,
                InterruptedException {
    
        // Call MameRuntime to get Xml data of the given system,
        // then unmarshall it
        String[] mameCommandLine = {"-listsoftware", m.getName()};
        InputStream is = null;

        try {
            is = this.mame.executeAndReturnStdoutAsInputStream(
                mameCommandLine);
        } catch (MameExecutionException e) {
            throw new MachineDoesntExistException(
                String.format("The machine '%s' doesn't exist or is not " 
                        + "supported by the provided Mame version",
                    m.getName()));
        }
        
        SoftwareListsXmlContainer slxc 
            = JAXB.unmarshal(is, SoftwareListsXmlContainer.class);
        if(slxc.getSoftwareLists().isEmpty()) {
            throw new MachineHasNoSoftwareListException(String.format(
                "The machine '%s' has no software lists",
                m.getDescription()));
        }

        return slxc;
        
    }



}
