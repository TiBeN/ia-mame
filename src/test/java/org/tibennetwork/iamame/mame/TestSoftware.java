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

public class TestSoftware {

    @Test
    public void testGetMissingChdFilesWithEmptyRomPath () 
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException,
                MachineHasNoSoftwareListException,
                SoftwareNotFoundInSoftwareListsException {

        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        // Add SegaCD Machine XML metadata
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd.xml"));

        // Add SMS XML softwarelist
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd-sl.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("segacd");

        SoftwareRepository sr = new SoftwareRepository(mame);

        Software s = sr.findByMachineAndByName(m, "slamcity");

        Set<File> romPaths = new HashSet<>();
        romPaths.add(new File("src/test/resources/empty-rompath"));

        Set<SoftwareFile> missingChdFiles = s.getMissingChdFiles(romPaths);

        Set<SoftwareFile> expectedMissingChdFiles = new HashSet<>();
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 1 of 4)[fingers][gw 02711_1 re-1]", true));
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 2 of 4)[juice][gw 02711_2 re-1]", true));
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 3 of 4)[mad dog][gw 02711_3 re-1]", true));
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 4 of 4)[smash][gw 02711_4 re-1]", true));

        assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));
            
    }

    @Test
    public void testGetMissingChdFilesWithMissingPartsRomPath () 
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException,
                MachineHasNoSoftwareListException,
                SoftwareNotFoundInSoftwareListsException {

        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        // Add SegaCD Machine XML metadata
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd.xml"));

        // Add SMS XML softwarelist
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd-sl.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("segacd");

        SoftwareRepository sr = new SoftwareRepository(mame);

        Software s = sr.findByMachineAndByName(m, "slamcity");

        Set<File> romPaths = new HashSet<>();
        romPaths.add(new File("src/test/resources/missing-parts-rompath"));

        Set<SoftwareFile> missingChdFiles = s.getMissingChdFiles(romPaths);

        Set<SoftwareFile> expectedMissingChdFiles = new HashSet<>();
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 1 of 4)[fingers][gw 02711_1 re-1]", true));
        expectedMissingChdFiles.add(new SoftwareFile("segacd/slamcity/slam city with scottie pippen (1994)(ntsc)(us)(disc 3 of 4)[mad dog][gw 02711_3 re-1]", true));

        assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));
            
    }

    @Test
    public void testGetMissingFilesWithFullRomPath () 
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException,
                MachineHasNoSoftwareListException,
                SoftwareNotFoundInSoftwareListsException {

        FakeMameRuntime mame = new FakeMameRuntime();
        
        List<InputStream> inputStreams = new ArrayList<>();

        // Add SegaCD Machine XML metadata
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd.xml"));

        // Add SMS XML softwarelist
        inputStreams.add(
                new FileInputStream("src/test/resources/xml/segacd-sl.xml"));
        
        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);

        Machine m = mr.findByName("segacd");

        SoftwareRepository sr = new SoftwareRepository(mame);

        Software s = sr.findByMachineAndByName(m, "slamcity");

        Set<File> romPaths = new HashSet<>();
        romPaths.add(new File("src/test/resources/full-rompath"));

        Set<SoftwareFile> missingChdFiles = s.getMissingChdFiles(romPaths);

        Set<SoftwareFile> expectedMissingChdFiles = new HashSet<>();

        assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));
            
    }

    //@Test
    //public void testGetMissingRomsFilesWithZipFilesRomPath ()
            //throws FileNotFoundException,
                //IOException,
                //InterruptedException,
                //MachineDoesntExistException,
                //MachineHasNoSoftwareListException,
                //SoftwareNotFoundInSoftwareListsException {

        //FakeMameRuntime mame = new FakeMameRuntime();
        
        //List<InputStream> inputStreams = new ArrayList<>();

        //// Add SMS Machine XML metadata
        //inputStreams.add(
                //new FileInputStream("src/test/resources/xml/sms.xml"));

        //// Add SMS XML softwarelist
        //inputStreams.add(
                //new FileInputStream("src/test/resources/xml/sms-sl.xml"));
        
        //mame.setInputStreamsToReturn(inputStreams);

        //MachineRepository mr = new MachineRepository(mame);

        //Machine m = mr.findByName("sms");

        //SoftwareRepository sr = new SoftwareRepository(mame);

        //Software s = sr.findByMachineAndByName(m, "columns");

        //Set<File> romPaths = new HashSet<>();
        //romPaths.add(new File("src/test/resources/full-rompath"));

        //Set<SoftwareFile> missingFiles = s.getMissingFiles(romPaths);

        //Set<SoftwareFile> expectedMissingFiles = new HashSet<>();

        //assertThat(missingFiles, equalTo(expectedMissingFiles));

    //}

    //@Test
    //public void testGetMissingRomsFilesWith7ZipFilesRomPath ()
            //throws FileNotFoundException,
                //IOException,
                //InterruptedException,
                //MachineDoesntExistException,
                //MachineHasNoSoftwareListException,
                //SoftwareNotFoundInSoftwareListsException {

        //FakeMameRuntime mame = new FakeMameRuntime();
        
        //List<InputStream> inputStreams = new ArrayList<>();

        //// Add SMS Machine XML metadata
        //inputStreams.add(
                //new FileInputStream("src/test/resources/xml/sms.xml"));

        //// Add SMS XML softwarelist
        //inputStreams.add(
                //new FileInputStream("src/test/resources/xml/sms-sl.xml"));
        
        //mame.setInputStreamsToReturn(inputStreams);

        //MachineRepository mr = new MachineRepository(mame);

        //Machine m = mr.findByName("sms");

        //SoftwareRepository sr = new SoftwareRepository(mame);

        //Software s = sr.findByMachineAndByName(m, "columns");

        //Set<File> romPaths = new HashSet<>();
        //romPaths.add(new File("src/test/resources/7z-rompath"));

        //Set<SoftwareFile> missingFiles = s.getMissingFiles(romPaths);

        //Set<SoftwareFile> expectedMissingFiles = new HashSet<>();

        //assertThat(missingFiles, equalTo(expectedMissingFiles));

    //}
}
