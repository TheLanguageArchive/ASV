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

import java.util.ArrayList;
import java.util.Collection;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDICitationNodeActionTest {

    private final Mockery context = new JUnit4Mockery();

    /**
     * Test of execute method, of class CMDICitationNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
	System.out.println("execute bookmark test");
	final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");

	Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();
	nodes.add(node);

	CMDICitationNodeAction instance = new CMDICitationNodeAction();
	NodeActionResult result = instance.execute(nodes);
	assertEquals("Citation", instance.getName());

	ControllerActionRequest actionRequest = result.getControllerActionRequest();
	assertNotNull(actionRequest);
	assertThat(actionRequest, instanceOf(ShowComponentRequest.class));
    }

    /**
     * Test of getName method, of class CMDICitationNodeAction.
     */
    @Test
    public void testGetName() {
	System.out.println("getName");
	CMDICitationNodeAction instance = new CMDICitationNodeAction();
	String expResult = "Citation";
	String result = instance.getName();
	assertEquals(expResult, result);
    }
}