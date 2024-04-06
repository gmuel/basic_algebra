/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import util.Accession;
import util.Fasta;
import util.FastaHeader;
import util.GZipReader01;
import util.GZipWriter01;
import util.RNA2ProtDict;
/**
 * A class to extract information from a mRNA and protein FASTA
 * files, as well as dictionary and refGene file.
 * @author hendrik1
 */
public class Extracter {
    /**the absolute path of the input files*/
	private String path;
    /**the mRNA to protein accession id map*/
    private RNA2ProtDict dict;
    /**the dictionary info map, mapping the accession id
      onto the info object*/
    private HashMap<Accession,RefGeneInfo> dictInfo;
    /**the accession to FastaPair map*/
    private HashMap<Accession,FastaPair> fastaPairs;
    /**the dictionary file object*/
    private File dictFile;
    /**the reference file object*/
    private File refGene;
    /**the mRNA FASTA file object*/
    private File mRNAFile;
    /**the protein FASTA file object*/
    private File protFile;
    /**the log file*/
    private GZipWriter01 log;
    /**the first accession id to be ignored
     while reading*/
    private Accession firstIgn;
    /**a flag indicating whether or not the EOF (end of file)
     of the mRNA FASTA file has been reached while reading*/
    private boolean eof;
    /**the maximal number of entries*/
    private int maxSize;
    /*---------------static fields-------------*/
    /**a string flag of the log file indicating that
     the writing operation completed successfully*/
    static final String COMPL = "COMPLETED ENTRY:\n";
    /**a string flag of the log file indicating that
     the writing operation failed due to missing refGene info*/
    static final String MISS1 = "MISSING ENTRY: no refGene info\n";
    /**a string flag of the log file indicating that
     the writing operation failed due to a missing protein FASTA entry*/
    static final String MISS2 = "MISSING ENTRY: no protein FASTA found\n";
    /**
     * Constructs an extracter object setting the
     * input file objects according to the provided arguments
     * @param path the absolute directory path
     * @param mRNAFile the mRNA FASTA file
     * @param protFile the protein FASTA file
     * @param dictFile the dictionary file
     * @param refGene the refGene file
     */
    public Extracter (String path, String mRNAFile, String protFile,
            String dictFile, String refGene){
    	this(path,mRNAFile,protFile,dictFile,refGene,45);
    }
    /**
     * Constructs an extracter object setting the
     * input file objects according to the provided arguments
     * @param path the absolute path of the directory of all input files
     * @param mRNAFile the mRNA FASTA file
     * @param protFile the protein FASTA file
     * @param dictFile the dictionary file
     * @param refGene the reGene file
     * @param maxSize the maximal number of entries to look up at one round
     */
    public Extracter (String path, String mRNAFile, String protFile,
            String dictFile, String refGene, int maxSize){
        this.path = path;
        this.dictFile = new File (this.path+dictFile);
        this.refGene  = new File (this.path+refGene);
        this.mRNAFile = new File (this.path+mRNAFile);
        this.protFile = new File (this.path+protFile);
        dict       = new RNA2ProtDict ();
        dictInfo   = new HashMap<Accession,RefGeneInfo>();
        fastaPairs = new HashMap<Accession,FastaPair> ();
        this.maxSize = maxSize;
    }
    /**
     * Returns true if and only if <tt>fastaPairs.size()==maxSize-1</tt>
     * returns true, i.e. all but one entry has been placed in the FASTA
     * pair map during one round of reading
     * @return true, if the FASTA pair map is full
     */
    private boolean checkForMaxSize (){
        return fastaPairs.size()==maxSize-1?true:false;
    }
    /**
     * Returns a <code>FastaHeader</code> object if and only if
     * the header's accession id refers to either an mRNA or a
     * protein. Otherwise null is returned
     * @param s a string object
     * @return a <code>FastaHeader</code> object or null
     */
    private FastaHeader constructMRNAOrProtHeader (String s){
    	FastaHeader header = new FastaHeader(s);
    	Accession ac = header.getAccession();
    	return ac.isProtein()||ac.isTranslatable()?header:null;
    }
    /**
     * Reads the provided input files and writes the results
     * to a GZIP file in the following format:
     * <p><li>Accession id mRNA, Accession id protein</li>
     * <li>Dictionary info</li>
     * <li>Fasta entry mRNA</li>
     * <li>Fasta entry protein</li>
     * </p>
     */
    public void readAndWrite (){
        GZipWriter01 writer = null;
    	try{
            //the log file initialized
            log    = new GZipWriter01 (path+"log01.log.gz");
    		writer = new GZipWriter01 (path+"output01.txt.gz");
    		while(!eof){
    			readFastaMRNA();
    			readDictionary ();
    			readFastaProt();
    			readRefGene();
    			for (Accession ac : fastaPairs.keySet()){
                    Accession pAc = dict.get(ac);
    				RefGeneInfo dicInf = dictInfo.get(ac);
                    String s = ac.toString()+"\t"+pAc.toString();
                    if(dicInf==null){
                        log.write (MISS1);
                        log.write (s);
                        log.write (GZipWriter01.LINE_TERMINATOR);
                        continue;
                    }
                    writer.write(s);
                    writer.write(GZipWriter01.LINE_TERMINATOR);
    				writer.write(dicInf.toString());
    				writer.write(GZipWriter01.LINE_TERMINATOR);
    				FastaPair pair = fastaPairs.get(ac);
    				writer.write(pair.toString());
    				writer.write(GZipWriter01.LINE_TERMINATOR);
                    log.write(COMPL);
                    log.write (s);
                    log.write (GZipWriter01.LINE_TERMINATOR);
    			}
    			dict.clear();
    			dictInfo.clear();
    			fastaPairs.clear();
    			writer.flush();
                log.flush();
    		}
    		writer.close();
            log.close();
    	} catch (IOException e){e.printStackTrace();
        } finally {
            try{
                writer.close();
                log.close();
            } catch (IOException e){e.printStackTrace();}
        }
    }

