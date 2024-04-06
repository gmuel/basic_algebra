/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.io.*;
import java.util.zip.*;
/**
 * A class to read GZIP files, i.e. all files with extension
 * 'gz', or 'gzip'.
 * @author hendrik1
 */
public class GZipReader01 {
    /**the GZIP file object*/
    private File zipFile;
    /**the reader object*/
    private BufferedReader reader;
    /**
     * Constructs a GZIP reader object, where the
     * argument <tt>zipFile</tt> is the absolute path
     * string of the GZIp file to read. <b>Note</b>, that
     * this constructor initializes a reader object thus
     * calling the {@link close() } method is crucial.
     * @param zipFile the absolute path of the file to read
     * @throws java.io.IOException an i/o error occurred
     */
    public GZipReader01 (String zipFile) throws IOException {
        this.zipFile = new File (zipFile);
        open();
    }
    /**
     * Constructs a GZIP reader object, where the
     * argument <tt>zipFile</tt> is a {@link File} instance 
     * representing the GZIp file to read. <b>Note</b>, that
     * this constructor initializes a reader object thus
     * calling the {@link close() } method is crucial.
     * @param zipFile the absolute path of the file to read
     * @throws java.io.IOException an i/o error occurred
     */
    public GZipReader01 (File zipFile) throws IOException {
        this.zipFile = zipFile;
        open();
    }
    /**
     * Closes the reader object
     * @throws java.io.IOException an i/o error occurred
     */
    public void close () throws IOException {
        reader.close();
    }
    @Override
    protected void finalize () throws IOException {
        try{
            close();
        } finally {
            reader.close();
        }
    }
    /**
     * Returns the absolute path of the GZIP file object
     * @return the absolute path
     */
    public String getGZipAbsolutePath (){
        return zipFile.getAbsolutePath();
    }
    /**
     * Returns the GZIP file name
     * @return the file name
     */
    public String getGZipName (){
        return zipFile.getName();
    }
    /**
     * Returns true if and only if the end of file
     * has been reached, i.e. <code>reader.readLine()==null</code>
     * returns true
     * @return true if EOF reached
     * @throws java.io.IOException an i/o error occurred
     */
    public boolean isEndOfFile () throws IOException {
        return reader.readLine()==null?true:false;
    }
    /**
     * Opens the the reader object
     * @throws java.io.IOException an i/o error occurred
     */
    public void open () throws IOException {
        reader = new BufferedReader (new InputStreamReader (new GZIPInputStream(
                    new FileInputStream(zipFile))));
    }
    /**
     * Returns the a string representation of each
     * line of the file to be read
     * @return the line string
     * @throws java.io.IOException
     */
    public String readLine () throws IOException {
        String s;
        return (s = reader.readLine())==null?null:s;
    }
}
