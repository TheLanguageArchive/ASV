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
package nl.mpi.metadatabrowser.services.cmdi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.*;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIActionsProvider implements NodeActionsProvider {
    
    private final List<NodeAction> resourcetxtNodeActionList = Arrays.<NodeAction>asList(
            new CMDITrovaNodeAction(),
            new CMDIAMSNodeAction(),
            new CMDIRrsNodeAction(),
            new CMDIStatsNodeAction(),
//            new CMDIAMSNodeAction("View Node"),
//            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDownloadNodeAction()
            );
    
    //private final List<NodeAction> resourcetxtNodeActionList;
    private final List<NodeAction> resourceAudioVideoNodeActionList = Arrays.<NodeAction>asList(
            new CMDIAMSNodeAction(),
            new CMDIRrsNodeAction(),
            new CMDIStatsNodeAction(),
//            new CMDIAMSNodeAction("View Node"),
//            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDownloadNodeAction()
            );
    
    private final List<NodeAction> metadataNodeActionList;
    
    public final List<NodeAction> collectionNodeActionList = Arrays.<NodeAction>asList(
            new CMDISearchNodeAction(),
            new CMDITrovaNodeAction(),
            new CMDIAMSNodeAction(),
            new CMDIRrsNodeAction(),
//            new CMDIAMSNodeAction("View Node"),
//            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDownloadNodeAction()
            );
    // private final List<NodeAction> extraNodeActionList;

    public CMDIActionsProvider(CmdiCorpusStructureDB csdb, ZipService zipService) {
        metadataNodeActionList  = Arrays.<NodeAction>asList(
            new CMDISearchNodeAction(),
            new CMDITrovaNodeAction(),
            new CMDIAMSNodeAction(),
            new CMDIRrsNodeAction(),
            new CMDIStatsNodeAction(),
//            new CMDIAMSNodeAction("View Node"),
//            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDownloadNodeAction(),
            new CMDIMultipleDownloadNodeAction(csdb, zipService)
//            new CMDIDownloadNodeAction("Version info"),
//            new CMDIAMSNodeAction("View Images")
            );
    }

    @Override
    public List<NodeAction> getNodeActions(Collection<TypedCorpusNode> nodes) {
        if(nodes.size() > 0 && nodes.size() ==1){
                if (nodes.iterator().next().getNodeType() instanceof CMDICollectionType) {
            return collectionNodeActionList;
        }
        if (nodes.iterator().next().getNodeType() instanceof CMDIMetadata) {
            return metadataNodeActionList;
        }
        if (nodes.iterator().next().getNodeType() instanceof CMDIResourceType) {
            return resourceAudioVideoNodeActionList;
        }
        if (nodes.iterator().next().getNodeType() instanceof CMDIResourceTxtType) {
            return resourcetxtNodeActionList;
        }
        } else if (nodes.size() > 1){
        // return a list for multiple selection
    }
        return null;
    }
}
