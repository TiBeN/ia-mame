package org.tibennetwork.iamame.mame;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.ParseException;
import org.tibennetwork.iamame.IaMame;
import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;

/**
 * Mame binary facade
 */
public class MameRuntimeImpl implements MameRuntime {

    /**
     * The path of the Mame binary executable
     */
    private String binPath;

    /**
     * Raw options to use when calling Mame
     * internally
     */
    private String[] defaultOptions;

    /**
     * Directory containing the Mame 
     * executable
     */
    private String binDirectory;

    private Set<File> romsPaths;

    public MameRuntimeImpl (String binPath, String[] defaultOptions) 
            throws IOException, InterruptedException, ParseException {
        this.binPath = binPath;
        this.binDirectory = new File(binPath).getParent();
        this.defaultOptions = defaultOptions;
        IaMame.debug(String.format(
            "Mame binary directory: %s", 
            this.binDirectory));
        this.getRomsPathsFromBinary();
    }
  
    public Set<File> getRomsPaths() {
        return this.romsPaths;        
    }

    /**
     * Executes Mame using the given arguments.
     * The containing (java) process is locked until
     * Mame terminates. cess so execution is transparent
     * for the user.
     */
    public void execute (String[] rawArgs, boolean mergeArgsWithDefaultOptions) 
            throws IOException, 
                InterruptedException {
        this.initMameProcess(rawArgs, true, mergeArgsWithDefaultOptions)
            .waitFor();
    }

    /**
     * Same as MameRuntime.execute but merge rawArgs 
     * with default options by default
     */
    public void execute (String[] rawArgs) 
            throws IOException, InterruptedException {
        this.execute(rawArgs, true);
    }

    /**
     * Executes Mame using the given arguments and return
     * the stdout of the command as a List of String
     */
    public List<String> executeAndReturnStdout (
            String[] rawArgs, 
            boolean mergeArgsWithDefaultOptions)
            throws IOException, 
                InterruptedException, 
                MameExecutionException {
        
        Process mameProcess 
            = this.initMameProcess(rawArgs, false, mergeArgsWithDefaultOptions);

        BufferedReader mameRuntimeStdout = new BufferedReader(
            new InputStreamReader(mameProcess.getInputStream()));

        List<String> stdout = new ArrayList<>();
        String s; 
        
        while ((s = mameRuntimeStdout.readLine()) != null) {
            stdout.add(s);
        }
        
        int exitValue = mameProcess.waitFor();

        if (exitValue != 0) {
            throw new MameExecutionException(
                String.format(
                    "Mame process returned exit value %s", 
                    exitValue));
        }

        return stdout;
    }

    /**
     * Same as MameRuntime.executeAndReturnStdout
     * but merge rawArgs with default options by default
     */
    public List<String> executeAndReturnStdout (String[] rawArgs) 
            throws IOException, 
                InterruptedException, 
                MameExecutionException {
        return this.executeAndReturnStdout(rawArgs, true);
    }

    /**
     * Executes Mame using the given arguments and return
     * an InputStream connected to the process to access to 
     * std inputs/outputs
     */
    public InputStream executeAndReturnStdoutAsInputStream (
            String[] rawArgs, 
            boolean mergeArgsWithDefaultOptions)
            throws IOException, 
                InterruptedException, 
                MameExecutionException {
    
        return this.initMameProcess(rawArgs, false, mergeArgsWithDefaultOptions)
            .getInputStream();

    }
 
    /**
     * Same as MameRuntime.executeAndReturnStdoutAsInputStream 
     * but merge rawArgs with default options by default
     */
    public InputStream executeAndReturnStdoutAsInputStream (String[] rawArgs) 
            throws IOException, 
                InterruptedException, 
                MameExecutionException {
         return this.executeAndReturnStdoutAsInputStream(rawArgs, true);
    }

    /**
     * Determines the path among the list of rom paths were roms 
     * and software should be put.
     */
    public File getWritableRomPath () throws NoWritableRomPathException {
        for (File f: romsPaths) {
            if (f.isDirectory() && f.canWrite()) {
                return f;
            }
        } 

        throw new NoWritableRomPathException (
            "No writable path to write roms into");
    }

    private Process initMameProcess (
            String[] rawArgs, 
            boolean inheritIO, 
            boolean mergeArgsWithDefaultOptions) 
            throws IOException {
    
        List<String> args = new ArrayList<>();
        args.add(this.binPath);
        if (mergeArgsWithDefaultOptions) {
            args.addAll(Arrays.asList(this.defaultOptions));
        }
        args.addAll(Arrays.asList(rawArgs));
        
        ProcessBuilder builder = new ProcessBuilder(args);
        if (inheritIO) {
            builder.inheritIO();
        }
        builder.directory(new File(this.binDirectory));
        return builder.start();

    }

    private void getRomsPathsFromBinary ()
            throws IOException, 
                InterruptedException, 
                ParseException {

        String[] rawArgs = {"-showconfig"};

        List<String> mameStdout = null;

        try {
            mameStdout = this.executeAndReturnStdout(rawArgs);
        } catch( MameExecutionException e) {
            throw (RuntimeException) 
                new RuntimeException("Unhandled error occured while " 
                    + "launching mame to fetch rompaths")
                    .initCause(e);
        }
             
        this.romsPaths = new HashSet<File>();

        Pattern p = Pattern.compile("^rompath +(.*)$");
        for (String s : mameStdout) {

            Matcher m = p.matcher(s);
            if (m.matches()) {

                // Change temporary the current working dir
                // to mame binary directory to find paths relative to 
                // it
                String originalCwd = System.getProperty("user.dir");
                System.setProperty("user.dir", this.binDirectory);

                for(String path : m.group(1).split(";")) {

                    // Handle $HOME syntax
                    path = path.replace("$HOME", 
                        System.getProperty("user.home"));

                    File dir = new File(path);

                    dir = new File(dir.getAbsolutePath());
                    if(dir.isDirectory()) {
                        IaMame.debug("rompath found: " + dir);
                        this.romsPaths.add(dir);
                    }
                }

                // Revert back the original cwd
                System.setProperty("user.dir", originalCwd);

                break;
            }
        }
        
    }

}
