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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import nl.mpi.archiving.corpusstructure.core.AccessLevel;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.springframework.beans.factory.annotation.Autowired;

;

/**
 * Icon provider for {@link CorpusNode}, primarily CMDI but with support for
 * {@link IMDICorpusType} and {@link IMDISessionType}. Icons are based on the
 * {@link NodeType} and the access level as reported by the
 * {@link CorpusStructureProvider}
 *
 * @param <T>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodeIconProvider<T extends CorpusNode> implements ArchiveTreeNodeIconProvider<T> {

    private final static ImageIcon sessionIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("session_color.gif"));
    private final static ImageIcon catalogueIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("catalogue.png"));
    private final static ImageIcon corpusIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("corpusnode_color.gif"));
    private final static ImageIcon fileIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("mediafile.gif"));
    private final static ImageIcon fileIconTxt = new ImageIcon(CMDINodeIconProvider.class.getResource("file.gif"));
    private final static ImageIcon cmdiIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("clarin.png"));
    private final static ImageIcon unknownIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("unknown.png"));
    private final static ImageIcon openIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_green.png"));
    private final static ImageIcon licensedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_yellow.png"));
    private final static ImageIcon restrictedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_orange.png"));
    private final static ImageIcon closedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_red.png"));
    private final static ImageIcon externalIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_black.png"));
    private final Map<Entry<ImageIcon, ImageIcon>, ResourceReference> iconMap = new HashMap<Entry<ImageIcon, ImageIcon>, ResourceReference>();
    private final NodeTypeIdentifier nodeTypeIdentifier;
    private final CorpusStructureProvider csProvider;
    private final AccessInfoProvider accessInfoProvider;

    /**
     * Constructor
     *
     * @param nodeTypeIdentifier
     * @param csProvider
     */
    @Autowired
    public CMDINodeIconProvider(NodeTypeIdentifier nodeTypeIdentifier, CorpusStructureProvider csProvider, AccessInfoProvider accessInfoProvider) {
        this.nodeTypeIdentifier = nodeTypeIdentifier;
        this.csProvider = csProvider;
        this.accessInfoProvider = accessInfoProvider;
        populateIconMap();
    }

    /**
     * Method that manage the type of resourceReference returned dpeending on
     * type of node and accesslevel
     *
     * @param contentNode, node for which type and accesslevel have to be grabed
     * @return ResourceReference
     */
    @Override
    public ResourceReference getNodeIcon(T contentNode) {
        ResourceReference combinedIcon = null;
        try {
            final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
            final ImageIcon nodeTypeIcon = getNodeTypeIcon(nodeType);
            combinedIcon = checkNodeAccess(contentNode, nodeTypeIcon);
        } catch (UnknownNodeException ex) {
            Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NodeTypeIdentifierException ex) {
            Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return combinedIcon;
    }

    /**
     * Method return an ImageIcon reflecting the access level for a particular
     * node. Access is requested to corpusStructureDB
     *
     * @param contentNode, node for which access level have to be requested
     * @param csdb , instance of corpusStructureDb
     * @return ImageIcon, corresponding to access level
     */
    private ResourceReference checkNodeAccess(T contentNode, ImageIcon typeNode) throws UnknownNodeException {
        final ImageIcon accessIcon = getNodeAccessIcon(accessInfoProvider.getAccessLevel(contentNode.getNodeURI()));

        // retrieve the corresponding combined icon based on nodetype and accesslevel
        final Map.Entry<ImageIcon, ImageIcon> iconTuple = new SimpleEntry<ImageIcon, ImageIcon>(typeNode, accessIcon);
        return iconMap.get(iconTuple);
    }

    private void populateIconMap() {
        final List<ImageIcon> nodeIcon = Arrays.asList(sessionIcon, corpusIcon, catalogueIcon, fileIcon, cmdiIcon, unknownIcon);
        final List<ImageIcon> accessIcon = Arrays.asList(openIcon, licensedIcon, restrictedIcon, closedIcon, externalIcon, unknownIcon);

        int i = 0;
        for (ImageIcon nodetypeIcon : nodeIcon) {
            for (ImageIcon accesslevelIcon : accessIcon) {
                final String name = String.format("node_icon_%d", i++);
                final Entry<ImageIcon, ImageIcon> iconsMap = new SimpleEntry<ImageIcon, ImageIcon>(nodetypeIcon, accesslevelIcon);
                iconMap.put(iconsMap, createCombinedIcon(nodetypeIcon, accesslevelIcon, name));
            }
        }
    }

    /**
     * Method that create a combinedIcon reflecting the type of Node and the
     * access level.
     *
     * @param ImageIcon,typeIcon : icon that give the type of node
     * @param ImageIcon, accesslevel : icon that give the level of access for
     * the node
     * @return ResourceReference, combination of both icon given as parameters.
     */
    private ResourceReference createCombinedIcon(final ImageIcon typeIcon, final ImageIcon accessLevel, String name) {
        ResourceReference combinedIcon = new ResourceReference(name) {
            @Override
            public IResource getResource() {
                final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
                int w = (int) (typeIcon.getImage().getWidth(null) + (float) accessLevel.getImage().getWidth(null));
                int h = Math.max(typeIcon.getImage().getHeight(null), accessLevel.getImage().getHeight(null));

                final BufferedImage testing = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = testing.createGraphics();
                typeIcon.paintIcon(null, g2, 1, 0);
                accessLevel.paintIcon(null, g2, typeIcon.getImage().getWidth(null), 0);
                g2.dispose();
                resource.setImage(testing);
                resource.setFormat("PNG");
                return resource;
            }
        };
        return combinedIcon;
    }

    private ImageIcon getNodeTypeIcon(final NodeType nodeType) {
        final ImageIcon nodeTypeIcon;
        if (nodeType instanceof CMDIResourceType) {
            nodeTypeIcon = fileIcon;
        } else if (nodeType instanceof CMDIResourceTxtType) {
            nodeTypeIcon = fileIconTxt;
        } else if (nodeType instanceof IMDISessionType) {
            nodeTypeIcon = sessionIcon;
        } else if (nodeType instanceof IMDICorpusType) {
            nodeTypeIcon = corpusIcon;
        } else if (nodeType instanceof IMDICatalogueType) {
            nodeTypeIcon = catalogueIcon;
        } else if (nodeType instanceof CMDIMetadataType) {
            nodeTypeIcon = cmdiIcon;
        } else {
            nodeTypeIcon = unknownIcon;
        }
        return nodeTypeIcon;
    }

    private ImageIcon getNodeAccessIcon(AccessLevel nodeAccessLevel) {
        switch (nodeAccessLevel) {
            case ACCESS_LEVEL_OPEN_EVERYBODY:
                return openIcon;
            case ACCESS_LEVEL_OPEN_REGISTERED_USERS:
                return licensedIcon;
            case ACCESS_LEVEL_PERMISSION_NEEDED:
                return restrictedIcon;
            case ACCESS_LEVEL_CLOSED:
                return closedIcon;
            case ACCESS_LEVEL_EXTERNAL:
                return externalIcon;
            default:
                return unknownIcon;
        }
    }
}
