/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 * A class representing codons, i.e. all triples
 * of the RNA
 * @author hendrik1
 */
public class Codon {
    /**the first nucleotide*/
    Nucleotide  first;
    /**the second nucleotide*/
    Nucleotide second;
    /**the third nucleotide*/
    Nucleotide  third;
    /**
     * Constructs a codon object setting the triple
     * nucleotides accordingly
     * @param first the first nucleotide
     * @param second the second nucleotide
     * @param third the third nucleotide
     */
    public Codon (Nucleotide first, Nucleotide second, Nucleotide third){
        this.first = first;
        this.second= second;
        this.first = third;
    }
    /**
     * Constructs a codon object based on the <tt>codon</tt>
     * string, if and only if the length of the string is exactly
     * 3 and all characters represent a valid nucleotide.
     * @param codon the codon string
     * @throws java.lang.IllegalArgumentException either length not 3 or
     * at least one character does not represent one of the four standard nucleotides
     */
    public Codon (String codon) throws IllegalArgumentException {
        if(codon.length()!=3) throw new IllegalArgumentException ("Codon string must have length 3!");
        if((first = Nucleotide.getNucleotide(codon.charAt(0)))==null)
            throw new IllegalArgumentException ("Unknown base: "+codon.charAt(0));
        if((second= Nucleotide.getNucleotide(codon.charAt(1)))==null)
            throw new IllegalArgumentException ("Unknown base: "+codon.charAt(1));
        if((third = Nucleotide.getNucleotide(codon.charAt(2)))==null)
            throw new IllegalArgumentException ("Unknown base: "+codon.charAt(2));
    }
    /**
     * Returns a hash code for this <code>Codon</code>
     * object
     * @return the hash code
     */
    @Override
    public int hashCode (){
        return 37*first.hashCode()+19*second.hashCode()+third.hashCode();
    }
    /**
     * Returns true if and only if this object equals
     * <tt>o</tt>, i.e. all three nucleotides have to match.
     * <b>Note</b>, that this method throws an
     * <code>ClassCastException</code> if <tt>o</tt> is not
     * of type <code>Codon</code>.
     * @param o an object to compare
     * @return true if both match
     */
    @Override
    public boolean equals (Object o){
        if(o instanceof Codon){
            Codon c = (Codon) o;
            return first.equals(c.first)&&second.equals(c.second)&&
                    third.equals(c.third)?true:false;
        } throw new ClassCastException ("Found: "+o.getClass().getName()+
                "\nRequired: Codon!");
    }
    /**
     * Returns a string representation of
     * this object
     * @return a string
     */
    @Override
    public String toString (){
        return first.getBaseString()+second.getBaseString()+third.getBaseString();
    }
}
