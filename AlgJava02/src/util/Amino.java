/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.util.*;
/**
 * A class providing basic utilities to deal with
 * amino acids and their corresponding codons.
 * @author hendrik1
 */
public class Amino {
    /**the one letter amino acid code*/
    private char aaCode;
    /**hash map to accelerate 'one letter to three letter' look ups*/
    private static HashMap<String,String> ONE_2_THREE;
    /**hash map to accelerate 'three letter to one letter' look ups*/
    private static HashMap<String,String> THREE_2_ONE;
    /**hash map to accelerate 'aa code to codon set' look ups*/
    private static HashMap<String,CodonArray> CODE_2_CODON;
    /**hash map to accelerate 'codon set to aa code' look ups*/
    private static HashMap<CodonArray,String> CODON_2_CODE;
    /**
     * Constructs an empty amino acid object. This
     * constructor is crucial to initialize the look
     * up maps but cannot be accessed from outside.
     */
    private Amino (){
        if(ONE_2_THREE==null) setOneToThree();
        if(THREE_2_ONE==null) setThreeToOne();
        if(CODE_2_CODON==null) initLookUps();
    }
    /**
     * Constructs an amino acid object if the amino acid code
     * <tt>aa</tt> is present in one of the provided look up maps.
     * <b>Note</b>, by convention the one letter code has to be an
     * upper case letter, the three letter code has to be as follows:
     * first letter upper case, the last two letters lower case. <b>This
     * constructor raises an exception, if the convention is violated by
     * the provided parameter <tt>aa</tt></b>.
     * @param aa the amino acid code, either three or one letter
     * @throws util.AminoException if no such code was found (no such entry or
     * convention violation)
     */
    public Amino (String aa) throws AminoException {
        this();
        String aCode;
        if((aCode = ONE_2_THREE.get(aa.toUpperCase()))!=null) aaCode = aa.charAt(0);
        else if ((aCode = THREE_2_ONE.get(aa))!=null) aaCode = aCode.charAt(0);
        else throw new AminoException ("\nNo such amino acid code found: "+aa);
    }
    /**
     * Returns the amino acid associated to the <tt>codon</tt> or
     * 'STOP'.
     * @param codon the codon
     * @return the single letter amino acid code or stop
     */
    public String getAAFromCodon (String codon){
        String aa = "STOP";
        String cp = codon.toUpperCase();
        for (CodonArray ar:CODON_2_CODE.keySet()){
            if(ar.containsCodon(cp)){
                aa = CODON_2_CODE.get(ar);
                break;
            }
        }
        return aa;
    }
    /**
     * Returns a set of all codons associated to the amino acid
     * <tt>aa</tt>. <b>Note</b>, that amino acid codes not conform
     * to the given convention will cause an {@link AminoException}.
     * @param aa the amino acid code (either one or three letter)
     * @return a set of all codons
     * @throws util.AminoException unknown coding format
     */
    public HashSet<String> getCodonFromAA (String aa) throws AminoException {
        if(aa.length()==1)
            return CODE_2_CODON.get(aa.toUpperCase()).getSet();
        else{
            String oneCode;
            if((oneCode=THREE_2_ONE.get(aa))==null) throw new AminoException ("\n"+
                    "Unknown coding format: "+aa);
            return CODE_2_CODON.get(oneCode).getSet();
        }
    }
    /**
     * Returns the one letter code of this amino acid object.
     * @return the one letter code
     */
    public String getOneLetterCode (){
        return ""+aaCode;
    }
    /**
     * Returns the three letter code of this amino acid object.
     * @return the three letter code
     */
    public String getThreeLetterCode (){
        return THREE_2_ONE.get(""+aaCode);
    }
    /**
     * Sets the 'codon set to aa code' map and its reversed map,
     * i.e. 'aa code to codon set'.
     */
    private static void initLookUps (){
        CODE_2_CODON = new HashMap<String,CodonArray> ();
        CODE_2_CODON.put("A",new CodonArray(new String[]{"GCA","GCC","GCG","GCU"}));
        CODE_2_CODON.put("C",new CodonArray(new String[]{"UGC","UGU"}));
        CODE_2_CODON.put("D",new CodonArray(new String[]{"GAC","GAU"}));
        CODE_2_CODON.put("E",new CodonArray(new String[]{"GAA","GAG"}));
        CODE_2_CODON.put("F",new CodonArray(new String[]{"UUC","UUU"}));
        CODE_2_CODON.put("G",new CodonArray(new String[]{"GGA","GGC","GGG","GGU"}));
        CODE_2_CODON.put("H",new CodonArray(new String[]{"CAC","CAU"}));
        CODE_2_CODON.put("I",new CodonArray(new String[]{"AUA","AUC","AUU"}));
        CODE_2_CODON.put("K",new CodonArray(new String[]{"AAA","AAG"}));
        CODE_2_CODON.put("L",new CodonArray(new String[]{"CUA","CUC","CUG","CUU",
                                                        "UUA","UUG"}));
        CODE_2_CODON.put("M",new CodonArray(new String[]{"AUG"}));
        CODE_2_CODON.put("N",new CodonArray(new String[]{"AAC","AAU"}));
        CODE_2_CODON.put("P",new CodonArray(new String[]{"CCA","CCC","CCG","CCU"}));
        CODE_2_CODON.put("Q",new CodonArray(new String[]{"CAA","CAG"}));
        CODE_2_CODON.put("R",new CodonArray(new String[]{"CGA","CGC","CGG","CGU",
                                                         "AGA","AGG"}));
        CODE_2_CODON.put("S",new CodonArray(new String[]{"UCA","UCC","UCG","UCU",
                                                        "AGC","AGU"}));
        CODE_2_CODON.put("T",new CodonArray(new String[]{"ACA","ACC","ACG","ACU"}));
        CODE_2_CODON.put("V",new CodonArray(new String[]{"GUA","GUC","GUG","GUU"}));
        CODE_2_CODON.put("W",new CodonArray(new String[]{"UGG"}));
        CODE_2_CODON.put("Y",new CodonArray(new String[]{"UAC","UAU"}));
        CODON_2_CODE = new HashMap<CodonArray,String> ();
        CODON_2_CODE.put(new CodonArray(new String[]{"GCA","GCC","GCG","GCU"}),"A");
        CODON_2_CODE.put(new CodonArray(new String[]{"UGC","UGU"}),"C");
        CODON_2_CODE.put(new CodonArray(new String[]{"GAC","GAU"}),"D");
        CODON_2_CODE.put(new CodonArray(new String[]{"GAA","GAG"}),"E");
        CODON_2_CODE.put(new CodonArray(new String[]{"UUC","UUU"}),"F");
        CODON_2_CODE.put(new CodonArray(new String[]{"GGA","GGC","GGG","GGU"}),"G");
        CODON_2_CODE.put(new CodonArray(new String[]{"CAC","CAU"}),"H");
        CODON_2_CODE.put(new CodonArray(new String[]{"AUA","AUC","AUU"}),"I");
        CODON_2_CODE.put(new CodonArray(new String[]{"AAA","AAG"}),"K");
        CODON_2_CODE.put(new CodonArray(new String[]{"CUA","CUC","CUG","CUU",
                                                        "UUA","UUG"}),"L");
        CODON_2_CODE.put(new CodonArray(new String[]{"AUG"}),"M");
        CODON_2_CODE.put(new CodonArray(new String[]{"AAC","AAU"}),"N");
        CODON_2_CODE.put(new CodonArray(new String[]{"CCA","CCC","CCG","CCU"}),"P");
        CODON_2_CODE.put(new CodonArray(new String[]{"CAA","CAG"}),"Q");
        CODON_2_CODE.put(new CodonArray(new String[]{"CGA","CGC","CGG","CGU",
                                                         "AGA","AGG"}),"R");
        CODON_2_CODE.put(new CodonArray(new String[]{"UCA","UCC","UCG","UCU",
                                                        "AGC","AGU"}),"S");
        CODON_2_CODE.put(new CodonArray(new String[]{"ACA","ACC","ACG","ACU"}),"T");
        CODON_2_CODE.put(new CodonArray(new String[]{"GUA","GUC","GUG","GUU"}),"V");
        CODON_2_CODE.put(new CodonArray(new String[]{"UGG"}),"W");
        CODON_2_CODE.put(new CodonArray(new String[]{"UAC","UAU"}),"Y");
    }
    /**
     * Sets the 'three to one' code look up map.
     */
    private static void setThreeToOne(){
        THREE_2_ONE = new HashMap<String,String> ();
        THREE_2_ONE.put("Ala","A");//1st
        THREE_2_ONE.put("Cys","C");//2nd
        THREE_2_ONE.put("Asp","D");//3rd
        THREE_2_ONE.put("Glu","E");//4th
        THREE_2_ONE.put("Phe","F");//5th
        THREE_2_ONE.put("Gly","G");//6th
        THREE_2_ONE.put("His","H");//7th
        THREE_2_ONE.put("Ile","I");//8th
        THREE_2_ONE.put("Lys","K");//9th
        THREE_2_ONE.put("Leu","L");//10th
        THREE_2_ONE.put("Met","M");//11th
        THREE_2_ONE.put("Asn","N");//12th
        THREE_2_ONE.put("Pro","P");//13th
        THREE_2_ONE.put("Gln","Q");//14th
        THREE_2_ONE.put("Arg","R");//15th
        THREE_2_ONE.put("Ser","S");//16th
        THREE_2_ONE.put("Thr","T");//17th
        THREE_2_ONE.put("Val","V");//18th
        THREE_2_ONE.put("Trp","W");//19th
        THREE_2_ONE.put("Tyr","Y");//20th
    }
    /**
     * Sets the 'one to three' look up map.
     */
    private static void setOneToThree(){
        ONE_2_THREE = new HashMap<String,String> ();
        ONE_2_THREE.put("A","Ala");//1st
        ONE_2_THREE.put("C","Cys");//2nd
        ONE_2_THREE.put("D","Asp");//3rd
        ONE_2_THREE.put("E","Glu");//4th
        ONE_2_THREE.put("F","Phe");//5th
        ONE_2_THREE.put("G","Gly");//6th
        ONE_2_THREE.put("H","His");//7th
        ONE_2_THREE.put("I","Ile");//8th
        ONE_2_THREE.put("K","Lys");//9th
        ONE_2_THREE.put("L","Leu");//10th
        ONE_2_THREE.put("M","Met");//11th
        ONE_2_THREE.put("N","Asn");//12th
        ONE_2_THREE.put("P","Pro");//13th
        ONE_2_THREE.put("Q","Gln");//14th
        ONE_2_THREE.put("R","Arg");//15th
        ONE_2_THREE.put("S","Ser");//16th
        ONE_2_THREE.put("T","Thr");//17th
        ONE_2_THREE.put("V","Val");//18th
        ONE_2_THREE.put("W","Trp");//19th
        ONE_2_THREE.put("Y","Tyr");//20th
    }
    /**
     * A subclass of {@link TypedStack}.
     */
    private static class CodonArray extends TypedStack<String> {
        /**
         * Constructs an <code>CodonArray</code> object, where
         * each substack contains the trailing part of the provided
         * array <tt>ar</tt>.
         * @param ar an array of strings
         */
        CodonArray (String[] ar){
            super(0,ar);
        }
        /**
         * Returns true, if and only if this codon array object
         * contains a matching codon.
         * @param codon the codon to look up
         * @return true, if it is contained
         */
        boolean containsCodon (String codon){
            if(codon.length()!=3) throw new IllegalArgumentException ("");
            return contains(codon);
        }
    }
    public static void main (String[] args){
        Amino aa = new Amino("K");
        System.out.println(aa.getCodonFromAA("Lys"));
        System.out.println(aa.getAAFromCodon("uGg"));
    }
}
