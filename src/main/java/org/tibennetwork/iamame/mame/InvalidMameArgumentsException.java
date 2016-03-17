package org.tibennetwork.iamame.mame;

/**
 * Exception related to given mame command
 * line arguments
 */
public class InvalidMameArgumentsException extends Exception {

    static final long serialVersionUID = 2L;

    public InvalidMameArgumentsException(String s) {
        super(s);  
    }

}
