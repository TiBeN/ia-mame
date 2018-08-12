package org.tibennetwork.iarcade.mame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

/**
 * Mame options deducing tests
 *
 * - Boolean options
 * - Negative boolean options
 * - Non boolean options
 * - Non boolean options, but default value like boolean
 * - Commands (non options)
 * - Media types
 */
public class TestCommandLineOptionsFactory {

    @Test
    public void testDeduceFromMameRuntimeWithBooleanOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options mameOpts = clof.deduceFromMameRuntime(mame).getOptions();

        assertTrue(mameOpts.hasOption("waitvsync"));

        Option opt = mameOpts.getOption("waitvsync");

        assertFalse(opt.hasArg());

    }

    @Test
    public void testDeduceFromMameRuntimeWithNegativeBooleanOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options mameOpts = clof.deduceFromMameRuntime(mame).getOptions();

        assertTrue(mameOpts.hasOption("nowaitvsync"));

        Option opt = mameOpts.getOption("nowaitvsync");

        assertFalse(opt.hasArg());

    }

    @Test
    public void testDeduceFromMameRuntimeWithNonBooleanOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options mameOpts = clof.deduceFromMameRuntime(mame).getOptions();

        assertTrue(mameOpts.hasOption("rompath"));
        assertFalse(mameOpts.hasOption("norompath"));

        Option opt = mameOpts.getOption("rompath");

        assertTrue(opt.hasArg());

    }

    @Test
    public void testDeduceFromMameRuntimeWithNonBooleanOptionHavingDefaultValueLikeBoolean ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options mameOpts = clof.deduceFromMameRuntime(mame).getOptions();

        assertTrue(mameOpts.hasOption("prescale"));
        assertFalse(mameOpts.hasOption("noprescale"));

        Option opt = mameOpts.getOption("prescale");

        assertTrue(opt.hasArg());

    }

    @Test
    public void testDeduceFromMameRuntimeWithCommandOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options commands 
            = clof.deduceFromMameRuntime(mame).getCommands();

        assertTrue(commands.hasOption("help"));

        Option command = commands.getOption("help");

        assertFalse(command.hasArg());

    }

    @Test
    public void testDeduceFromMameRuntimeWithCommandOptionHavingNoOptionsTypeOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options commands 
            = clof.deduceFromMameRuntime(mame).getCommands();

        assertFalse(commands.hasOption("waitvsync"));

    }

    @Test
    public void testDeduceFromMameRuntimeWithMediaTypeOption ()
            throws FileNotFoundException,
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        Options mediaTypes 
            = clof.deduceFromMameRuntime(mame).getMediaTypes();

        assertTrue(mediaTypes.hasOption("cart"));

        Option opt = mediaTypes.getOption("cart");

        assertTrue(opt.hasArg());

    }
}
