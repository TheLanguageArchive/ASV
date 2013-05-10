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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import java.io.Serializable;
import java.net.URI;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.services.NodeResolver;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TypedCorpusNodeResolver implements NodeResolver, Serializable {

    private final CmdiCorpusStructureDB csdb;

    public TypedCorpusNodeResolver(CmdiCorpusStructureDB csdb) {
	this.csdb = csdb;
    }

    @Override
    public URI getUri(CorpusNode node) {
	return csdb.getObjectURI(node.getNodeId());
    }
}
