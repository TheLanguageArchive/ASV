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
package nl.mpi.metadatabrowser.services;

import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeType;

/**
 * Interface for a service that identifies the type of a given node
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface NodeTypeIdentifier {

    /**
     * Gets the node type for the specified node
     *
     * @param nodeUri URI of node to determine type for
     * @return an object representing a type of node
     */
    NodeType getNodeType(CorpusNode node);
}
