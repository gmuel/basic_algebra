/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.io.*;
import java.util.TreeSet;

import util.*;
/**
 *
 * @author hendrik1
 */
public class CodonTest {
    public static final String PATH = "C:/Dokumente und Einstellungen/"+
            "hendrik1/Eigene Dateien/DNA - Kram/DATEN_Andrade/";
    public static final File NUCI = new File (PATH+"NM_011897_3.txt");
    public static final File PROT = new File (PATH+"NM_011897_3transl.txt");
    public static String readFile (File input){
        StringBuffer strb = new StringBuffer ();
        try(BufferedReader reader = new BufferedReader (new FileReader(input))){
            
            String line;
            while ((line = reader.readLine())!=null){
                if(line.contains(">gi")) continue;
                strb.append(line);
            }
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
        return strb.toString();
    }
    public static TreeSet<Integer> findFrame (String str){
        TreeSet<Integer> frameSet = new TreeSet<Integer> ();
        int first = str.indexOf("ATG");
        int length = str.length();
        if(first>=0){
            frameSet.add(first);
            while (first<length&&first>=0){
                first = str.indexOf("ATG", frameSet.last()+1);
                if(first<0) break;
                frameSet.add(first);
            }
        }
        return frameSet;
    }
    public static void main (String[] args){
        String nucString = readFile(NUCI);
        String proString = readFile(PROT);
        int length = nucString.length();
        TreeSet<Integer> frames = findFrame(nucString);
        String bestMatch = null;
        int bestFrame = 0;
        for (Integer index:frames){
            StringBuilder strb = new StringBuilder ();
            for (int i = index; i < length-2; i += 3){
                String sub = nucString.substring(i,i+3);
                CodonSet co = CodonSet.getCodon(new Codon(sub));
                if(co!=null&&!co.isStopCodon())
                    strb.append(co.getAminoAcid());
                else if(co.isStopCodon()) break;
            }
            System.out.println("nuci: "+strb.toString());
            System.out.println("prot: "+proString);
            if(proString.matches(strb.toString())){
                bestMatch = strb.toString();
                bestFrame = index;
            }
        }
        System.out.println("\n"+bestMatch+"\nframe: "+bestFrame+"\nfirst AUG:"+
                frames.first());
        System.out.println(frames);
    }
}
