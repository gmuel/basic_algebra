/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.util.HashSet;

/**
 * An enumeration of all 20 standard amino acids
 * plus the three stop codons 'AMBER', 'OCHRE' and 'OPAL'.
 * <b>Note</b>, that the three stop codons are included and have their own
 * single and three letter codes:
 * <p><li><tt>AMBER - single: B, triple: Amb, codon: UAG</tt></li>
 * <li><tt>OCHRE - single: J, triple: Och, codon: UAA</tt></li>
 * <li><tt>OPAL  - single: O, triple: Opl, codon: UGA</tt></li>
 * @author hendrik1
 */
public enum AminoAcid {
    /**the amino acid alanine*/
    A("A","Ala",new String[]{"GCA","GCC","GCG","GCU"}),
    /**the stop codon 'AMBER'*/
    B("B","Amb",new String[]{"UAG"}),//the amber stop codon
    /**the amino acid cysteine*/
    C("C","Cys",new String[]{"UGC","UGU"}),
    /**the amino acid aspartate*/
    D("D","Asp",new String[]{"GAC","GAU"}),
    /**the amino acid glutamate*/
    E("E","Glu",new String[]{"GAA","GAG"}),
    /**the amino acid phenylalanine*/
    F("F","Phe",new String[]{"UUC","UUU"}),
    /**the amino acid glycine*/
    G("G","Gly",new String[]{"GGA","GGC","GGG","GGU"}),
    /**the amino acid histidine*/
    H("H","His",new String[]{"CAC","CAU"}),
    /**the amino acid isoleucine*/
    I("I","Ile",new String[]{"AUA","AUC","AUU"}),
    /**the stop codon 'OCHRE'*/
    J("J","Och",new String[]{"UAA"}),//the ochre stop codon
    /**the amino acid lysine*/
    K("K","Lys",new String[]{"AAA","AAG"}),
    /**the amino acid leucine*/
    L("L","Leu",new String[]{"CUA","CUC","CUG","CUU","UUA","UUG"}),
    /**the amino acid methionine*/
    M("M","Met",new String[]{"AUG"}),
    /**the amino acid aspargine*/
    N("N","Asn",new String[]{"AAC","AAU"}),
    /**the stop codon 'OPAL'*/
    O("O","Opl",new String[]{"UGA"}),//the opal stop codon
    /**the amino acid proline*/
    P("P","Pro",new String[]{"CCA","CCC","CCG","CCU"}),
    /**the amino acid glutamine*/
    Q("Q","Gln",new String[]{"CAA","CAG"}),
    /**the amino acid arginine*/
    R("R","Arg",new String[]{"CGA","CGC","CGG","CGU","AGA","AGG"}),
    /**the amino acid serine*/
    S("S","Ser",new String[]{"UCA","UCC","UCG","UCU","AGC","AGU"}),
    /**the amino acid threonine*/
    T("T","Thr",new String[]{"ACA","ACC","ACG","ACU"}),
    /**the amino acid valine*/
    V("V","Val",new String[]{"GUA","GUC","GUG","GUU"}),
    /**the amino acid tryptophane*/
    W("W","Trp",new String[]{"UGG"}),
    /**the amino acid tyrosine*/
    Y("Y","Tyr",new String[]{"UAC","UAU"});
    /**
     * Constructs an amino acid object.
     * @param single single letter code
     * @param triple triple letter code
     * @param codonSet a string array of all codons
     */
    private AminoAcid (String single, String triple, String[] codonSet){
        this.single = single;
        this.triple = triple;
        this.codonSet = new CodonSet(codonSet);
    }
    /**the single letter code*/
    private String single;
    /**the triple letter code*/
    private String triple;
    /**a set of all codons*/
    private CodonSet codonSet;
    /**
     * Returns the single letter code
     * @return the single letter
     */
    public String getSingleLetter (){
        return single;
    }
    /**
     * Returns the triple letter code of
     * this amino acid
     * @return the triple letter
     */
    public String getTripleLetter (){
        return triple;
    }
    /**an inner class wrapping all codon objects
     * to a given amino acid*/
    private class CodonSet {
        /**the only field is a hash set*/
        private HashSet<Codon> set;
        /**
         * Constructs a codon set object
         * by instantiating each single codon
         * provided in the array <tt>codon</tt>
         * @param codons the codon string array
         */
        CodonSet(String[] codons){
            set = new HashSet<Codon> ();
            for (int i = 0; i < codons.length; i++)
                set.add(new Codon(codons[i]));
        }
        /**
         * Returns true, if and only if this codon
         * set contains <tt>codon</tt>.
         * @param codon the codon to look up
         * @return true, if it contains the <tt>codon</tt>
         */
        boolean contains(Codon codon){
            return set.contains(codon);
        }
    }
    /**
     * Returns the amino acid associated to the
     * character <tt>aa</tt>.
     * @param aa the amino acid character
     * @return the amino acid
     */
    public static AminoAcid getAminoFromChar (char aa){
        AminoAcid aA = null;
        for (AminoAcid a: AminoAcid.values()){
            if(aa==a.single.charAt(0)){aA = a; break;}
        }
        return aA;
    }
    /**
     * Returns the amino acid associated to the
     * <tt>codonStr</tt>, the codon string. Note,
     * that an exception is thrown if either the
     * codon string is not of length 3 or contains
     * at least one character not comform with standard
     * {@link Nucleotide} objects.
     * @param codonStr the codon string
     * @return the amino acid
     * @see Nucleotide
     * @throws java.lang.IllegalArgumentException invalid codon
     * string
     */
    public static AminoAcid getAminoFromCodon (String codonStr) throws IllegalArgumentException {
        Codon codon = new Codon (codonStr);
        AminoAcid aa = null;
        for (AminoAcid a:AminoAcid.values()){
            if(a.codonSet.contains(codon)){
                aa = a;
                break;
            }
        }
        return aa;
    }
    public boolean isStopAA (){
        return equals(B)||equals(J)||equals(O)?true:false;
    }
}
