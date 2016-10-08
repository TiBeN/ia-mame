package org.tibennetwork.iamame.mame;

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

public class TestMameArguments {

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
        MameArguments ma = new MameArguments(mameOpts, args);

        ma.validate();

        String[] optionsArgs = ma.getRawOptionsArgs();
    
        assertThat(
                Arrays.asList(optionsArgs), 
                not(hasItems("-cart", "dkongca")));

    }

}
