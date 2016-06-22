package org.tibennetwork.iamame.mame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;

public interface MameRuntime {

    public Set<File> getRomsPaths();

    /**
     * Executes Mame using the given arguments.
     * The containing (java) process is locked until
     * Mame terminates. cess so execution is transparent
     * for the user.
     */
    public void execute (String[] rawArgs, boolean mergeArgsWithDefaultOptions) 
            throws IOException, InterruptedException;

    /**
     * Same as MameRuntime.execute but merge rawArgs 
     * with default options by default
     */
    public void execute (String[] rawArgs) 
        throws IOException, InterruptedException;

    /**
     * Executes Mame using the given arguments and return
     * the stdout of the command as a List of String
     */
    public List<String> executeAndReturnStdout (
            String[] rawArgs, boolean mergeArgsWithDefaultOptions)
            throws IOException, InterruptedException, MameExecutionException;
    /**
     * Same as MameRuntime.executeAndReturnStdout
     * but merge rawArgs with default options by default
     */
    public List<String> executeAndReturnStdout (String[] rawArgs) 
            throws IOException, InterruptedException, MameExecutionException;

    /**
     * Executes Mame using the given arguments and return
     * an InputStream connected to the process to access to 
     * std inputs/outputs
     */
    public InputStream executeAndReturnStdoutAsInputStream (
            String[] rawArgs, boolean mergeArgsWithDefaultOptions)
            throws IOException, InterruptedException, MameExecutionException;
 
    /**
     * Same as MameRuntime.executeAndReturnStdoutAsInputStream 
     * but merge rawArgs with default options by default
     */
    public InputStream executeAndReturnStdoutAsInputStream (String[] rawArgs) 
            throws IOException, InterruptedException, MameExecutionException;

    /**
     * Determines the path among the list of rom paths were roms 
     * and software should be put.
     */
    public File getWritableRomPath () throws NoWritableRomPathException;

}
