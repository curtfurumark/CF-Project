/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.time.LocalDate;
import persist.FurumarkPersist;

/**
 *
 * @author curtr
 */
public class FurumarkPersistTest {
    private FurumarkPersist persist = new FurumarkPersist();
    public static void main(String[] args){
        new FurumarkPersistTest().testGetProjects();
    
    }
    
    public void testAddProject(){
        System.out.println("FurumarkPersist.testAddProject()");
        persist.addProject(LocalDate.now(), "testing", "not done", "just do it");
    }
    public void testUpdateProject(){
        System.out.println("FurumarkPersist.testUpdateProject()");
    }
    public void testGetProjects(){
        System.out.println("testGetProjects()");
        persist.getProjects("", null);
    }
    
}
