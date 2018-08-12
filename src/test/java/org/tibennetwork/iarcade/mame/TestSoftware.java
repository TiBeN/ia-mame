package org.tibennetwork.iarcade.mame;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestSoftware {

  @Test
  public void testGetMissingChdFilesWithEmptyRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SegaCD Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/segacd.xml"));

    // Add SMS XML softwarelist
    inputStreams
        .add(new FileInputStream("src/test/resources/xml/segacd-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("segacd");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "slamcity");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/empty-rompath"));

    Set<SoftwareChdFile> missingChdFiles = s.getMissingChdFiles(romPaths);

    Set<SoftwareChdFile> expectedMissingChdFiles = new HashSet<>();
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 1 of 4)[fingers][gw 02711_1 re-1]"));
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 2 of 4)[juice][gw 02711_2 re-1]"));
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 3 of 4)[mad dog][gw 02711_3 re-1]"));
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 4 of 4)[smash][gw 02711_4 re-1]"));

    assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));

  }

  @Test
  public void testGetMissingChdFileForSoftwareNeedingOnlyOneChdWithEmptyRomPath()
      throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SegaCD Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/segacd.xml"));

    // Add SMS XML softwarelist
    inputStreams
        .add(new FileInputStream("src/test/resources/xml/segacd-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("segacd");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "silpheed");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/empty-rompath"));

    Set<SoftwareChdFile> missingChdFiles = s.getMissingChdFiles(romPaths);

    Set<SoftwareChdFile> expectedMissingChdFiles = new HashSet<>();
    expectedMissingChdFiles
        .add(new SoftwareChdFile("segacd", "silpheed", "silpheed (usa)"));

    assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));

  }

  @Test
  public void testGetMissingChdFilesWithMissingPartsRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SegaCD Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/segacd.xml"));

    // Add SMS XML softwarelist
    inputStreams
        .add(new FileInputStream("src/test/resources/xml/segacd-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("segacd");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "slamcity");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/missing-parts-rompath"));

    Set<SoftwareChdFile> missingChdFiles = s.getMissingChdFiles(romPaths);

    Set<SoftwareChdFile> expectedMissingChdFiles = new HashSet<>();
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 1 of 4)[fingers][gw 02711_1 re-1]"));
    expectedMissingChdFiles.add(new SoftwareChdFile("segacd", "slamcity",
        "slam city with scottie pippen (1994)(ntsc)(us)(disc 3 of 4)[mad dog][gw 02711_3 re-1]"));

    assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));

  }

  @Test
  public void testGetMissingChdFilesWithFullRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SegaCD Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/segacd.xml"));

    // Add SMS XML softwarelist
    inputStreams
        .add(new FileInputStream("src/test/resources/xml/segacd-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("segacd");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "slamcity");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/full-rompath"));

    Set<SoftwareChdFile> missingChdFiles = s.getMissingChdFiles(romPaths);

    Set<SoftwareChdFile> expectedMissingChdFiles = new HashSet<>();

    assertThat(missingChdFiles, equalTo(expectedMissingChdFiles));

  }

  @Test
  public void testGetMissingRomFileWithEmptyRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SMS Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms.xml"));

    // Add SMS XML softwarelist
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("sms");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "columns");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/empty-rompath"));

    Set<SoftwareRomFile> missingRomFiles = s.getMissingRomFiles(romPaths);

    Set<SoftwareRomFile> expectedMissingRomFiles = new HashSet<>();
    expectedMissingRomFiles.add(new SoftwareRomFile("sms", "columns"));

    assertThat(missingRomFiles, equalTo(expectedMissingRomFiles));

  }

  @Test
  public void testGetMissingRomFileWithFullRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SMS Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms.xml"));

    // Add SMS XML softwarelist
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("sms");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "columns");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/full-rompath"));

    Set<SoftwareRomFile> expectedMissingRomFiles = new HashSet<>();

    assertThat(s.getMissingRomFiles(romPaths),
        equalTo(expectedMissingRomFiles));

  }

  @Test
  public void testGetMissingRomFileWith7zRomPath() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SMS Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms.xml"));

    // Add SMS XML softwarelist
    inputStreams.add(new FileInputStream("src/test/resources/xml/sms-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("sms");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "columns");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/7z-rompath"));

    Set<SoftwareRomFile> expectedMissingRomFiles = new HashSet<>();

    assertThat(s.getMissingRomFiles(romPaths),
        equalTo(expectedMissingRomFiles));
  }

  @Test
  public void testGetMissingRomFilesOnChdFilesSystemShouldReturnEmptySet()
      throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SegaCD Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/segacd.xml"));

    // Add SMS XML softwarelist
    inputStreams
        .add(new FileInputStream("src/test/resources/xml/segacd-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("segacd");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "slamcity");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/full-rompath"));

    Set<SoftwareRomFile> expectedMissingRomFiles = new HashSet<>();

    assertThat(s.getMissingRomFiles(romPaths),
        equalTo(expectedMissingRomFiles));

  }

  @Test
  public void testGetMissingRomFilesForSoftwareClone() throws Exception {

    FakeMameRuntime mame = new FakeMameRuntime();

    List<InputStream> inputStreams = new ArrayList<>();

    // Add SMS Machine XML metadata
    inputStreams.add(new FileInputStream("src/test/resources/xml/snes.xml"));

    // Add SMS XML softwarelist
    inputStreams.add(new FileInputStream("src/test/resources/xml/snes-sl.xml"));
    // will be called twice to find the original from the clone
    inputStreams.add(new FileInputStream("src/test/resources/xml/snes-sl.xml"));

    mame.setInputStreamsToReturn(inputStreams);

    MachineRepository mr = new MachineRepository(mame);

    Machine m = mr.findByName("snes");

    SoftwareRepository sr = new SoftwareRepository(mame);

    Software s = sr.findByMachineAndByName(m, "megamnx2u");

    Set<File> romPaths = new HashSet<>();
    romPaths.add(new File("src/test/resources/full-rompath"));

    Set<SoftwareRomFile> expectedMissingRomFiles = new HashSet<>();
    expectedMissingRomFiles.add(new SoftwareRomFile("snes", "megamnx2u"));
    expectedMissingRomFiles.add(new SoftwareRomFile("snes", "megamnx2"));

    assertThat(s.getMissingRomFiles(romPaths),
        equalTo(expectedMissingRomFiles));

  }
}
