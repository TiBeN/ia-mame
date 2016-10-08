package org.tibennetwork.iamame;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.tibennetwork.iamame.CommandLineArguments;
import org.tibennetwork.iamame.mame.CommandLineOptions;
import org.tibennetwork.iamame.mame.CommandLineOptionsFactory;
import org.tibennetwork.iamame.mame.FakeMameRuntime;
import org.tibennetwork.iamame.mame.InvalidMameArgumentsException;
import org.tibennetwork.iamame.mame.MameExecutionException;

public class TestCommandLineArguments {

    @Test 
    public void testGetRawOptionsArgsDoesntReturnMediaArgument () 
            throws InvalidMameArgumentsException, 
                   IOException,
                   InterruptedException,
                   MameExecutionException {
        
        // Init Options           
        FakeMameRuntime mame = new FakeMameRuntime();
        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(
            new FileInputStream("src/test/resources/showconfig.txt"));
        inputStreams.add(
            new FileInputStream("src/test/resources/showusage.txt"));
        mame.setInputStreamsToReturn(inputStreams);
        CommandLineOptionsFactory clof = new CommandLineOptionsFactory ();
        CommandLineOptions mameOpts 
            = clof.deduceFromMameRuntime(mame);
        
        String[] args = {"-rompath", "/tmp/roms", "snes", "-cart", "dkongca"};
        CommandLineArguments ma = new CommandLineArguments(mameOpts, args);

        ma.validate();

        String[] optionsArgs = ma.getRawOptionsArgs();
    
        assertThat(
                Arrays.asList(optionsArgs), 
                not(hasItems("-cart", "dkongca")));

    }

}
