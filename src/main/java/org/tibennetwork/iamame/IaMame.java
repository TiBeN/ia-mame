package org.tibennetwork.iamame;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.ParseException;
import org.tibennetwork.iamame.CommandLineArguments.ExtractedMachineAndSoftwares;
import org.tibennetwork.iamame.internetarchive.FileNotFoundInCollectionException;
import org.tibennetwork.iamame.internetarchive.MessAndMameCollections;
import org.tibennetwork.iamame.internetarchive.NoWritableRomPathException;
import org.tibennetwork.iamame.mame.CommandLineOptions;
import org.tibennetwork.iamame.mame.CommandLineOptionsFactory;
import org.tibennetwork.iamame.mame.InvalidMameArgumentsException;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.MachineRepository;
import org.tibennetwork.iamame.mame.MameExecutionException;
import org.tibennetwork.iamame.mame.MameRuntime;
import org.tibennetwork.iamame.mame.MameRuntimeImpl;
import org.tibennetwork.iamame.mame.MameVersion;
import org.tibennetwork.iamame.mame.Software;
import org.tibennetwork.iamame.mame.SoftwareRepository;
import org.tibennetwork.iamame.mame.UnhandledMameVersionPatternException;

public class IaMame {

  public static void main(String[] args) {

    MameRuntime mame = null;
    String mameBinary = null;
    CommandLineOptions mameOptions = null;
    CommandLineArguments cliArgs = null;

    // Search for Mame binary

    try {
      mameBinary = IaMame.findMameBinary();
    } catch (MameBinaryNotFoundException e) {
      IaMame.errorAndExit(e.getMessage());
    }

    // Init MameRuntime

    try {

      mame = new MameRuntimeImpl(mameBinary);

      // Deduce mame command line options scheme

      mameOptions = new CommandLineOptionsFactory().deduceFromMameRuntime(mame);

    } catch (IOException | InterruptedException | ParseException
        | MameExecutionException | UnhandledMameVersionPatternException e) {
      IaMame.errorAndExit(
          "An error occured while trying to execute Mame: " + e.getMessage());
    }

    try {
      cliArgs = new CommandLineArguments(mameOptions, args);
      cliArgs.validate();
      mame.setDefaultOptions(cliArgs.getMameOptionsRawArgs());
      downloadFilesIfNeeded(cliArgs, mame);
    } catch (InvalidMameArgumentsException e) {
      IaMame.warn("An error occured while trying to parse command line: "
          + e.getMessage());
    }

    // Launch Mame if there is no "noexecmame"

    if (!cliArgs.contains("noexecmame")) {
      try {
        mame.execute(cliArgs.getMameRawArgs(), false);
      } catch (IOException | InterruptedException e) {
        IaMame.errorAndExit(
            "An error occured while trying to execute Mame: " + e.getMessage());
      }
    }

  }

  /**
   * Find and return as String the path to the MAME executable
   */
  public static String findMameBinary() throws MameBinaryNotFoundException {

    // Check the MAME_EXEC environment variable
    String mameExecEnvVar = System.getenv("MAME_EXEC");
    IaMame.debug(String.format("$MAME_EXEC: %s", mameExecEnvVar));
    if (mameExecEnvVar != null) {
      File mameBinary = new File(mameExecEnvVar);
      if (mameBinary.exists() && mameBinary.canExecute()) {
        IaMame
            .debug("MAME binary found on MAME_EXEC" + " environment variable");
        return mameExecEnvVar;
      }
    }

    String[] possibleBinaryNames = {"mame", "mame64", "mame.exe"};
    String candidateMameBinaryPath;
    File candidateMameBinary;
    String containingJarPath = null;

    // Search for mame binary on the directory containing the
    // containing jar.

    try {
      containingJarPath = new File(IaMame.class.getProtectionDomain()
          .getCodeSource().getLocation().toURI().getPath()).getParent();
      IaMame.debug(String.format("IaMame directory: %s", containingJarPath));
    } catch (URISyntaxException e) {
      // If entering in, its a bug.
      IaMame.debug(e.getMessage());
    }

    for (String b : possibleBinaryNames) {
      candidateMameBinaryPath = containingJarPath + File.separator + b;
      candidateMameBinary = new File(candidateMameBinaryPath);
      if (candidateMameBinary.exists() && candidateMameBinary.canExecute()) {
        IaMame.debug(String.format(
            "Mame binary found on the same directory than IaMame: %s",
            candidateMameBinaryPath));
        return candidateMameBinaryPath;
      }
    }

    // Search for Mame binary on the PATH environment variable
    String pathEnvVar = System.getenv("PATH");
    IaMame.debug(String.format("$PATH: %s", pathEnvVar));

    for (String p : pathEnvVar.split(":")) {
      for (String b : possibleBinaryNames) {
        candidateMameBinaryPath = p + File.separator + b;
        candidateMameBinary = new File(candidateMameBinaryPath);
        if (candidateMameBinary.exists() && candidateMameBinary.canExecute()) {
          IaMame.debug(String.format("Mame binary found on $PATH : %s",
              candidateMameBinaryPath));
          return candidateMameBinaryPath;
        }
      }

    }

    throw new MameBinaryNotFoundException("MAME executable has not been found");

  }

  private static void downloadFilesIfNeeded(CommandLineArguments mameArgs,
      MameRuntime mame) throws InvalidMameArgumentsException {

    // Do nothing if Mame command line arguments contains a `Mame command`,
    // like '-showusage', '-showconfig' etc.

    if (mameArgs.containsMameCommand()) {
      return;
    }

    ExtractedMachineAndSoftwares ems = null;

    try {
      ems = mameArgs.extractMachineAndSoftwares(new MachineRepository(mame),
          new SoftwareRepository(mame));
    } catch (IOException | InterruptedException e) {
      IaMame.errorAndExit("An error occured " + e.getMessage());
    }

    Machine machine = ems.getMachine();

    Set<File> romsPaths = null;
    File writableRomPath = null;

    try {
      romsPaths = mame.getRomsPaths();
      writableRomPath = mame.getWritableRomPath();
    } catch (IOException | InterruptedException | ParseException e) {
      IaMame.errorAndExit(
          "An error occured while trying to execute Mame: " + e.getMessage());
    } catch (NoWritableRomPathException e) {
      IaMame.errorAndExit(e.getMessage());
    }

    MameVersion version = mame.getVersion();
    MessAndMameCollections collections = new MessAndMameCollections();

    try {
      collections.download(machine, version, romsPaths, writableRomPath);
    } catch (FileNotFoundInCollectionException e) {
      IaMame.warn(e.getMessage());
    }

    if (ems.hasSoftwares()) {

      List<Software> softwares = ems.getSoftwares();

      for (Software software : softwares) {
        if (software.isRegularFile()) {
          return;
        }

        try {
          collections.download(software, version, romsPaths, writableRomPath);
        } catch (FileNotFoundInCollectionException e) {
          IaMame.warn(e.getMessage());
        }

      }

    }

  }

  public static void errorAndExit(String message) {
    System.err.println("[ia-mame error]: " + message);
    System.exit(1);
  }

  public static void debug(String message) {
    if (System.getProperties().containsKey("iamame.debug")
        && System.getProperty("iamame.debug").equals("1")) {
      System.err.println("[ia-mame debug]: " + message);
    }
  }

  public static void warn(String message) {
    System.err.println("[ia-mame warn]: " + message);
  }

  public static void info(String message) {
    System.out.println("[ia-mame info]: " + message);
  }

}
