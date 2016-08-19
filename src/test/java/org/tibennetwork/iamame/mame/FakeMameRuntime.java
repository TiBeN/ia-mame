package org.tibennetwork.iamame.mame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;

/**
 * Fake MameRuntime which avoid the use of a real Mame process 
 * for testing purposes.
 */
public class FakeMameRuntime implements MameRuntime {
    
    /**
     * Hold the input streams to return sequencially when 
     * calling methods output InputStreams
     */
    private List<InputStream> inputStreamsToReturn;

    private int inputStreamIndex = 0;

    private String version;

    public Set<File> getRomsPaths () {
        return null;
    }

    public String getVersion () {
        return this.version;
    }

    public void setVersion (String v) {
        this.version = v;
    }

    /**
     * Set the List of InputStream to be returned sequentially when calling
     * methods which returns InputStreams.
     */
    public void setInputStreamsToReturn (List<InputStream> is) {
        this.inputStreamsToReturn = is;
        this.inputStreamIndex = 0;
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
        InputStream is = this.inputStreamsToReturn.get(this.inputStreamIndex);
        this.inputStreamIndex++;
        return is;
    }
 
    /**
     * Same as MameRuntime.executeAndReturnStdoutAsInputStream 
     * but merge rawArgs with default options by default
     */
    public InputStream executeAndReturnStdoutAsInputStream (String[] rawArgs) 
            throws IOException, InterruptedException, MameExecutionException {
        InputStream is = this.inputStreamsToReturn.get(this.inputStreamIndex);
        this.inputStreamIndex++;
        return is;
    }

    /**
     * Determines the path among the list of rom paths were roms 
     * and software should be put.
     */
    public File getWritableRomPath () throws NoWritableRomPathException {
        return null;
    }
}
