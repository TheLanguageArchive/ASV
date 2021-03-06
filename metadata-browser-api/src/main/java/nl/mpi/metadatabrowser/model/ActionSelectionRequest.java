/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
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
package nl.mpi.metadatabrowser.model;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ActionSelectionRequest implements ControllerActionRequest {

    private final List<NodeAction> nodeActions;
    private final NodeAction defaultAction;
    private final Collection<TypedCorpusNode> nodes;

    public ActionSelectionRequest(List<NodeAction> nodeActions, NodeAction defaultAction, Collection<TypedCorpusNode> nodes) {
        this.nodeActions = nodeActions;
        this.nodes = nodes;
        this.defaultAction = defaultAction;
    }

    /**
     *
     * @return list of actions to choose from
     */
    public List<NodeAction> getNodeActions() {
        return nodeActions;
    }

    /**
     *
     * @return actions to execute selected action on
     */
    public Collection<TypedCorpusNode> getNodes() {
        return nodes;
    }

    /**
     *
     * @return default action in case the user cannot be asked (preferably not
     * null)
     */
    public NodeAction getDefaultAction() {
        return defaultAction;
    }
}
