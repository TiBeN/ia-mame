package org.tibennetwork.iamame.mame;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestMachineRepository {

    /**
     * For Mame version up to 0.160
     */
    @Test
    public void testFindByNameWithDriverXmlHavingRootTagNamedGame ()
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        mame.setVersion(new MameVersion("0.149"));
        
        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/punisher0149.xml"));

        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);
        Machine m = mr.findByName("punisher");
        
        // Testing two or three properties should suffice
        assertThat(m.getName(), equalTo("punisher"));
        assertThat(m.getDescription(), equalTo("The Punisher (World 930422)"));
    }

    /**
     * For Mame starting from version 0.161 (0.162 ?)
     */
    @Test
    public void testFindByNameWithDriverXmlHavingRootTagNamedMachine ()
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {

        FakeMameRuntime mame = new FakeMameRuntime();
        mame.setVersion(new MameVersion("0.170"));

        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/punisher.xml"));

        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);
        Machine m = mr.findByName("punisher");
        
        // Testing two or three properties should suffice
        assertThat(m.getName(), equalTo("punisher"));
        assertThat(m.getDescription(), equalTo("The Punisher (World 930422)"));

    }

    @Test
    public void testFindByNameWithNameHavingUppercaseLetters ()
            throws FileNotFoundException,
                IOException,
                InterruptedException,
                MachineDoesntExistException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        mame.setVersion(new MameVersion("0.149"));
        
        List<InputStream> inputStreams = new ArrayList<>();

        inputStreams.add(
                new FileInputStream("src/test/resources/xml/punisher0149.xml"));

        mame.setInputStreamsToReturn(inputStreams);

        MachineRepository mr = new MachineRepository(mame);
        Machine m = mr.findByName("Punisher");
        
        // Testing two or three properties should suffice
        assertThat(m.getName(), equalTo("punisher"));
        assertThat(m.getDescription(), equalTo("The Punisher (World 930422)"));
    }
}
