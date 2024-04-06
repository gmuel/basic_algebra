/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author hendrik1
 */
public class Frame {
    /**the coding frame index*/
    private int frameIndex;
    /**the number of 'amber' stop codons, i.e. 'UAG'*/
    private int amberCount;
    /**the number of 'amber' stop codons, i.e. 'UAA'*/
    private int ochreCount;
    /**the number of 'amber' stop codons, i.e. 'UGA'*/
    private int  opalCount;
    /**a shallow copy of the coding region object*/
    private CodingRegion coding;
    /**
     * Constructs a frame object, with <tt>cr</tt> as the
     * coding region object and <tt>frameIndex</tt> as the
     * specifying index.
     * @param cr the coding region object
     * @param frameIndex the frame index
     */
    public Frame (CodingRegion cr, int frameIndex){
        this.frameIndex = frameIndex;
        coding = cr;
    }
    /**
     * Counts all stop codons in this frame.
     */
    public void countStopCodons (){
        int iniFrame = coding.getFrameIndex();
        coding.getFASTAmRNA().setFrame(frameIndex);
        int start = coding.getStartRNA()+frameIndex;
        int end   = coding.getEndRNA()+frameIndex;
        int maxEnd= coding.getFASTAProtein().getSequenceLength();
        for (int i = start; i < end; i += 3){
            if(i>=maxEnd) break;
            AminoAcid aa = coding.getCodon(i);
            if(aa.equals(AminoAcid.B)) amberCount++;
            else if(aa.equals(AminoAcid.J)) ochreCount++;
            else if(aa.equals(AminoAcid.O)) opalCount++;
        }
        coding.getFASTAmRNA().setFrame(iniFrame);
    }
    /**
     * Returns the number of amber stop codons in this
     * frame, i.e. the number of 'UAG' triplets.
     * @return the number of amber codons
     */
    public int getAmberCount (){
        return amberCount;
    }
    /**
     * Returns the number of ochre stop codons in this
     * frame, i.e. the number of 'UAA' triplets.
     * @return the number of ochre codons
     */
    public int getOchreCount (){
        return ochreCount;
    }
    /**
     * Returns the number of opal stop codons in this
     * frame, i.e. the number of 'UGA' triplets.
     * @return the number of opal codons
     */
    public int getOpalCount (){
        return opalCount;
    }
}
