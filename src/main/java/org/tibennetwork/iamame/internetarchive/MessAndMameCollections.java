package org.tibennetwork.iamame.internetarchive;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.MameVersion;
import org.tibennetwork.iamame.mame.Software;
import org.tibennetwork.iamame.mame.SoftwareChdFile;
import org.tibennetwork.iamame.mame.SoftwareRomFile;

/**
 * Main archive Mame romsets collections entry point
 *
 * @Url https://archive.org/details/messmame
 */
public class MessAndMameCollections {

  /**
   * Download missing machine ROMs and CHDs for the given machine on the rompath
   *
   * @Param version
   * @Param romsPaths Paths where are stored romfiles
   * @Param writableRomPath Path where downloaded roms will be stored
   * @Param machine
   */
  public void download(Machine machine, MameVersion version,
      Set<File> romsPaths, File writableRomPath)
      throws FileNotFoundInCollectionException {

    // 1. Download missing ROM files
    //
    // Try to find needed roms into the romset version closest to or equal given
    // version then iterate through romset anteriors to version until required
    // roms are found.
    //
    // This is required mainly because of ROMs collection version 0.161 doesn't
    // not contains bios/devices roms (not available in archive.org). In this
    // case, theses roms are searched into older collections.

    Iterator<MachineRomSet> iterator = MachineRomSet.getRomSets(version);

    boolean neededRomsFoundInSets = false;

    romsetsloop: while (iterator.hasNext()) {

      MachineRomSet romSet = iterator.next();

      Set<String> missingRoms =
          machine.getMissingRomFiles(romSet.getFormat(), romsPaths);

      for (String rom : missingRoms) {
        if (!romSet.contains(rom)) {
          continue romsetsloop;
        }
      }

      neededRomsFoundInSets = true;

      for (String rom : missingRoms) {
        romSet.download(writableRomPath, rom);
      }

    }

    if (!neededRomsFoundInSets) {
      StringBuilder builder =
          new StringBuilder("Required rom files for machine '");
      builder.append(machine.getName());
      builder.append("' can\'t be found in any IA romset collections");
      throw new FileNotFoundInCollectionException(builder.toString());
    }

    // 2. Download missing CHD files

    String machineName = machine.getName();

    Set<String> missingChds = machine.getMissingChdFiles(romsPaths);

    for (String chd : missingChds) {
      MachineChdSet chdSet = MachineChdSet.findBest(version);
      if (chdSet.contains(machineName, chd)) {
        chdSet.download(writableRomPath, machineName, chd);
      } else {
        StringBuilder builder = new StringBuilder("Required CHD file '");
        builder.append(chd);
        builder.append("' for machine '");
        builder.append(machineName);
        builder.append("' can\'t be found in IA romset collections ");
        builder.append(chdSet.getVersion());
        throw new FileNotFoundInCollectionException(builder.toString());
      }
    }

  }

  /**
   * Download missing softwarelist ROMs and CHDs on the rom set matching exactly
   * or nearest lower the given Mame version.
   *
   * @Param software Software to download
   * @Param version Mame version
   * @Param romsPaths Paths where are stored romfiles
   * @Param writableRomPath Path where downloaded roms will be stored
   * @Param machine
   */
  public void download(Software software, MameVersion version,
      Set<File> romsPaths, File writableRomPath)
      throws FileNotFoundInCollectionException {

    // 1. Download missing ROM files


    Set<SoftwareRomFile> missingRomFiles =
        software.getMissingRomFiles(romsPaths);

    if (!missingRomFiles.isEmpty()) {

      SoftwareListRomSet romSet = SoftwareListRomSet.findBest(version);

      for (SoftwareRomFile rom : missingRomFiles) {
        if (romSet.contains(rom.getListName(), rom.getName())) {
          romSet.download(writableRomPath, rom.getListName(), rom.getName());
        } else {
          StringBuilder builder =
              new StringBuilder("Required ROM files for software '");
          builder.append(rom.getName());
          builder.append("' can\'t be found in IA romset collections ");
          builder.append(romSet.getVersion());
          throw new FileNotFoundInCollectionException(builder.toString());
        }

      }

    }

    // 2. Download missing CHD files

    Set<SoftwareChdFile> missingFiles = software.getMissingChdFiles(romsPaths);

    if (!missingFiles.isEmpty()) {

      SoftwareListChdSet chdSet = SoftwareListChdSet.findBest(version);

      for (SoftwareChdFile chd : missingFiles) {
        if (chdSet.contains(chd.getListName(), chd.getSoftwareName(),
            chd.getName())) {
          chdSet.download(writableRomPath, chd.getListName(),
              chd.getSoftwareName(), chd.getName());
        } else {
          StringBuilder builder = new StringBuilder("Required CHD file '");
          builder.append(chd.getName());
          builder.append("' for software '");
          builder.append(chd.getSoftwareName());
          builder.append("' can\'t be found in IA romset collections ");
          builder.append(chdSet.getVersion());
          throw new FileNotFoundInCollectionException(builder.toString());
        }

      }

    }

  }
}
