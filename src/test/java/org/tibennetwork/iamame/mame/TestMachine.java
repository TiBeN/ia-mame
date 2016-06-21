package org.tibennetwork.iamame.mame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestMachine {

    @Test
    public void testGetNeededRomsFilesWithMachineNeedingOnlyOneFile ()
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/altbeast.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("altbeast");

        List<String> neededFiles = m.getNeededRomFiles();

        // Replace return argument type of methods 
        // get{Needed|Missing}{Rom|Chd}Files from List to Set

    }

    @Test
    public void testGetNeededRomsFilesWithMachineNeedingManyFiles () {
    
    }

    @Test 
    public void testGetMissingRomsFilesWithEmptyRomPath () {
    
    }

    @Test
    public void testGetMissingRomsFilesWithMissingPartsRomPath () {
    
    }

    @Test 
    public void testGetMissingRomsFilesWithFullRomPath () {
    
    }

    // Tests with chd
}
