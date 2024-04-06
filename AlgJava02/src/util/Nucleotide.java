/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 * A constant class of all four DNA nucleotides
 * @author hendrik1
 */
public enum Nucleotide {
    A ("A"), C ("C"), G ("G"), U ("U");
    /**the base string*/
    private String baseStr;
    Nucleotide (String baseStr){
        this.baseStr = baseStr;
    }
    /**
     * Returns the base string
     * @return the base string
     */
    public String getBaseString (){
        return baseStr;
    }
    /**
     * Returns the complementary nucleotide
     * @return the complementary nucleotide
     */
    public Nucleotide getComplement (){
        Nucleotide compl = null;
        switch (this){
            case A : {compl = U; break;}
            case U : {compl = A; break;}
            case C : {compl = G; break;}
            case G : {compl = C; break;}
        }
        return compl;
    }
    /**
     * Returns the nucleotide corresponding to
     * <tt>base</tt> or null, if no such nucleotide
     * exists (e.g. 'N'). <b>Note</b>, that this method
     * is case insensitive.
     * @param base the base character
     * @return the nucleotide
     */
    public static Nucleotide getNucleotide (char base){
        Nucleotide nucl = null;
        switch (base){
            case 'A' : {nucl = A; break;}
            case 'a' : {nucl = A; break;}
            case 'T' : {nucl = U; break;}
            case 't' : {nucl = U; break;}
            case 'U' : {nucl = U; break;}
            case 'u' : {nucl = U; break;}
            case 'C' : {nucl = C; break;}
            case 'c' : {nucl = C; break;}
            case 'G' : {nucl = G; break;}
            case 'g' : {nucl = G; break;}
        }
        return nucl;
    }
}
