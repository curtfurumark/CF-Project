/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.time.LocalDate;
import persist.CFDBPersist;

/**
 *
 * @author curtr
 */
public class TestCFDBPersist {
    CFDBPersist persist = new CFDBPersist();
    public static void main(String[] args){
        new TestCFDBPersist().testAddProject();
    
    }
    
    public void testAddProject(){
        System.out.println("testAddProject()");
        persist.setURL("http://curtfurumark");
        persist.addProject(LocalDate.now(), "testing", "not done", "just do it");
        
    }
    
}
