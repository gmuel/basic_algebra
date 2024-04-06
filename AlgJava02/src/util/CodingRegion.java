/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.util.regex.*;

/**
 * A coding region object: a pair of {@link Fasta} objects where
 * the first entry is the mRNA and the second is protein
 * @author hendrik1
 */
public class CodingRegion {
	/**the mRNA FASTA object*/
    private Fasta mRna;
    /**the protein FASTA object*/
    private Fasta prot;
    /**the starting index in the translated protein sequence*/
    private int startingIndex;
    /**the ending index in the translated protein sequence*/
    private int endingIndex;
    /**flag indicating the protein sequence has a matching
     * region in the translated mRNA*/
    private boolean hasMatch;
    /**
     * Constructs a coding region object, with
     * an mRNA FASTA object as first argument and
     * a protein FASTA as second argument
     * @param mRna an mRNA FASTA object
     * @param prot an protein FASTA object
     * @throws IllegalArgumentException if either <tt>mRna.getAccession().isTranslatable()</tt>
     * or <tt>prot.getAccession().isProtein()</tt> returns false or no translated frame contained
     * the protein sequence
     */
    public CodingRegion (Fasta mRna, Fasta prot) throws IllegalArgumentException {
    	setFields(mRna,prot);
    	setFrameInMRNA();
    }
    /**
     * Sets the coding region frame index
     * @throws IllegalArgumentException if the translated mRNA did not
     * contain the protein sequence
     */
    private void setFrameInMRNA () throws IllegalArgumentException {
    	String transl = null;
    	for (int i = 0; i < 3; i++){
    		mRna.setFrame(i);
    		transl = mRna.translate();
    		if(transl.contains(prot.getSequence())){
    			transl = null;
    			break;
    		}
    	}
    	if(transl!=null) throw new IllegalArgumentException ("\nThe translated mRNA did not contain the" +
    			" protein sequence!");
    }
    /**
     * Performs sanity checking of the arguments <tt>mRna</tt> and
     * <tt>prot</tt>. This method throws an exception if either:
     * <ol><li><code>mRNa.getAccession().isTranslatable()</code> returns true</li>
     * <li><code>prot.getAccession().isProtein()</code> returns true</li></ol>
     * @param mRna an FASTA mRNA object
     * @param prot an FASTA protein object
     * @throws IllegalArgumentException if one of the two arguments violates FASTA convention
     */
    private void setFields (Fasta mRna, Fasta prot) throws IllegalArgumentException {
    	boolean isMRNA = mRna.getAccession().isTranslatable();
    	boolean isProt = prot.getAccession().isProtein();
    	if(!isMRNA||!isProt){
    		if(!mRna.getAccession().isTranslatable())
    			throw new IllegalArgumentException ("\nData type" +
    				" of the first FASTA argument must be translatable!");
    		if(!isProt)
    			throw new IllegalArgumentException ("\nData type" +
				" of the second FASTA argument must be a protein!");
    	}
    	this.mRna = mRna;
    	this.prot = prot;
    }
    /**
     * Finds the starting and ending indices of
     * the translated gene product (mRNA to protein)
     */
    public void findProtSeq (){
    	String transl = mRna.translate();
    	Pattern p = Pattern.compile(prot.getSequence());
    	Matcher m = p.matcher(transl);
    	if(m.find()){
    		startingIndex = m.start();
    		endingIndex   = m.end();
            hasMatch      = true;
    	}
    }
    /**
     * Returns the frame index of this coding region
     * @return the frame index
     */
    public int getFrameIndex (){
        if(!hasMatch) throw new IllegalArgumentException ("");
        return mRna.getFrame();
    }
    /**
     * Returns the amino acid translated from the mRNA
     * FASTA object, if the index <tt>i</tt> is in translation range
     * and searching has successfully completed before calling this
     * method.
     * @param i the codon index within the mRNA sequence range
     * @return the amino acid
     * @throws java.lang.IllegalArgumentException not search performed or
     * no match found
     */
    public AminoAcid getCodon (int i) throws IllegalArgumentException {
        if(!hasMatch) throw new IllegalArgumentException("Either no match found or" +
                " searching not performed!");
        return AminoAcid.getAminoFromCodon(mRna.getSubSequence(i, i+3));
    }
    public Fasta getFASTAProtein (){
        return prot;
    }
    public Fasta getFASTAmRNA (){
        return mRna;
    }
    /**
     * Returns the starting position of this coding
     * region object: i.e. the index of the first
     * amino acid in the protein sequence
     * @return the starting index of the protein
     */
    public int getStartProt (){
    	return startingIndex;
    }
    /**
     * Returns the starting position of this coding
     * region object: i.e. the index of the first
     * nucleotide in the mRNA sequence to be translated
     * @return the starting index of the mRNA
     */
    public int getStartRNA (){
    	return 3*startingIndex+mRna.getFrame();
    }
    /**
     * Returns the ending position of this coding
     * region object: i.e. the index of the first
     * amino acid in the protein sequence (the stop codon)
     * @return the ending index of the protein
     */
    public int getEndProt (){
    	return startingIndex;
    }
    /**
     * Returns the ending position of this coding
     * region object: i.e. the index of the first
     * nucleotide in the mRNA sequence not to be translated
     * @return the ending index of the mRNA
     */
    public int getEndRNA (){
    	return 3*endingIndex+mRna.getFrame();
    }
    /**
     * Returns true if and only if the mRNA FASTA
     * object translated contained the protein sequence,
     * i.e. <code>mRna.translate().contains(prot.getSequence())</code>
     * returns true
     * @return true if at least one match was found
     */
    public boolean hasMatch (){
    	return hasMatch;
    }
    public static void main (String[] args){
    	Fasta mRNA = new Fasta ("ATCTGTATGATAGGATCCCACCTTGAAGATCTATCAGAATCTACGTGATGAAAA",">gi|12312321|NM_012345.1|Homo homus, new prot, mRNA");
    	String seq = mRNA.getSequence().substring(6,mRNA.getSequenceLength());
    	StringBuilder sb = new StringBuilder ();
    	int relLength = (int) Math.floor((double) seq.length()/3d);
    	for (int i = 0; i < relLength; i++){
    		String sub = seq.substring(3*i,3*i+3);
    		AminoAcid aa = AminoAcid.getAminoFromCodon(sub);
    		if(!aa.isStopAA())
    			sb.append(AminoAcid.getAminoFromCodon(sub).toString());
    		else break;
    	}
    	Fasta prot = new Fasta (sb.toString(),">gi|12312321|NP_012345.1|Homo homus, new prot, protein");

    }
}