    void readFastaMRNA (){
    	try{
    		GZipReader01 reader = new GZipReader01 (mRNAFile);
    		FastaHeader header = null;
    		StringBuilder sb = new StringBuilder ();
    		String s;
    		while ((s = reader.readLine())!=null){
    			if(s.contains(">gi")){
    				if(checkForMaxSize()&&header!=null){
    					firstIgn = new FastaHeader(s).getAccession();
    					if(firstIgn.isTranslatable()){
    						setFastaEntry(sb,header,true);
    						break;
    					}
    					continue;
    				}
    				if(header!=null){
    					if(firstIgn==null){
    						setFastaEntry(sb,header,true);
    						sb.delete(0,sb.length());
    						if((header = constructMRNAOrProtHeader(s))==null) header = null;
    					} else {
    						if(!header.getAccession().equals(firstIgn)) continue;
    						firstIgn = null;
    					}
    				} else{
                        if((header = constructMRNAOrProtHeader(s))==null) continue;
                        if(firstIgn!=null&&!header.getAccession().equals(firstIgn)){
                            header = null;
                            continue;
                        } else firstIgn = null;
                    }
    			} else if(firstIgn==null&&header!=null) sb.append(s);
    		}
    		eof = reader.isEndOfFile();
    		reader.close();
    	} catch (IOException e){e.printStackTrace();}
    }

