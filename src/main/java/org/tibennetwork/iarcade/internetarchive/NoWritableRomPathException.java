package org.tibennetwork.iarcade.internetarchive;

public class NoWritableRomPathException extends Exception {

    static final long serialVersionUID = 4L;

    public NoWritableRomPathException(String s) {
        super(s);  
    }
}
