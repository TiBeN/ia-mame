package org.tibennetwork.iamame.mame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Factory for MameSystems
 */
public class MachineRepository {

    /**
     * Mame XML root element container.
     */
    @XmlRootElement(name = "mame")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MameXmlContainer {

        @XmlElement(name="machine")
        private List<Machine> machines = new ArrayList<Machine>();

        public List<Machine> getMachines() {
            return machines;
        }

        public void setMachines(List<Machine> machines) {
            this.machines = machines;
        }

    }

    private MameRuntime mame; 

    public MachineRepository (MameRuntime mame) {
        this.mame = mame;
    }

    /**
     * Return a MameSystem object corresponding
     * to the given system name
     */
    public Machine findByName (String machineName) 
            throws IOException, 
                InterruptedException, 
                MachineDoesntExistException {

        // Call MameRuntime to get Xml data of the given system,
        // then unmarshall it
        String[] mameCommandLine = {"-listxml", machineName};
        InputStream is;

        MameXmlContainer ms = null;

        try {
            is = this.mame.executeAndReturnStdoutAsInputStream(
                    mameCommandLine);
            ms = JAXB.unmarshal(is, MameXmlContainer.class);
        } catch (MameExecutionException | DataBindingException e) {
            throw new MachineDoesntExistException(
                String.format("The machine '%s' doesn't exist or is not " 
                        + "supported by the provided Mame version",
                    machineName));
        }

        Machine machine = null;
        Set<Machine> subMachines = new HashSet<>();

        for (Machine m: ms.getMachines()) {

            if (m.getName().equals(machineName)) {

                machine = m;

                String parentMachineName = m.getRomof();
                if(parentMachineName != null) {
                    m.setParentMachine(
                            this.findByName(parentMachineName));
                }

            } else {
                // Others machines of the set are considered "subMachines"
                subMachines.add(m);
            }

        }
            
        if (machine == null) {
            throw new RuntimeException(String.format(
                "Unhandled case: Mame returned no errors while searching " 
                    + "for machine %s but the machine has not been found " 
                    + "on the XML content", 
                machineName));
        }

        machine.setSubMachines(subMachines);

        return machine;
    }

}
