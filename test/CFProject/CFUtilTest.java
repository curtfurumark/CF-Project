/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.sql.ResultSet;
import java.util.Map;
import javafx.event.Event;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curtr
 */
public class CFUtilTest {
    
    public CFUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of composeWhereClause method, of class CFUtil.
     */
    @Test
    public void testComposeWhereClause() {
        System.out.println("composeWhereClause");
        CFStates states = null;
        String expResult = "";
        String result = CFUtil.composeWhereClause(states);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentDir method, of class CFUtil.
     */
    @Test
    public void testGetCurrentDir() {
        System.out.println("getCurrentDir");
        String expResult = "";
        String result = CFUtil.getCurrentDir();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of print method, of class CFUtil.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        ResultSet rs = null;
        CFUtil.print(rs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stringArr2HashMap method, of class CFUtil.
     */
    @Test
    public void testStringArr2HashMap() {
        System.out.println("stringArr2HashMap");
        String[] arr = null;
        String delim = "";
        Map<String, String> expResult = null;
        Map<String, String> result = CFUtil.stringArr2HashMap(arr, delim);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of debug method, of class CFUtil.
     */
    @Test
    public void testDebug() {
        System.out.println("debug");
        Event event = null;
        CFUtil.debug(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
