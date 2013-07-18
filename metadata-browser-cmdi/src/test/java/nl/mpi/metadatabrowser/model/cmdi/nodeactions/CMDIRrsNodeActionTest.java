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
import java.util.ArrayList;
import java.util.Collection;
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
import org.junit.*;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIRrsNodeActionTest {

    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");

    public CMDIRrsNodeActionTest() {
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
     * Test of getName method, of class CMDIRrsNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIRrsNodeAction instance = new CMDIRrsNodeAction();
        String expResult = "rrs";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDIRrsNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();
        nodes.add(node);

        Map<String, URI> map = new HashMap<String, URI>();

        map.put("nodeId", NODE_ID);

        context.checking(new Expectations() {

            {
                oneOf(node).getUri();
                will(returnValue(new URI("nodeUri")));
                allowing(node).getNodeId();
                will(returnValue(NODE_ID));
            }
        });



        CMDIRrsNodeAction instance = new CMDIRrsNodeAction();
        NodeActionResult result = instance.execute(nodes);
        assertEquals("rrs", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));

        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
        assertEquals(NavigationRequest.NavigationTarget.RRS, navigationActionRequest.getTarget());
        assertNotNull(navigationActionRequest.getParameters());
        assertEquals(map, navigationActionRequest.getParameters());
        assertEquals("rrs", instance.getName());
    }
}
