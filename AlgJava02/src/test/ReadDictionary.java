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
public class ReadDictionary {
    private String path;
    private RNA2ProtDict dict;
    private HashMap<Accession,DictInfo> dictInfo;
    private HashMap<Accession,FastaPair> fastaPairs;
    private File dictFile;
    private File refGene;
    private File mRNAFile;
    private File protFile;
    private Accession firstIgn;
    private boolean eof;
    public ReadDictionary (String path, String dictFile, String refGene){
        this.path = path;
        this.dictFile = new File (this.path+dictFile);
        this.refGene  = new File (this.path+refGene);
        mRNAFile = new File (this.path+"human.rna.fna.gz");
        protFile = new File (this.path+"human.protein.faa.gz");
        dict = new RNA2ProtDict ();
        dictInfo = new HashMap<Accession,DictInfo>();
        fastaPairs = new HashMap<Accession,FastaPair> ();
    }
    public void read (){
        try{
            GZipWriter01 writer = new GZipWriter01 (path+"output.gz");
            while (!eof){
                readDictionary ();
                readRefGene();
                readFastas (true);
                readFastas (false);
                for (Accession ac : dictInfo.keySet()){
                    DictInfo info = dictInfo.get(ac);
                    writer.write(info.toString());
                    FastaPair pair = fastaPairs.get(ac);
                    writer.write(pair.toString());
                }
                dict.clear();
                dictInfo.clear();
                fastaPairs.clear();
            }
            writer.close();
        } catch (IOException e){e.printStackTrace();}

    }
    private void readDictionary (){
        try{
            GZipReader01 reader = new GZipReader01 (dictFile);
            String s;
            while ((s = reader.readLine())!=null){
                String[] ar = s.split("\t");
                if(dict.size()>9){
                    firstIgn = new Accession(ar[2]);
                    break;
                }
                if(firstIgn==null){
                    if(ar.length>=4){
                        if(ar[2].length()==0||ar[3].length()==0) continue;
                        Accession mRNA = new Accession (ar[2]);
                        Accession prot = new Accession (ar[3]);
                        dict.addKey(mRNA);
                        dict.addValue(mRNA, prot);
                    }
                } else {
                    if(ar.length>=4){
                        if(ar[2].length()==0||ar[3].length()==0) continue;
                        Accession mRNA = new Accession (ar[2]);
                        if(!firstIgn.equals(mRNA)) continue;
                        firstIgn = null;
                        Accession prot = new Accession (ar[3]);
                        dict.addKey(mRNA);
                        dict.addValue(mRNA,prot);
                    }
                }
            }
            eof = reader.isEndOfFile();
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void readRefGene (){
        try{
            GZipReader01 reader = new GZipReader01 (refGene);
            String s;
            while((s = reader.readLine())!=null){
                String[] args = s.split("\t");
                Accession ac;
                if(!(ac= new Accession (args[1])).isTranslatable()) continue;
                if(!dict.containsKey(ac)) continue;
                DictInfo di = new DictInfo (s);
                dictInfo.put(ac,di);
            }
            reader.close();
            Iterator<Accession> it = dict.iterator();
            while (it.hasNext()){
                Accession ac = it.next();
                if(!dictInfo.containsKey(ac)) it.remove();
            }
        } catch(IOException e){
            e.printStackTrace ();
        }
    }
    private void readFastas (boolean readMRNAFile){
        if(dict.size()==0) return;
        try{
            GZipReader01 reader;
            if(readMRNAFile) reader = new GZipReader01 (mRNAFile);
            else reader = new GZipReader01 (protFile);
            FastaHeader header = null;
            StringBuilder sb = new StringBuilder ();
            String s;
            while ((s = reader.readLine())!=null){
                if(s.contains(">gi")){
                    if(header!=null){
                        Fasta fasta = new Fasta (sb.toString(),header);
                        
                        if(readMRNAFile){
                            FastaPair pair = new FastaPair();
                            pair.setMRNA(fasta);
                            fastaPairs.put(fasta.getAccession(), pair);
                        } else {
                            Accession ac = dict.getKey(fasta.getAccession());
                            FastaPair pair = fastaPairs.get(ac);
                            pair.setProt(fasta);
                            fastaPairs.put(ac, pair);
                        }
                        sb.delete(0, sb.length());
                        header = new FastaHeader (s);
                    } else header = new FastaHeader (s);
                } else sb.append(s);
            }
            reader.close();
        } catch (IOException e){e.printStackTrace();}
    }
    private class DictInfo {
        private int chromo;
        private boolean strand;
        private int  tCStart;
        private int    tCEnd;
        private int tLStart;
        private int tLEnd;
        private String gene;
        DictInfo (String s){
            readInputString(s);
        }
        void readInputString (String s){
            String[] args = s.split("\t");
            if(args.length>=13){
                gene = args[12];
                chromo = Integer.parseInt(args[5].replace("chr", ""));
                if(args[3].matches("+")) strand = true;
                tCStart = Integer.parseInt(args[4]);
                tCEnd   = Integer.parseInt(args[5]);
                tLStart = Integer.parseInt(args[6]);
                tLEnd   = Integer.parseInt(args[7]);
            }
        }
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
            return mRNA.toString()+"\n"+prot.toString();
        }
    }
    public static void main (String[] args){
        String path = "C:/Dokumente und Einstellungen/hendrik1/Eigene Dateien/"+
                "DNA - Kram/DATEN_Andrade/";
        ReadDictionary dict = new ReadDictionary (path,"refLink.txt.gz","refGene.txt.gz");
        dict.read();
        System.out.println(dict.dictInfo.toString());
    }
}
