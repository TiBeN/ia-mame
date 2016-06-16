package org.tibennetwork.iamame.mame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;

/**
 * Fake MameRuntime which avoid the use of a real Mame process 
 * for testing purposes.
 */
public class FakeMameRuntime implements MameRuntime {
    
    public List<File> getRomsPaths() {
        return null;
    }

    public void execute (String[] rawArgs, boolean mergeArgsWithDefaultOptions) 
            throws IOException, InterruptedException {}

    public void execute (String[] rawArgs) 
        throws IOException, InterruptedException {}

    public List<String> executeAndReturnStdout (
            String[] rawArgs, boolean mergeArgsWithDefaultOptions)
            throws IOException, InterruptedException, MameExecutionException {
    
        return null;        
    }

    public List<String> executeAndReturnStdout (String[] rawArgs) 
            throws IOException, InterruptedException, MameExecutionException {
        return null;
    }

    /**
     * Executes Mame using the given arguments and return
     * an InputStream connected to the process to access to 
     * std inputs/outputs
     */
    public InputStream executeAndReturnStdoutAsInputStream (
            String[] rawArgs, boolean mergeArgsWithDefaultOptions)
            throws IOException, InterruptedException, MameExecutionException {
            
        return null;
    }
 
    /**
     * Same as MameRuntime.executeAndReturnStdoutAsInputStream 
     * but merge rawArgs with default options by default
     */
    public InputStream executeAndReturnStdoutAsInputStream (String[] rawArgs) 
            throws IOException, InterruptedException, MameExecutionException {
        return null;
    }

    /**
     * Determines the path among the list of rom paths were roms 
     * and software should be put.
     */
    public File getWritableRomPath () throws NoWritableRomPathException {
        return null;
    }
}
