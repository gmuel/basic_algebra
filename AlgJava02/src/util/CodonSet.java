/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author hendrik1
 */
public enum CodonSet {
    AAA("AAA",AminoAcid.K),AAC("AAC",AminoAcid.N),AAG("AAG",AminoAcid.K),AAU("AAU",AminoAcid.N),
    ACA("ACA",AminoAcid.T),ACC("ACC",AminoAcid.T),ACG("ACG",AminoAcid.T),ACU("ACU",AminoAcid.T),
    AGA("AGA",AminoAcid.R),AGC("AGC",AminoAcid.S),AGG("AGG",AminoAcid.R),AGU("AGU",AminoAcid.S),
    AUA("AUA",AminoAcid.I),AUC("AUC",AminoAcid.I),AUG("AUG",AminoAcid.M),AUU("AUU",AminoAcid.I),
    CAA("CAA",AminoAcid.Q),CAC("CAC",AminoAcid.H),CAG("CAG",AminoAcid.Q),CAU("CAU",AminoAcid.H),
    CCA("CCA",AminoAcid.P),CCC("CCC",AminoAcid.P),CCG("CCG",AminoAcid.P),CCU("CCU",AminoAcid.P),
    CGA("CGA",AminoAcid.R),CGC("CGC",AminoAcid.R),CGG("CGG",AminoAcid.R),CGU("CGU",AminoAcid.R),
    CUA("CUA",AminoAcid.L),CUC("CUC",AminoAcid.L),CUG("CUG",AminoAcid.L),CUU("CUU",AminoAcid.L),
    GAA("GAA",AminoAcid.E),GAC("GAC",AminoAcid.D),GAG("GAG",AminoAcid.E),GAU("GAU",AminoAcid.D),
    GCA("GCA",AminoAcid.A),GCC("GCC",AminoAcid.A),GCG("GCG",AminoAcid.A),GCU("GCU",AminoAcid.A),
    GGA("GGA",AminoAcid.G),GGC("GGC",AminoAcid.G),GGG("GGG",AminoAcid.G),GGU("GGU",AminoAcid.G),
    GUA("GUA",AminoAcid.V),GUC("GUC",AminoAcid.V),GUG("GUG",AminoAcid.V),GUT("GUU",AminoAcid.V),
    UAA("UAA",AminoAcid.J),UAC("UAC",AminoAcid.Y),UAG("UAG",AminoAcid.B),UAU("UAU",AminoAcid.Y),
    UCA("UCA",AminoAcid.S),UCC("UCC",AminoAcid.S),UCG("UCG",AminoAcid.S),UCU("UCU",AminoAcid.S),
    UGA("UGA",AminoAcid.O),UGC("UGC",AminoAcid.C),UGG("UGG",AminoAcid.W),UGU("UGU",AminoAcid.C),
    UUA("UUA",AminoAcid.L),UUC("UUC",AminoAcid.F),UUG("UUG",AminoAcid.L),UUT("UUU",AminoAcid.F);
    /**the codon triplet*/
    private Codon codon;
    /**the amino acid*/
    private AminoAcid aa;
    private CodonSet (String codonStr, AminoAcid aa){
        codon = new Codon (codonStr);
        this.aa = aa;
    }
    /**
     * Returns the amino acid associated to this
     * <code>CodonSet</code> object
     * @return the amino acid
     */
    public AminoAcid getAminoAcid (){
        return aa;
    }
    /**
     * Returns the codon associated to this
     * <code>CodonSet</code> object
     * @return the codon
     */
    public Codon getCodon (){
        return codon;
    }
    public static CodonSet getCodon (Codon codon){
        for (CodonSet co:CodonSet.values()){
            if(co.codon.equals(codon)) return co;
        } return null;
    }
    public boolean isStopCodon (){
        return UAA.codon.equals(codon)||UAG.codon.equals(codon)||UGA.codon.equals(codon);
    }
}
