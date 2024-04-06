/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.io.File;
import java.io.IOException;

import java.util.HashSet;

import util.Accession;
import util.GZipReader01;
/**
 *
 * @author hendrik1
 */
public class HeaderExtracter {
    private static final String HEADER_ID = ">gi";
    private File     rnaFile;
    private File refGeneFile;
    private int        maxSize;
    
    private int       tolCount;
    private int      mRNACount;
    private int   acFoundCount;
    private int mRNAFoundCount;
    private Accession nextEntry;
    private HashSet<Accession> helperMap;
    private boolean eof;

    public HeaderExtracter (String path, String rnaFile, String refGeneFile){
        this.refGeneFile = new File (path+refGeneFile);
        this.rnaFile     = new File (path+rnaFile);
        helperMap        = new HashSet<Accession> (1024);
        maxSize = 676;
    }
    public void read (){
        HashSet<Accession> surv = new HashSet<Accession> ();
        while (!eof){
            readRNAFile ();
            readRefGeneFile ();
            surv.addAll(helperMap);
            helperMap.clear();
        }
    }
    public void readRNAFile (){
        try{
            GZipReader01 reader1 = new GZipReader01 (rnaFile);
            String s1;
            while ((s1 = reader1.readLine())!=null){
                if(s1.contains(HEADER_ID)){
                    if(helperMap.size()==maxSize){
                        nextEntry = new Accession (s1);
                        break;
                    }
                    Accession ac1 = new Accession (s1);
                    if(nextEntry!=null&&!nextEntry.equals(ac1)) continue;
                    nextEntry = null;
                    tolCount++;
                    if(ac1.isTranslatable()) mRNACount++;
                    helperMap.add(ac1);
                }
            }
            eof = reader1.isEndOfFile();
            reader1.close();
        } catch (IOException e){e.printStackTrace();}
    }
    public void readRefGeneFile (){
        try{
            GZipReader01 reader = new GZipReader01 (refGeneFile);
            String s;
            while ((s = reader.readLine())!=null){
                String[] args = s.split("\t");
                Accession ac = new Accession (args[1]);
                if(!helperMap.contains(ac)) continue;
                if(ac.isTranslatable()) mRNAFoundCount++;
                
                acFoundCount++;
            }
            reader.close();
        } catch (IOException e){e.printStackTrace();}
    }
    @Override
    public String toString (){
        StringBuilder sb = new StringBuilder ();
        String nL  = "\n";
        sb.append("mRNA FASTA input:");
        sb.append(rnaFile.getAbsolutePath());
        sb.append(nL);
        sb.append("refGene input:");
        sb.append(refGeneFile.getAbsolutePath());
        sb.append(nL);
        sb.append("# total: "+tolCount);
        sb.append(nL);
        sb.append("# mRNA: "+mRNACount);
        sb.append(nL);
        sb.append("# ref entries: "+acFoundCount);
        sb.append(nL);
        sb.append("# ref entries mRNA: "+mRNAFoundCount);
        return sb.toString();
    }
    public static void main (String[] args){
        String path = "C:/Dokumente und Einstellungen/hendrik1/Eigene Dateien/"+
                "DNA - Kram/DATEN_Andrade/";
        HeaderExtracter he = new HeaderExtracter (path,"human.rna.fna.gz",
                "refGene.txt.gz");
        long start = System.currentTimeMillis();
        he.read();
        long end   = System.currentTimeMillis();
        System.out.println(he);
        System.out.println("exe time: "+(end-start)*.001+" s");
    }
}
