/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;
import java.util.*;
/**
 * An RNA 2 Protein accession number dictionary. The keys
 * are accession ids of mRNAs and the values are the accession
 * ids of proteins
 * @author hendrik1
 */
public class RNA2ProtDict implements Iterable<Accession> {
    /**an RNA accession number to protein accession number map*/
    private HashMap<Accession,Accession> rna2Prot;
    /**the number of non-null values associated to all keys*/
    private int valueSize;
    /**
     * Constructs an empty RNA 2 Protein dictionary
     */
    public RNA2ProtDict (){
        rna2Prot = new HashMap<Accession,Accession> ();
    }
    /**
     * Adds the argument <tt>key</tt> to this dictionary
     * @param key the key
     */
    public void addKey (Accession key){
        rna2Prot.put(key,null);
    }
    /**
     * Adds a collection of accession ids
     * to this dictionary
     * @param coll the collection
     */
    public void addKeys (Collection<Accession> coll){
        for (Accession id : coll)
            rna2Prot.put(id, null);
    }
    /**
     * Adds <tt>value</tt> to this dictionary and returns
     * true if and only if <tt>key</tt> is present and
     * its associated value is null
     * @param key the key to look up
     * @param value the associated value
     * @return true if <tt>value</tt> was added
     */
    public boolean addValue (Accession key,Accession value){
        //a collection of all entries
        Set<Map.Entry<Accession,Accession>> set = rna2Prot.entrySet();
        //iterator to run through all entries
        Iterator<Map.Entry<Accession,Accession>> it = set.iterator();
        //iterate until no more entries are returned
        while (it.hasNext()){
            //an entry object: a key-value pair
            Map.Entry<Accession,Accession> entry = it.next();
            //the mRNA accession id
            Accession key1 = entry.getKey();
            //test, if argument key and current key are the same object
            //as specified by Accession.equals()
            if(key1.equals(key)){
                Accession val1 = entry.getValue();
                //only if the associated value is null
                //add the argument value
                if(val1==null){
                    entry.setValue(value);
                    valueSize++;
                    //return true only in this case
                    return true;    
                }
                //key was present but another non-null value already
                //present
                return false;
            }
        }
        //the argument key was not present
        return false;
    }
    /**
     * Removes all key value pairs in this dictionary
     */
    public void clear (){
        rna2Prot.clear();
        valueSize = 0;
    }
    /**
     * Returns true, if and only if this dictionary contains
     * the <tt>key</tt>
     * @param key the key
     * @return true if key is contained
     */
    public boolean containsKey (Accession key){
        return rna2Prot.containsKey(key);
    }
    /**
     * Returns true, if and only if this dictionary contains
     * the <tt>value</tt>
     * @param value the value
     * @return true if value is contained
     */
    public boolean containsValue (Accession value){
        return rna2Prot.containsValue(value);
    }

    /**
     * Returns a collection view of this dictionary
     * @return a set of all entries
     */
    public Set<Map.Entry<Accession,Accession>> entrySet (){
        return rna2Prot.entrySet();
    }
    /**
     * Returns the value associated to <tt>key</tt>, possibly
     * null
     * @param key the key
     * @return the value or null
     */
    public Accession get(Accession key){
        return rna2Prot.get(key);
    }
    /**
     * Returns the key associated to <tt>value</tt> if such
     * value exists or null otherwise. <b>Note</b>, the return
     * value is a shallow copy of the original value, any modification
     * will be reflected in this dictionary. Additionally, if the argument
     * <tt>value</tt> is null an <code>NullPointerException</code>
     * will be thrown.
     * @param value the value to look up
     * @return the key or null
     */
    public Accession getKey (Accession value){
        if(value==null) throw new NullPointerException ("Null argument not permitted!");
        Accession key = null;
        for (Map.Entry<Accession,Accession> entries:rna2Prot.entrySet()){
            if(entries.getValue().equals(value)){
                key = entries.getKey();
                break;
            }
        }
        return key;
    }
    /**
     * Returns an iterator over the key set
     * @return an iterator
     */
    public Iterator<Accession> iterator (){
        return rna2Prot.keySet().iterator();
    }
    /**
     * Removes <tt>key</tt> and its value from
     * this dictionary
     * @param key the key
     */
    public void remove (String key){
        if(rna2Prot.remove(key)!=null) valueSize--;
    }
    /**
     * Returns the size of this dictionary
     * @return the size
     */
    public int size (){
        return rna2Prot.size();
    }
    @Override
    public String toString (){return rna2Prot.toString();}
    /**
     * Returns the number of non-null values associated to
     * all key present in this dictionary
     * @return the value size
     */
    public int valueSize(){
        return valueSize;
    }

}
