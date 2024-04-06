/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.io.*;
/**
 * A class providing functionality of the
 * FASTA file format, i.e. the header which by
 * convention is the first line of each FASTA file
 * or FASTA entry
 * @author hendrik1
 */
public class FastaHeader {
   private String headerStr;
   private Accession ac;
   private int gi;
   /**
    * Constructs a FASTA header object
    * @param headerStr the header string
    */
   public FastaHeader (String headerStr){
       this.headerStr = headerStr;
       setACs();
   }
   /**
    * Returns the accession string of this header
    * @return the accession
    */
   public String getAC (){
       return ac.toString();
   }
   public Accession getAccession (){
       return ac;
   }
   /**
    * Returns the accession number
    * @return the accession number
    */
   public int getACNumber (){
       return ac.getAccessionNumber();
   }
   /**
    * Returns the accession version number, that
    * is - each gene has its own accession number
    * but new versions only get a new version.
    * @return
    */
   public int getACVersion (){
       return ac.getAccessionVersion();
   }
   /**
    * Returns the accession specifier, i.e.
    * <ol><li>NM -> mRNA</li></ol>
    * @return
    */
   public String getACSpecifier (){
       return ac.getAccessionSpecifier();
   }
   /**
    * Returns a string flag indicating the data type of a
    * sequence:
    * <ol><li>"NUCLEIC" - > nucleic acid</li>
    * <li>"PROT" -> protein</li></ol>
    * @return
    */
   public String getDataType (){
       return ac.getDataType();
   }
   /**
    * Returns the gene identifier
    * @return the gene identifier
    */
   public int getGeneIdentifier (){
       return gi;
   }
   /**
    * Returns this FASTA header string if and only
    * if the passed argument string of the constructor
    * {@link FastaHeader(String)} is conform with
    * the FASTA format, otherwise null is returned
    * @return the header string
    */
   @Override
   public String toString (){
       return ac==null?null:headerStr;
   }
   /**
    * Sets the fields of this FASTA header object
    */
   private void setACs (){
       String[] sList = headerStr.split("\\|");
       int length = sList.length;
       if(length>=4){
           gi = Integer.parseInt(sList[1]);
           ac = new Accession (sList[3]);
       }
   }
   public static void main (String [] args){
       String path = "C:/Dokumente und Einstellungen/hendrik1/"+
               "Eigene Dateien/DNA - Kram/DATEN_Andrade/NM_011897_3.txt";
       try{
           BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
           String s = reader.readLine();
           reader.close();
           FastaHeader header = new FastaHeader (s);
           System.out.println(header);
       } catch (IOException e){
           e.printStackTrace();
       }
   }
}
