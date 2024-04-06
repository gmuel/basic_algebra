/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 * A FASTA file object
 * @author hendrik1
 */
public class Fasta {
    /**the FASTA file header object - containing all
       necessary information of the FASTA object*/
    private FastaHeader header;
    /**the sequence string*/
    private String seq;
    private int frame;
    /**
     * Constructs a <code>Fasta</code> object using
     * the two argument strings to set the fields
     * @param seq the sequence string
     * @param header the FASTA file header string
     * @throws util.FastaFormatException if the FASTA file
     * header string is violating the FASTA header format
     */
    public Fasta (String seq, String header) throws FastaFormatException {
        this.seq = seq;
        this.header = new FastaHeader (header);
        if(this.header.toString()==null)
            throw new FastaFormatException ("\nProvided FASTA header string argument"+
                    " violates FastaHeader format:\n"+header);
    }
    public Fasta (String seq, FastaHeader header){
        this.seq    = seq;
        this.header = header;
    }
    public Accession getAccession (){
        return header.getAccession();
    }
    /**
     * Returns the translation frame index
     * @return the frame index
     */
    public int getFrame (){
    	return frame;
    }
    /**
     * Returns the FASTA file header object
     * @return the header
     */
    public FastaHeader getHeader (){
        return header;
    }
    /**
     * Returns the sequence string
     * @return the sequence
     */
    public String getSequence (){
        return seq;
    }
    /**
     * Returns the length of the sequence string
     * @return the sequence length
     */
    public int getSequenceLength(){return seq.length();}
    public String getSubSequence (int start, int end){
        if(end>seq.length()) throw new IllegalArgumentException ("");
        if(start<0||start>=end) throw new IllegalArgumentException ("");
        return seq.substring(start,end);
    }
    /**
     * Sets the translation frame index. <b>Note</b>, that any
     * value less than zero or equal to or greater than 3 causes
     * an exception
     * @param frame the translation frame index (0, 1 or 2)
     */
    public void setFrame (int frame){
    	if(frame<0||frame>=3) throw new IllegalArgumentException ("Frame index exclusively 0, 1 or 2!\n"+frame);
    	this.frame = frame;
    }
    @Override
    public String toString (){
    	String lineTerm = "\n";
    	StringBuilder sb = new StringBuilder ();
    	sb.append(header.toString());
    	sb.append(lineTerm);
    	int length = seq.length();
    	int indHexMax = (int) Math.floor((double) length/60d);
    	for (int i = 0; i < indHexMax; i++){
    		int ind = 60*i;
    		sb.append(seq.substring(ind,ind+60));
    		sb.append(lineTerm);
    	}
    	return sb.toString();
    }
    /**
     * Returns a string representation of the mRNA to protein
     * sequence of this <code>Fasta</code> object, if and only if it
     * is translatable (i.e. an mRNA) otherwise returns null
     * @return the amino acid sequence
     */
    public String translate (){
    	if (!header.getAccession().isTranslatable()) return null;
    	int length = seq.length();
    	StringBuilder sb = new StringBuilder ();
    	for (int i = 0; i < length-3-frame; i+=3){
    		String sub = seq.substring(i+frame,i+3+frame);
    		AminoAcid aa = AminoAcid.getAminoFromCodon(sub);
    		sb.append(aa.getSingleLetter());
    	}
    	return sb.toString();
    }

}
