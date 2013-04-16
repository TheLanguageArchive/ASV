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
package nl.mpi.metadatabrowser.model.cmdi;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.*;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIDownloadNodeActionTest {

private TypedCorpusNode corpType = new TypedCorpusNode() {

        @Override
        public int getNodeId() {
            return 1;
        }

        @Override
        public String getName() {
            return "1";
        }

        @Override
        public URI getUri() {
            try {
                URI uri = new URI("http://lux16.mpi.nl/corpora/lams_demo/Corpusstructure/1.imdi");
                return uri;
            } catch (URISyntaxException ex) {
                Logger.getLogger(CMDIDownloadNodeActionTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public GenericTreeNode getChild(int index) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getChildCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getIndexOfChild(GenericTreeNode child) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public GenericTreeNode getParent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public NodeType getNodeType() {
            return new CMDIMetadata();
        }
    };
    
    public CMDIDownloadNodeActionTest() {
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
     * Test of getName method, of class CMDIDownloadNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIDownloadNodeAction instance = new CMDIDownloadNodeAction();
        String expResult = "download";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDIDownloadNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        DownloadActionRequest dar = new DownloadActionRequest();
        URI nodeUri = new URI("http://lux16.mpi.nl/corpora/lams_demo/Corpusstructure/1.imdi");
        String fileName = nodeUri.toString().substring(nodeUri.toString().lastIndexOf('/') + 1, nodeUri.toString().length());
        String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));


        File file = new File(nodeUri.getPath());
        IResourceStream resStream = new FileResourceStream(file);
        DownloadActionRequest.setStreamContent(resStream);
        DownloadActionRequest.setFileName(fileNameWithoutExtn);
        NodeActionResult expResult = new NodeActionResult() {

            @Override
            public String getFeedbackMessage() {
                return "no message";
            }

            @Override
            public ControllerActionRequest getControllerActionRequest() {
                return new DownloadActionRequest();
            }
        };
        CMDIDownloadNodeAction instance = new CMDIDownloadNodeAction();
        NodeActionResult result = instance.execute(corpType);
        assertEquals("download", instance.getName());
        assertEquals("1", dar.getFileName());
    }
}
