package org.tibennetwork.iamame.mame;

import java.util.Arrays;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.not;

public class TestMameArguments {

    @Test 
    public void testGetRawOptionsArgsDoesntReturnMediaArgument () 
            throws InvalidMameArgumentsException {
        
        String[] args = {"-rompath", "/tmp/roms", "snes", "-cart", "dkongca"};
        MameArguments ma = new MameArguments(args);

        String[] optionsArgs = ma.getRawOptionsArgs();
    
        assertThat(
                Arrays.asList(optionsArgs), 
                not(hasItems("-cart", "dkongca")));

    }

}
