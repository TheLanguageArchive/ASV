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
package nl.mpi.metadatabrowser.wicket.model;

import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Wrapper model that dynamically provides access to the nodes list in a wrapped {@link NodeActionsStructure} model
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeActionsListModel extends AbstractReadOnlyModel<List<NodeAction>> {

    private final IModel<NodeActionsStructure> model;

    /**
     *
     * @param model Node actions structure model to wrap
     */
    public NodeActionsListModel(IModel<NodeActionsStructure> model) {
	this.model = model;
    }

    @Override
    public List<NodeAction> getObject() {
	if (model.getObject() == null) {
	    return null;
	} else {
	    return model.getObject().getNodeActions();
	}
    }
}
