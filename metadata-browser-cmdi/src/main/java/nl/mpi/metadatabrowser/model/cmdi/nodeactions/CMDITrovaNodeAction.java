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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDITrovaNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "trova";

    public CMDITrovaNodeAction() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        Map<String, URI> parameters = new HashMap<String, URI>();
        for (TypedCorpusNode node : nodes) {
            try {
                // HANDLE trova action here
                //TODO get session id
                parameters.put("nodeId", node.getNodeId());
                parameters.put("jessionID", new URI("session_number"));
            } catch (URISyntaxException ex) {
                logger.error("URI syntax exception: " + ex);
            }
        }
        final NavigationActionRequest request = new NavigationActionRequest(NavigationRequest.NavigationTarget.TROVA, parameters);

        return new SimpleNodeActionResult(request);
    }
}