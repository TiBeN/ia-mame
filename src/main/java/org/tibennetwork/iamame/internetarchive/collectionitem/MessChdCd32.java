package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

public class MessChdCd32 extends GenericChdCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_cd32/" 
            + "MESS_0.149_CHD_cd32.zip/MESS_0.149_CHD_cd32/%1$s/%2$s";

    public MessChdCd32 (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public String getSoftwareFileUrlPattern () {
        return this.softwareFileUrlPattern;
    }

}
