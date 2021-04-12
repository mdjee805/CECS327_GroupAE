/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 *
 * @author Michael
 */
public class NodeVariables {
    private Multimap<String, String> dht;
    private long dhtTime;
    
    public NodeVariables()
    {
        dht = ArrayListMultimap.create();
        dhtTime = 0;
    }
    
    public void setDht(Multimap<String, String> dht) { this.dht = dht; }
    public Multimap<String, String> getDht() { return dht; }
    public void setDhtTime(long dhtTime) { this.dhtTime = dhtTime; }
    public long getDhtTime() { return dhtTime; }
}

