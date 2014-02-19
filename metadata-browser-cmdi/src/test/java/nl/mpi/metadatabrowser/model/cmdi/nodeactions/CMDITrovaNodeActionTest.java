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
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.adapter.AdapterUtils;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDITrovaNodeActionTest {

    private NodeActionsConfiguration nodeActionsConfiguration = new NodeActionsConfiguration();
    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");
    private final static URI NODE_ID2 = URI.create("node:2");

    public CMDITrovaNodeActionTest() {
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
     * Test of getName method, of class CMDITrovaNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDITrovaNodeAction instance = new CMDITrovaNodeAction(nodeActionsConfiguration);
        String expResult = "Content Search";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDITrovaNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final TypedCorpusNode node2 = context.mock(TypedCorpusNode.class, "child1");
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();
        nodes.add(node);
        nodes.add(node2);
        String id = AdapterUtils.toNodeIdString(NODE_ID);
        String id2 = AdapterUtils.toNodeIdString(NODE_ID2);
        nodeActionsConfiguration.setTrovaURL("http://lux16.mpi.nl/ds/trova/search.jsp");
        UriBuilder targetURL = UriBuilder.fromUri(nodeActionsConfiguration.getTrovaURL());
        URI targetURI = targetURL.queryParam("nodeid", id).queryParam("nodeid", id2).queryParam("jsessionID", new URI("session_number")).build();

        context.checking(new Expectations() {
            {
                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));
                allowing(node2).getNodeURI();
                will(returnValue(NODE_ID2));
            }
        });


        CMDITrovaNodeAction instance = new CMDITrovaNodeAction(nodeActionsConfiguration);
        NodeActionResult result = instance.execute(nodes);
        assertEquals("Content Search", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));

        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
        assertNotNull(navigationActionRequest.getTargetURL());
        assertEquals(targetURI.toString(), navigationActionRequest.getTargetURL().toString());


    }
}
