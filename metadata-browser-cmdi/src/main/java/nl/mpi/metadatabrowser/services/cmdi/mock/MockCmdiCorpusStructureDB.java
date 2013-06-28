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
package nl.mpi.metadatabrowser.services.cmdi.mock;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.corpusstructure.provider.AccessInfo;
import nl.mpi.archiving.corpusstructure.provider.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.provider.UnknownNodeException;
import nl.mpi.archiving.tree.CorpusNode;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockCmdiCorpusStructureDB implements CorpusStructureProvider, Serializable {

    private MockCorpusNode rootNode;

    public void setRootNode(MockCorpusNode rootNode) {
	this.rootNode = rootNode;
    }

    @Override
    public MockCorpusNode getNode(URI nodeId) {
	return rootNode.getChildRecursive(nodeId);
    }

    private List<CorpusNode> getChildrenCMDIs(URI nodeId) throws UnknownNodeException {
	final MockCorpusNode node = getNode(nodeId);
	if (node != null) {
	    return Collections.<CorpusNode>unmodifiableList(node.getChildren());
	} else {
	    return Collections.emptyList();
	}
    }

    @Override
    public URI getObjectURI(URI id) throws UnknownNodeException {
	final CorpusNode node = getNode(id);
	if (node != null) {
	    return node.getUri();
	} else {
	    return null;
	}
    }

    //@Override
    public String getProfileId(URI uri) {
	return "profile";
    }

    @Override
    public String getAdminKey(String name) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
    }

    @Override
    public boolean getStatus() {
	return true;
    }
    
    @Override
    public URI getHandleResolverURI() {
	return URI.create("http://hdl.handle.net/");
    }

    @Override
    public String getHandle(URI nodeid) {
	return "11142/00-021C2EA5-66DB-4363-A957-CE9FEE4226CD";
    }

    @Override
    public Timestamp getObjectFileTime(URI node) {
	return new Timestamp(1368520487);
    }

    @Override
    public String getObjectChecksum(URI node) {
	return "adab88ba00910f9591df887f45a05419";
    }

    @Override
    public long getObjectSize(URI node) {
	return 2640;
    }

    @Override
    public URI getRootNodeId() {
	return rootNode.getNodeId();
    }

    @Override
    public List<URI> getSubnodes(URI nodeId) throws UnknownNodeException {
	List<CorpusNode> childrenNodes = getChildrenNodes(nodeId);
	List<URI> subUris = new ArrayList<URI>(childrenNodes.size());
	for (CorpusNode node : childrenNodes) {
	    subUris.add(node.getNodeId());
	}
	return subUris;
    }

    @Override
    public List<CorpusNode> getChildrenNodes(URI nodeId) throws UnknownNodeException {
	return getChildrenCMDIs(nodeId);
    }

    @Override
    public List<URI> getParentNodes(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getDescendants(URI nodeId, nl.mpi.archiving.corpusstructure.provider.CorpusNodeType nodeType, String format) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getDescendants(URI nodeId, nl.mpi.archiving.corpusstructure.provider.CorpusNodeType nodeType, Collection<String> formats) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getDescendants(URI nodeId, nl.mpi.archiving.corpusstructure.provider.CorpusNodeType nodeType, Collection<String> formats, String user, boolean onsite) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CorpusNode> getDescendantResources(URI nodeId, boolean onsiteOnly, String userToRead, String userToWrite) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getResourcesFromArchive(nl.mpi.archiving.corpusstructure.provider.CorpusNodeType nodeType, Collection<String> formats, String user, boolean onlyAvailable, boolean onlyOnSite) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getProfileSchemaLocation(URI nodeId) throws UnknownNodeException {
	return URI.create("profile");
    }

    @Override
    public CorpusNodeType getCorpusNodeType(URI nodeId) throws UnknownNodeException {
	return getNode(nodeId).getCorpusNodeType();
    }

    @Override
    public String getCanonicalVPath(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNamePath(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> translateToNamePath(List<URI> nodeids) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI resolveNameInAnnotationContext(URI annotationNodeId, String name, String function) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI resolveNameInSessionContext(URI sessionNodeId, String name, String function) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCanonicalParent(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getObjectURI(URI id, int context) throws UnknownNodeException {
        URI newURI = null;
        try {
            newURI = new URI("www.anewurifortestnode.com");
        } catch (URISyntaxException ex) {
            Logger.getLogger(MockCmdiCorpusStructureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
            return newURI;

    }

    @Override
    public URI getObjectURIForPid(URI pid) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getObjectId(URI uri, int context) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getObjectForPID(String pid) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getObjectId(URI uri) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getObjectsByChecksum(String checksum) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getObjectTimestamp(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getObjectPID(URI nodeId) throws UnknownNodeException {
	URI pid = null;
        try {
            pid = new URI("123456789321");
        } catch (URISyntaxException ex) {
            Logger.getLogger(MockCmdiCorpusStructureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pid;
    }

    @Override
    public boolean isAccessible(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOnSite(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasReadAccess(URI nodeid, String uid) throws UnknownNodeException {
	return true;
    }

    @Override
    public List<URI> getNewArchiveObjectsSince(Date timestamp, boolean onsiteonly, boolean urlformat, boolean usefiletime) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AccessInfo getObjectAccessInfo(URI nodeId) throws UnknownNodeException {
	return new MockAccessInfo();
    }
}
