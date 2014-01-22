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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.adapter.AdapterUtils;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that call redirect to RRS
 */
@Component
public class CMDIRrsNodeAction implements NodeAction {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final static Logger logger = LoggerFactory.getLogger(CMDIRrsNodeAction.class);

    @Autowired
    public CMDIRrsNodeAction(NodeActionsConfiguration nodeActionsConfiguration) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
    }

    @Override
    public String getName() {
        return "Resource Access (RRS)";
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        URI targetURI = null;
        NavigationActionRequest request = null;
        UriBuilder uriBuilder = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
        for (TypedCorpusNode node : nodes) {
            //Buil redirect to RRS
            URI nodeId = node.getNodeURI();
            String nodeid = AdapterUtils.toNodeIdString(nodeId);
            targetURI = uriBuilder.queryParam("nodeid", nodeid).queryParam("jsessionID", "session_id").build();
        }
        try {
            request = new NavigationActionRequest(targetURI.toURL());
        } catch (MalformedURLException ex) {
            logger.error("URL syntax exception:" + ex);
        }
        return new SimpleNodeActionResult(request);
    }
}
