package org.tibennetwork.iamame.internetarchive.collectionitem;

import java.io.File;
import java.util.Set;

public class MessChdVsmileCd extends GenericChdCollectionItem {

    private String softwareFileUrlPattern 
        = "http://archive.org/download/MESS_0.149_CHD_vsmile_cd/" 
            + "MESS_0.149_CHD_vsmile_cd.zip/MESS_0.149_CHD_vsmile_cd/%1$s/%2$s";

    public MessChdVsmileCd (Set<File> romsPaths, File writableRomPath) {
        super(romsPaths, writableRomPath);
    }

    public String getSoftwareFileUrlPattern () {
        return this.softwareFileUrlPattern;
    }
    

}
