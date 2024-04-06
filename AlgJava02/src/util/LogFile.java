/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.io.*;
/**
 * 
 * @author hendrik1
 */
public class LogFile {
    private File file;
    private BufferedWriter writer;
    public LogFile (String file) throws IOException {
        this.file = new File(file);
        writer = new BufferedWriter(new FileWriter(this.file));
    }
    public void append (String str) throws IOException {
        writer.append(str);
    }
    public void close () throws IOException {
        writer.close();
    }
    public void flush () throws IOException {
        writer.flush();
    }
    public void newLine () throws IOException {
        writer.newLine();
    }
}
