package org.tibennetwork.iarcade.mame;

public class MachineDoesntExistException extends Exception {

    static final long serialVersionUID = 4L;

    public MachineDoesntExistException(String s) {
        super(s);  
    }

}
