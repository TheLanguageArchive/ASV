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
package nl.mpi.metadatabrowser.services.impl.cmdi;

import nl.mpi.metadatabrowser.services.cmdi.impl.CMDINodeTypeIdentifier;
import java.net.URI;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import nl.mpi.metadatabrowser.model.cmdi.CorpusNodeType;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifierTest {

    private final Mockery context = new JUnit4Mockery();

    public CMDINodeTypeIdentifierTest() {
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
     * Test of getNodeType method, of class CMDINodeTypeIdentifier.
     */
    @Test
    public void testGetNodeType() throws Exception {
        System.out.println("getNodeType");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final CmdiCorpusStructureDB csdb = context.mock(CmdiCorpusStructureDB.class);

        context.checking(new Expectations() {

            {
                oneOf(node).getUri();
                will(returnValue(new URI("nodeUri")));
                oneOf(node).getNodeId();
                will(returnValue(1));
                allowing(csdb).getCorpusNodeType(1);
                will(returnValue(CorpusNodeType.RESOURCE_ANNOTATION));
            }
        });
        CMDINodeTypeIdentifier instance = new CMDINodeTypeIdentifier(csdb);
        NodeType expResult = new CMDIResourceTxtType();
        NodeType result = instance.getNodeType(node);
        assertThat(result, instanceOf(CMDIResourceTxtType.class));
        assertEquals(expResult.getName(), result.getName());
    }
}
