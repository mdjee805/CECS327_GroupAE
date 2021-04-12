/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

/**
 *
 * @author Michael
 */
public class NodeVariablesSingleton {
    private static NodeVariables nv;
    
    private NodeVariablesSingleton() {}
    
    public static NodeVariables getNodeVariablesSingleton()
    {
        if(nv ==  null)
        {
            nv = new NodeVariables();
        }
        return nv;
    }
}
