package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

import org.tibennetwork.iamame.mame.Software;

public abstract class SoftwareListCollectionItem extends CollectionItem {

    public SoftwareListCollectionItem (
            Set<File> romPaths, 
            File writableRomPath) {

        super(romPaths, writableRomPath);    

    }

    public abstract void download (Software software)
        throws FileNotFoundInCollectionItem;

}
