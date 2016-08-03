package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

public class MessChdPceCd extends GenericChdCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_pcecd/" 
            + "MESS_0.149_CHD_pcecd.zip/MESS_0.149_CHD_pcecd/%1$s/%2$s";

    public MessChdPceCd (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public String getSoftwareFileUrlPattern () {
        return this.softwareFileUrlPattern;
    }

}
