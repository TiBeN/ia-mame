package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

public class MessChdMacHdd extends GenericChdCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_mac_hdd/" 
            + "MESS_0.149_CHD_mac_hdd.zip/MESS_0.149_CHD_mac_hdd/%1$s/%2$s";

    public MessChdMacHdd (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public String getSoftwareFileUrlPattern () {
        return this.softwareFileUrlPattern;
    }

}
