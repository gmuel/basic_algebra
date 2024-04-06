/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.io.*;
import java.util.*;
import util.*;
/**
 *
 * @author hendrik1
 */
public class TestRun {
    private static final String PATH = "C:/Dokumente und Einstellungen/hendrik1/"+
            "Eigene Dateien/DNA - Kram/DATEN_Andrade/";
    private File    rnaFile;
    private File   protFile;
    private File dictionary;
    private RNA2ProtDict dict;
    private transient Accession lastEntry;
    private HashMap<Accession,Fasta> rnaMap;
    private HashMap<Accession,Fasta> proMap;
    private boolean isEOF;
    private LogFile log;
    private StringBuilder errorStr;
    public TestRun (){
        rnaFile    = new File (PATH+"human.rna.fna.gz");
        protFile   = new File (PATH+"human.protein.faa.gz");
        dictionary = new File (PATH+"refLink.txt.gz");
        dict   = new RNA2ProtDict ();
        rnaMap = new HashMap<Accession,Fasta> ();
        proMap = new HashMap<Accession,Fasta> ();
    }
    public void computeTranslationFrame () throws IOException {
    	if(rnaMap.size()!=dict.valueSize()) return;
        for (Accession ac : rnaMap.keySet()){
            StringBuilder sb = new StringBuilder ();
            sb.append("AC - mRNA: "+ac.toString()+"\n");
            Accession pAc = dict.get(ac);
            sb.append("AC - prot: "+pAc.toString()+"\n");
            Fasta mRNA = rnaMap.get(ac);
            Fasta prot = proMap.get(pAc);
            CodingRegion cr = null;
            try{
                cr = new CodingRegion (mRNA,prot);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                errorStr.append("\nAC mRNA: "+ac);
                errorStr.append("\nAC prot: "+pAc+"\nno translated match found!");
                System.err.println(errorStr);
                continue;
            }
            cr.findProtSeq();
            sb.append("frame\tamber\tochre\topal\ttol\n");
            int tol = 0;
            for (int i = 1; i < 3; i++){
                Frame frame = new Frame (cr,i);
                frame.countStopCodons();
                int amb = frame.getAmberCount(), och = frame.getOchreCount();
                int opl = frame.getOpalCount();
                int frmtol = amb+och+opl;
                sb.append(i+"\t"+amb+"\t"+och+"\t"+opl+"\t"+frmtol+"\n");
                tol += frmtol;
            }
            sb.append("#total: "+tol);
            log.append(sb.toString());
            log.newLine();
            log.flush();
        }
    }
    public void readRNAFile (){
        try{
            GZipReader01 reader = new GZipReader01 (rnaFile);
            StringBuilder sb = new StringBuilder ();
            FastaHeader header = null;
            boolean skipper = false;
            String s;
            while ((s = reader.readLine())!=null){
                //running over the file linewise
                if(s.contains(">gi")){
                    //in case the line conforms to a FASTA file header

                    skipper = false;
                    if(lastEntry==null){
                        //in case the file has be opened before
                        if(header!=null){
                            //in case the header object initialized
                            //flush the current entries of 'header' and 'sb'
                            int length = sb.length();
                            rnaMap.put(header.getAccession(),
                                    new Fasta(sb.toString(),header));
                            sb.delete(0, length);
                            if(rnaMap.size()<50){
                                if((header = composeFastaHeader (s))==null){
                                    skipper = true;
                                    continue;
                                }
                            }
                            else{
                                if((header = composeFastaHeader(s))==null){
                                    skipper = true;
                                    continue;
                                }
                                lastEntry = header.getAccession();
                                break;
                            }
                        } else if((header = composeFastaHeader(s))==null){
                            skipper = true;
                            continue;
                        }
                    } else {
                        if((header = composeFastaHeader(s))==null){
                            skipper = true;
                            continue;
                        }
                        if(!lastEntry.equals(header.getAccession())){
                            skipper = true;
                            continue;
                        }
                        lastEntry = null;
                    }
                } else if(!skipper) sb.append(s);
            }
            setEOF(reader.isEndOfFile());
            reader.close();
            fillDictionary();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void readDictionary (){
        try{
            GZipReader01 reader = new GZipReader01 (dictionary);
            String s;
            while ((s = reader.readLine())!=null){
                String[] ar = s.split("\t");
                if(ar[3].matches("")) continue;
                Accession rnaKey = new Accession (ar[2]);
                if(!rnaMap.containsKey(rnaKey)) continue;
                if(dict.valueSize()>=rnaMap.size()) break;
                dict.addValue(rnaKey, new Accession(ar[3]));
            }
            reader.close();
            Iterator<Accession> it = dict.iterator();
            while(it.hasNext()){
                Accession ac = it.next();
                if(dict.get(ac)==null) it.remove();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void readProteinFile (){
    	try{
    		GZipReader01 reader = new GZipReader01 (protFile);
    		StringBuilder sb = new StringBuilder ();
    		FastaHeader header = null;
    		String s;
    		while ((s = reader.readLine())!=null){
    			if(s.contains(">gi")){
    				int length = sb.length();
    				if(header!=null&&length>0){
    					proMap.put(header.getAccession(), new Fasta(sb.toString(),header));
    					sb.delete(0, length);
    				}
    				header = new FastaHeader (s);
                    try{
                        if(!dict.containsValue(header.getAccession())){
                            header = null;
                            continue;
                        }
    				} catch (NullPointerException e){
                        e.printStackTrace();
                        System.err.println("\n"+s);
                        System.exit(1);
                    }

    			} else {
    				if(header!=null) sb.append(s);
    			}
    		}
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    }
    public void run (){
        try{
            errorStr = new StringBuilder ();
            log = new LogFile (PATH+"logFile.txt");
            int count = 0;
            while (!isEOF){
                readRNAFile();
                readDictionary();
                int num = dict.size();
                readProteinFile();
                computeTranslationFrame();
                rnaMap.clear();
                dict.clear();
                proMap.clear();
                count += num;
                System.out.println(num+" of "+count+" entries written to LOG file");
                System.out.println(lastEntry);
            }
            log.close();
        } catch (IOException e){
            throw new RuntimeException (e.getMessage());
        } catch (Exception e){
            try{
                log.append(errorStr.toString());
                log.flush();
                log.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
            System.exit(1);
        }
    }
    private void fillDictionary (){
        if(rnaMap.size()>0)
            dict.addKeys(rnaMap.keySet());
    }
    private FastaHeader composeFastaHeader (String s){
        FastaHeader header = new FastaHeader (s);
        return header.getAccession().isTranslatable()?header:null;
    }
    public static void main (String[] args){
        TestRun run = new TestRun();
        run.run();
    }
	public void setEOF(boolean isEOF) {
		this.isEOF = isEOF;
	}
	public boolean isEOF() {
		return isEOF;
	}
}
