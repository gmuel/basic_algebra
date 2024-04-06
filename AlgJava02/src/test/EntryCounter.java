/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.io.File;
import java.io.IOException;

import util.Accession;
import util.FastaHeader;
import util.GZipReader01;

public class EntryCounter {
	private File input;
	private int tolCount;
	private int mRNACount;
	public EntryCounter (String input){
		this.input = new File (input);
	}
	public void readInput (){
		try{
			GZipReader01 reader = new GZipReader01 (input);
			String s;
			while ((s = reader.readLine())!=null){
				if(s.contains(">gi")){
					FastaHeader header = new FastaHeader (s);
					if(header.getAccession().isTranslatable()) mRNACount++;
					tolCount++;
				}
			}
			reader.close();
		} catch (IOException e){e.printStackTrace();}
	}
	public void readInput (boolean dummy){
		try{
			GZipReader01 reader = new GZipReader01 (input);
			String s;
			while ((s = reader.readLine())!=null){
				String[] args = s.split("\t");
				if(args.length>=4){
					if(args[2].length()!=0||args[3].length()!=0){
						Accession ac = new Accession (args[2]);
						if(ac.isTranslatable()) mRNACount++;
						tolCount++;
					}
				}
			}
			reader.close();
		} catch(IOException e){e.printStackTrace();}
	}
	public String toString (){
		StringBuilder sb = new StringBuilder ();
		sb.append("input file: "+input.getAbsolutePath());
		sb.append("\nmRNA count: "+mRNACount);
		sb.append("\ntol count: "+tolCount);
		return sb.toString();
	}
	public static void main  (String[] args){
		EntryCounter ec = new EntryCounter ("C:/Users/messier/Desktop/Gabi/Daten/refLink.txt.gz");
		ec.readInput(false);
		System.out.print(ec);
	}
}
