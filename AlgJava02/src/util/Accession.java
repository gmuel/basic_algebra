/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.util.regex.*;
/**
 * A class representing accession identifiers of a gene or gene
 * product
 * @author hendrik1
 */
public class Accession {
    /**the accession specifier:
     <ol><li>NM -> mRNA</li>
     <li>NP -> protein</li></ol>*/
    private String acSpec;
    /**the accession number, unique to each entry*/
    private int acNb;
    /**the accession version: new entries always gets a 1*/
    private int version;
    /**the data type associated to the entry
     <ol><li>{@link DataType#NUCLEIC} -> nucleic acid</li>
     <li>{@link DataType#PROT} -> protein</li></ol>*/
    private DataType type;
    /**
     * Constructs an accession object by decomposing the
     * provided argument string <tt>acString</tt>. Note that
     * strings violating the standard accession identifier format
     * will cause an exception.
     * @param acString the accession string
     */
    public Accession (String acString){
        setFields(acString);
        if(type==null) throw new IllegalArgumentException ("\nUnknown accession type:"+
                acString);
    }
    @Override
    public boolean equals (Object o){
        if(o instanceof Accession){
            Accession a = (Accession) o;
            char tChar = acSpec.charAt(0);
            char oChar = a.acSpec.charAt(0);
            return tChar==oChar&&a.acNb==acNb?true:false;
        } throw new ClassCastException ("\nFound: "+o.getClass().getName()+
                "\nRequired: Accession");
    }
    /**
     * Returns the accession identifier as a string
     * @return the accession identifier
     */
    public String toString (){
        return String.format("%1$s_%2$09d.%3$d", acSpec,acNb,version);
    }
    /**
     * Returns the accession number
     * @return the accession number
     */
    public int getAccessionNumber (){
        return acNb;
    }
    /**
     * Returns the accession specifier
     * @return the accession specifier
     */
    public String getAccessionSpecifier (){
        return acSpec;
    }
    /**
     * Returns the accession version number
     * @return the accession version number
     */
    public int getAccessionVersion (){
        return version;
    }
    /**
     * Returns the data type of this accession object as
     * a string
     * @return the data type
     */
    public String getDataType (){
        String s = null;
        switch (type){
            case M_RNA:{
                s = "mRNA";
                break;
            }
            case PROTEIN:{
                s = "protein";
                break;
            }
            case RNA : {
                s = "RNA";
                break;
            }
            case GENOMIC_DNA:{
                s = "gDNA";
                break;
            }
        }
        return s;
    }
    @Override
    public int hashCode (){
        return 37*acSpec.charAt(0)+acNb;
    }
    /**
     * Returns true if and only if this accession object
     * represents a protein
     * @return true if this is a protein
     */
    public boolean isProtein (){
    	return type.equals(DataType.PROTEIN)?true:false;
    }
    /**
     * Returns true if and only if this accession object
     * represents an mRNA
     * @return true if this is translatable (i.e. an mRNA)
     */
    public boolean isTranslatable (){
        return type.isTranslatable;
    }
    /**
     * Sets the fields of this object
     * @param acString the accession string
     */
    private void setFields (String acString){
        try{
            acSpec   = setFields(acString,"[ANXYZ][CMPR]");
            acNb     = Integer.parseInt(setFields(acString,"[0-9]{6,}"));
            String s = setFields(acString,"\\.[0-9]*");
            if(s!=null){
                s = s.replaceAll("\\.", "");
                version = Integer.parseInt(s);
            }
            switch (acSpec.charAt(1)){
                case 'R':{
                    type = DataType.RNA;
                    break;
                }
                case 'M':{
                    type = DataType.M_RNA;
                    break;
                }
                case 'P':{
                    type = DataType.PROTEIN;
                    break;
                }
            }
        } catch (NumberFormatException e){
            throw new IllegalArgumentException (e.getMessage());
        } catch (PatternSyntaxException e){
            throw new IllegalArgumentException (e.getMessage());
        }
    }
    /**
     * Decomposes the argument string <tt>sub</tt> via the
     * {@link Pattern} class specified by <tt>regex</tt>, the
     * regular expression string
     * @param sub the string ar
     * @param regex the regular expression
     * @return a decomposed substring
     * @throws java.util.regex.PatternSyntaxException illegal pattern
     */
    private String setFields (String sub, String regex) throws PatternSyntaxException {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sub);
        return m.find()?m.group():null;
    }
    /**
     * An enumeration of the expected date type
     */
    protected enum DataType {
        /**nucleic acid*/
        GENOMIC_DNA(false),
        M_RNA(true),
        RNA(false),
        /**protein*/
        PROTEIN(false);
        /**a flag indicating whether or not an DataType object is
         * translatable, i.e. an mRNA*/
        private boolean isTranslatable;
        private DataType(boolean isTranslatable){
            this.isTranslatable = isTranslatable;
        }
        public boolean isTranslatable (){
            return isTranslatable;
        }
    }
    public static void main (String[] args){
        Accession ac = new Accession("NM_111897.3");
        System.out.println(ac);
    }
}