    void readFastaProt (){
        try{
            GZipReader01 reader = new GZipReader01 (protFile);
            FastaHeader header = null;
            StringBuilder sb = new StringBuilder ();
            int counter = 0;
            String s;
            while ((s = reader.readLine())!=null){
                if(counter==maxSize) break;
                if(s.contains(">gi")){
                    if(header!=null){
                        setFastaEntry(sb,header,false);
                        counter++;
                        sb.delete(0, sb.length());
                        if((header = constructMRNAOrProtHeader(s))==null) continue;
                        if(!dict.containsValue(header.getAccession())){
                            header = null;
                            continue;
                        }
                    } else {
                        if((header = constructMRNAOrProtHeader (s))==null) continue;
                        if(!dict.containsValue(header.getAccession())){
                            header = null;
                            continue;
                        }
                    }
                } else if(header!=null) sb.append(s);
            }
            reader.close();
            removeUnpaired();
        } catch (IOException e){e.printStackTrace();}
    }
    /**
     * A method to extract the information stored
     * in a file of the same format as the
     * <code>refLink.txt.gz</code> file
     */
    void readDictionary (){
    	try{
    		GZipReader01 reader = new GZipReader01 (dictFile);
    		String s;
    		while ((s = reader.readLine())!=null){
    			if(dict.valueSize()==maxSize) break;
    			String[] args = s.split("\t");
    			if(args.length>=4){
    				 if(args[2].length()==0||args[3].length()==0) continue;
                     Accession mRNA = new Accession (args[2]);
                     if(!fastaPairs.containsKey(mRNA)) continue;
                     Accession prot = new Accession (args[3]);
                     dict.addKey(mRNA);
                     dict.addValue(mRNA, prot);
    			}
    		}
    		reader.close();
    	} catch (IOException e){e.printStackTrace();}
    }
    /**
     * Reads the refGene file to extract info of chromosomal
     * position transcription start and end, as well as translation
     * start and end
     */
    void readRefGene (){
    	try{
    		GZipReader01 reader = new GZipReader01 (refGene);
    		String s;
    		while ((s = reader.readLine())!=null){
    			if(dictInfo.size()==maxSize) break;
    			String[] args = s.split("\t");
                Accession ac;
                if(!(ac= new Accession (args[1])).isTranslatable()) continue;
                if(!dict.containsKey(ac)) continue;
                try{
                    RefGeneInfo di = new RefGeneInfo (s);
                    dictInfo.put(ac,di);
                } catch (Exception e){
                    e.printStackTrace();
                    System.err.println(s);
                }
    		}
    		reader.close();
    	} catch (IOException e){e.printStackTrace();}
    }
    /**
     * Removes all entries of the FASTA pair map, such that
     * <tt>entry.getValue()==null</tt> returns false for all remaining
     * entries, i.e. each entry has a protein FASTA
     */
    private void removeUnpaired (){
    	Set<Map.Entry<Accession, FastaPair>> entrieSet = fastaPairs.entrySet();
    	Iterator<Map.Entry<Accession,FastaPair>> it = entrieSet.iterator();
    	while(it.hasNext()){
    		Map.Entry<Accession, FastaPair> entry = it.next();
    		FastaPair pair = entry.getValue();
    		if(pair.getProt()==null){
                try{
                    log.write(MISS2);
                    log.write(pair.getMRNA().getAccession().toString());
                    log.write(GZipWriter01.LINE_TERMINATOR);
                } catch (IOException e){e.printStackTrace();}
                it.remove();
            }
    	}
    }
    /**
     * Auxiliary setter: adds a {@link FastaPair} object if and
     * only if:
     * <ol><li>the argument <tt>header</tt> represents an mRNA and
     * the argument flag <tt>isMRNA</tt> is true, or</li>
     * <li><tt>header</tt> represents a protein whose accession id is present
     * the dictionary and the argument flag is false<tt></tt></li></ol>
     * <b>Note</b>, that the <tt>sb</tt> argument must never be null.
     * @param sb a <code>StringBuilder</code> containing the sequence
     * @param header a <code>FastaHeader</code> object - must represent a protein
     * or an mRNA
     * @param isMRNA a flag
     * @return
     */
    private boolean setFastaEntry (StringBuilder sb, FastaHeader header, boolean isMRNA){
    	Fasta fasta = new Fasta (sb.toString(),header);
    	Accession ac1 = fasta.getAccession();
    	if(!ac1.isProtein()&&!ac1.isTranslatable()) return false;
    	FastaPair pair;
    	if(isMRNA){
    		pair = new FastaPair();
    		pair.setMRNA(fasta);
    		fastaPairs.put(header.getAccession(),pair);
    		return true;
    	}
    	Accession ac = dict.getKey(ac1);
    	if(ac==null) return false;
    	pair = fastaPairs.get(ac);
    	pair.setProt(fasta);
    	return true;
    }
    /**
     * A class representing information provided by
     * a RefGene Info file
     */
    private class RefGeneInfo {
        /**the chromosome*/
        private String chromo;
        /**a flag indicating whether or not the base identical
         * strand is in the same (plus==true) or inverse
         * (minus==false) orientation*/
        private boolean strand;
        /**the chromosomal transcription start position*/
        private int  tCStart;
        /**the chromosomal transcription end position*/
        private int    tCEnd;
        /**the chromosomal translation start position*/
        private int tLStart;
        /**the chromosomal translation end position*/
        private int tLEnd;
        /**the gene string identifier*/
        private String gene;
        /**
         * Constructs an object decomposing the argument
         * string <tt>s</tt>, if and only if it conforms
         * with standard RefGene info file
         * @param s the argument string
         */
        RefGeneInfo (String s){
            readInputString(s);
        }
        /**
         * Initial setter: decomposes the argument string
         * according to the RefGene file format
         * @param s the argument string
         */
        void readInputString (String s){
            String[] args = s.split("\t");
            if(args.length>=13){
                gene = args[12];
                chromo = args[2];
                if(args[3].matches("\\+")) strand = true;
                tCStart = Integer.parseInt(args[4]);
                tCEnd   = Integer.parseInt(args[5]);
                tLStart = Integer.parseInt(args[6]);
                tLEnd   = Integer.parseInt(args[7]);
            }
        }
        /**
         * Returns a string representation of this
         * <code>RefGeneInfo</code> object
         * @return a string
         */
        @Override
        public String toString (){
            String tab = "\t";
            StringBuilder sb = new StringBuilder ();
            sb.append(gene);
            sb.append(tab);
            sb.append(chromo);
            sb.append(tab);
            if(strand) sb.append("+");
            else sb.append("-");
            sb.append(tab);
            sb.append(tCStart+tab);
            sb.append(tCEnd+tab);
            sb.append(tLStart+tab);
            sb.append(tLEnd+"\n");
            return sb.toString();
        }
    }
    private class FastaPair {
        private Fasta mRNA;
        private Fasta prot;
        FastaPair (){}
        Fasta getProt (){return prot;}
        Fasta getMRNA (){return mRNA;}
        void setMRNA (Fasta mRNA){
            if(mRNA.getAccession().isTranslatable())
                this.mRNA = mRNA;
        }
        void setProt (Fasta prot){
            if(prot.getAccession().isProtein())
                this.prot = prot;
        }
        @Override
        public String toString(){
        	if(mRNA==null) return "";
        	if(prot==null) return mRNA.toString();
            return mRNA.toString()+prot.toString();
        }
    }
    public static void main (String[] args){
    	String p = "C:/Dokumente und Einstellungen/hendrik1/Eigene Dateien/"+
                "DNA - Kram/DATEN_Andrade/";
    	Extracter ex = new Extracter (p,"human.rna.fna.gz","human.protein.faa.gz",
                "refLink.txt.gz","refGene.txt.gz",45);
    	ex.readAndWrite();
    }
}
