/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 * An exception indicating that a violation of
 * the standard FASTA format was encountered
 * @author hendrik1
 */
public class FastaFormatException extends IllegalArgumentException {

    /**
     * Creates a new instance of <code>FastaFormatException</code> without detail message.
     */
    public FastaFormatException() {
    }


    /**
     * Constructs an instance of <code>FastaFormatException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FastaFormatException(String msg) {
        super(msg);
    }
}
