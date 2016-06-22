package org.tibennetwork.iamame.mame;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<String> neededFiles = m.getNeededRomFiles();

        Set<String> expectedNeededFiles = new HashSet<>();
        expectedNeededFiles.add("altbeast");

        assertThat(neededFiles, equalTo(expectedNeededFiles));

    }

    @Test
    public void testGetNeededRomsFilesWithMachineNeedingManyFiles ()
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {

        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/neomrdo.xml"));
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/neogeo.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("neomrdo");

        Set<String> neededFiles = m.getNeededRomFiles();

        Set<String> expectedNeededFiles = new HashSet<>();
        expectedNeededFiles.add("neogeo");
        expectedNeededFiles.add("neomrdo");

        assertThat(neededFiles, equalTo(expectedNeededFiles));
    
    }

    @Test 
    public void testGetMissingRomsFilesWithEmptyRomPath () 
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {

        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/neomrdo.xml"));
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/neogeo.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("neomrdo");

        Set<File> romPaths = new HashSet<>();
        romPaths.add(new File("src/test/resources/empty-rompath"));

        Set<String> missingFiles = m.getMissingRomFiles(romPaths);

        Set<String> expectedMissingFiles = new HashSet<>();
        expectedMissingFiles.add("neogeo");
        expectedMissingFiles.add("neomrdo");

        assertThat(missingFiles, equalTo(expectedMissingFiles));
    
    }

    @Test
    public void testGetMissingRomsFilesWithMissingPartsRomPath () {
    
    }

    @Test 
    public void testGetMissingRomsFilesWithFullRomPath () {
    
    }

    // Tests with chd
}
