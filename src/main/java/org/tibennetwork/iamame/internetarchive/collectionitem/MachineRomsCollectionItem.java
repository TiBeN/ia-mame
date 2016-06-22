package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Machine;

/**
 * Item containing Machine Roms/Bios.
 */
public abstract class MachineRomsCollectionItem extends CollectionItem {

    public MachineRomsCollectionItem (
            Set<File> romPaths, 
            File writableRomPath) {

        super(romPaths, writableRomPath);    

    }

    public abstract void download (Machine machine)
        throws FileNotFoundInCollectionItem;

}
