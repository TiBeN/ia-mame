package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

public class MessChdMegaCdJpn extends GenericChdCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_megacdj/" 
            + "MESS_0.149_CHD_megacdj.zip/MESS_0.149_CHD_megacdj/%1$s/%2$s";

    public MessChdMegaCdJpn (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public String getSoftwareFileUrlPattern () {
        return this.softwareFileUrlPattern;
    }

}
