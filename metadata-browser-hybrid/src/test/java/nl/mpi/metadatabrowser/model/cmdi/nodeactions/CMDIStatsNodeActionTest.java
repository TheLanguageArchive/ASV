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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIStatsNodeActionTest {

    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");

    public CMDIStatsNodeActionTest() {
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
     * Test of getName method, of class CMDIStatsNodeAction.
     */
    @Test
    public void testGetName() {
	System.out.println("getName");
	CMDIStatsNodeAction instance = new CMDIStatsNodeAction();
	String expResult = "stats";
	String result = instance.getName();
	assertThat(result, is(not(expResult)));
	assertThat(result, is("accessStats"));
    }

    /**
     * Test of execute method, of class CMDIStatsNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
	System.out.println("execute");
	final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");

	context.checking(new Expectations() {
	    {
		allowing(node).getNodeURI();
		will(returnValue(NODE_ID));
	    }
	});



	CMDIStatsNodeAction instance = new CMDIStatsNodeAction();
	NodeActionResult result = instance.execute(node);
	assertEquals("accessStats", instance.getName());

	ControllerActionRequest actionRequest = result.getControllerActionRequest();
	assertNotNull(actionRequest);
	assertThat(actionRequest, instanceOf(NavigationActionRequest.class));

	NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
	assertEquals(null, navigationActionRequest.getTargetURL());
	assertNull(navigationActionRequest.getTargetURL());
	assertEquals(null, navigationActionRequest.getTargetURL());
    }
}
