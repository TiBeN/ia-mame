package org.tibennetwork.iamame.mame;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestMachine {

    @Test
    public void testGetNeededRomsFilesWithMachineNeedingOnlyOneFile () {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

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
