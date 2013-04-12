/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.metadatabrowser.services.cmdi;

import java.net.URI;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ProfileIdentifierTest {
    
    public ProfileIdentifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProfile method, of class ProfileIdentifier.
     */
    @Test
    public void testGetProfile() {
        System.out.println("getProfile");
        URI uri = null;
        ProfileIdentifier instance = new ProfileIdentifier();
        String expResult = "profile2";
        instance.setProfile(expResult);
        String result = instance.getProfile(uri);
        assertNotEquals(expResult, result);
    }

    /**
     * Test of setProfile method, of class ProfileIdentifier.
     */
    @Test
    public void testSetProfile() {
        System.out.println("setProfile");
        String expResult_2 = "";
        ProfileIdentifier instance = new ProfileIdentifier();
        instance.setProfile(expResult_2);
    }
}
