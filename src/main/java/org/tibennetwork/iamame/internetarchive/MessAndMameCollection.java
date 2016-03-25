package org.tibennetwork.iamame.internetarchive;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tibennetwork.iamame.internetarchive.collectionitem.FileNotFoundInCollectionItem;
import org.tibennetwork.iamame.internetarchive.collectionitem.MachineRomsCollectionItem;
import org.tibennetwork.iamame.internetarchive.collectionitem.MameRoms;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessBios;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdCd32;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdCdi;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdMegaCd;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdMegaCdJpn;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdNeoCd;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdPceCd;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdPippin;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdPsx;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdSaturn;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessChdSegaCd;
import org.tibennetwork.iamame.internetarchive.collectionitem.MessSoftwareListRoms;
import org.tibennetwork.iamame.internetarchive.collectionitem.SoftwareFileNotFoundInCollectionException;
import org.tibennetwork.iamame.internetarchive.collectionitem.SoftwareListCollectionItem;
import org.tibennetwork.iamame.mame.Machine;
import org.tibennetwork.iamame.mame.Software;

public class MessAndMameCollection {

    /**
     * Collection items containing machine roms and bios
     */
    private List<MachineRomsCollectionItem> machineRomsCollectionItems
        = new ArrayList<>();

    /**
     * Hash collection of specific software lists (CHDs collections)
     */
    private Map<String, SoftwareListCollectionItem> 
        softwareCollectionItems = new HashMap<>();

    /**
     * Mess software list collection
     */
    private SoftwareListCollectionItem messSoftwareListRoms;

    public MessAndMameCollection (List<File> romsPaths, File writableRomPath) {

        // Instanciate the collection items
        this.machineRomsCollectionItems
            .add(new MameRoms(romsPaths, writableRomPath));
        this.machineRomsCollectionItems
            .add(new MessBios(romsPaths, writableRomPath));

        this.messSoftwareListRoms
            = new MessSoftwareListRoms(romsPaths, writableRomPath);

        this.softwareCollectionItems.put(
            "pcecd", 
            new MessChdPceCd(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "psx", 
            new MessChdPsx(romsPaths, writableRomPath));
        
        this.softwareCollectionItems.put(
            "cdi", 
            new MessChdCdi(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "saturn", 
            new MessChdSaturn(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "segacd", 
            new MessChdSegaCd(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "neocd", 
            new MessChdNeoCd(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "megacd", 
            new MessChdMegaCd(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "pippin", 
            new MessChdPippin(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "megacdj", 
            new MessChdMegaCdJpn(romsPaths, writableRomPath));

        this.softwareCollectionItems.put(
            "cd32", 
            new MessChdCd32(romsPaths, writableRomPath));
    }

    /**
     * search on items of the collection for the needed roms
     * of the given machine then download them on the rom path
     * set in constructor.
     */
    public void download (Machine machine) 
            throws MachineRomFileNotFoundInCollection {

        for (MachineRomsCollectionItem mci
                : this.machineRomsCollectionItems) {
            try {
                mci.download(machine);
            } catch (FileNotFoundInCollectionItem e) {
                continue;
            }
            return;
        }

        throw new MachineRomFileNotFoundInCollection(
            String.format("Needed Rom files for machine %s has not"
               + " been found on the collection",
            machine.getName()));
        
    }

    /**
     * search on items of the collection for the needed software file
     * then download it on the rom path set in constructor.
     */
    public void download (Software software) 
            throws SoftwareFileNotFoundInCollectionException {
    
        String sl = software.getSoftwareList().getName();

        if (this.softwareCollectionItems.containsKey(sl)) {
            try {    
                this.softwareCollectionItems.get(sl).download(software);
            } catch (FileNotFoundInCollectionItem e) {
                String.format("Needed file for software %s has not"
                   + " been found on the collection",
                software.getName());
                
            }
        
        } else {    

            try {    
                this.messSoftwareListRoms.download(software);
            } catch (FileNotFoundInCollectionItem e) {
                String.format("Needed file for software %s has not"
                   + " been found on the collection",
                software.getName());
                
            }
        }
    }

}
