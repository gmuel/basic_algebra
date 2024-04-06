/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.io.File;
import java.io.IOException;

import util.GZipReader01;

public class LogFileExtracter {
	private File logFile;
	private int completed;
	private int missed1;
	private int missed2;
	private int tol;
	public LogFileExtracter (String logFile){
		this.logFile = new File (logFile);
		readLog();
	}
	private void readLog (){
		try{
			GZipReader01 reader = new GZipReader01 (logFile);
			int count = 0;
			String compl = Extracter.COMPL.split(":")[0];
			String miss1 = Extracter.MISS1.split(":")[0];
			String miss2 = Extracter.MISS2.split(":")[0];
			String s;
			while ((s = reader.readLine())!=null){
				if(count%2==0){
					if(s.contains(compl))
						completed++;
					else if (s.contains(miss1))
						missed1++;
					else if (s.contains(miss2))
						missed2++;
				}
				count++;
			}
			tol = completed+missed1+missed2;
			reader.close();
		} catch (IOException e){e.printStackTrace();}
	}
	public String toString (){
		String lineTerm = "\n";
		StringBuilder sb = new StringBuilder ();
		sb.append("Completed: "+completed);
		sb.append(lineTerm);
		sb.append("Missing refGene entry: "+missed1);
		sb.append(lineTerm);
		sb.append("Missing protein FASTA: "+missed2);
		sb.append(lineTerm);
		sb.append("total: "+tol);
		sb.append(lineTerm);
		return sb.toString();
	}
	public static void main (String[] args){
		LogFileExtracter ex = new LogFileExtracter ("C:/Users/messier/Desktop/Gabi/Daten/log01.log.gz");
		System.out.println(ex);
	}
}
