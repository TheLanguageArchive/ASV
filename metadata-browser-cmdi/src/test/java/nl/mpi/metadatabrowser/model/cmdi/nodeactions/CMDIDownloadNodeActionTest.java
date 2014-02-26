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
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.apache.wicket.util.resource.IResourceStream;
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
public class CMDIDownloadNodeActionTest {

    private Mockery context;
    private static URI NODE_ID;
    private AuthenticationHolder auth;

    public CMDIDownloadNodeActionTest() {
    }

    @Before
    public void setUp() {
        context = new JUnit4Mockery();
        NODE_ID = URI.create("node:1");
        auth = context.mock(AuthenticationHolder.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class CMDIDownloadNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final NodeResolver nodeResolver = context.mock(NodeResolver.class);
        final AccessInfoProvider aiProvider = context.mock(AccessInfoProvider.class);

        context.checking(new Expectations() {
            {
                oneOf(nodeResolver).getUrl(node);
                will(returnValue(new URL("http://my/nodeUri")));

                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));

                allowing(node).getName();
                will(returnValue("nodeName"));

                allowing(aiProvider).hasReadAccess(NODE_ID, "everybody");
                will(returnValue(true));

                allowing(auth).getPrincipalName();
                will(returnValue("anonymous"));
                allowing(auth).setPrincipalName(null);
            }
        });

        CMDIDownloadNodeAction instance = new CMDIDownloadNodeAction(nodeResolver, aiProvider);
        auth.setPrincipalName(null);// this is a test for no authenticated user
        instance.setAuthenticationHolder(auth);
        NodeActionResult result = instance.execute(node);
        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(DownloadActionRequest.class));

        assertEquals("Download", instance.getName());

        DownloadActionRequest downloadActionRequest = (DownloadActionRequest) actionRequest;
        assertEquals("nodeUri", downloadActionRequest.getFileName());
        IResourceStream downloadStream = downloadActionRequest.getDownloadStream();
        assertNotNull(downloadStream);
    }
}
