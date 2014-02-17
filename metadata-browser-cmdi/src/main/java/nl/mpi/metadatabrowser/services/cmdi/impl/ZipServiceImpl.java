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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * All logic to download multiples files happens here. Parent node is passed on
 * and recursively get all children and add them to the zipFile.
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ZipServiceImpl implements ZipService, Serializable {

    private final static Logger logger = LoggerFactory.getLogger(ZipServiceImpl.class);
    private final static long MAX_LIMIT = FileUtils.ONE_GB * 2; // 200000000L ; //4000000000L  //4GB
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    private final AccessInfoProvider accessInfoProvider;
    private int overallSize = 0;

    /**
     * Constructor
     *
     * @param csdb
     * @param nodeResolver
     */
    @Autowired
    public ZipServiceImpl(CorpusStructureProvider csdb, NodeResolver nodeResolver, AccessInfoProvider accessInfoProvider) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
	this.accessInfoProvider = accessInfoProvider;
    }

    /**
     * create zip and logic to add file to the zip
     *
     * @param node TypedCorpusNode : parent node
     * @param userid String : user that request the download
     * @return File : return file which is basically the zip file.
     * @throws IOException
     * @throws UnknownNodeException
     * @throws FileNotFoundException
     */
    @Override
    @Transactional
    public File createZipFileForNodes(TypedCorpusNode node, String userid) throws IOException, FileNotFoundException {
	//create object of FileOutputStream
	final File tmpFile = File.createTempFile("mdtbrowser", ".zip");
	final FileOutputStream fout = new FileOutputStream(tmpFile);
	//create object of ZipOutputStream from FileOutputStream
	final ZipOutputStream zout = new ZipOutputStream(fout);

	int filesAdded = addChildren(zout, node.getNodeURI(), userid, 0);
        if(filesAdded == 0){
            return null;
        }else {
	//close the ZipOutputStream
	zout.close();
	return tmpFile;
        }
    }

    /**
     * recursive method to add file and children to zip
     *
     * @param zout ZipOutputStream : the zip stream
     * @param nodeUri URI : uri of the node to be zipped or checked for children
     * @param userid String: the user that request the download
     * @param itemsadded int: number to check that at least one item will be added to the zip
     * @param overallSize long: check the limit of the zip file size.
     */
    private int addChildren(ZipOutputStream zout, URI nodeUri, String userid, int itemsAdded) {
	final List<CorpusNode> childrenNodes = new CopyOnWriteArrayList<CorpusNode>(csdb.getChildNodes(nodeUri));
	boolean hasaccess;
        int filesAdded =0;
	if (childrenNodes.size() > 0) {
	    for (CorpusNode childNode : childrenNodes) {
		URI childUri = childNode.getNodeURI();
		if (itemsAdded == 0) { // check if at least one resource is accessible for user
		    if (childUri != null) {
			hasaccess = checkAccess(userid, childUri);// get access rights for each resource
			logger.debug("resources-download, access for " + childUri + ", " + userid + ", " + hasaccess);
			if (hasaccess) {
			    itemsAdded++;
			}
		    }
		}
		if (overallSize > MAX_LIMIT) { // check size limit 4GB
		    overallSize = 0;
		    logger.info("maximum size limit of {} GB reached", ((double) MAX_LIMIT) / FileUtils.ONE_GB);
		    break;
		}
		if (!csdb.getChildNodes(childUri).isEmpty()) {
		    filesAdded = zipFile(zout, childNode, userid, itemsAdded, filesAdded); // here zip parent file
		    addChildren(zout, childUri, userid, itemsAdded);// getChildren for parent file
		    continue;
		} else {
		   filesAdded = zipFile(zout, childNode, userid, itemsAdded, filesAdded);
		}               
	    }
	} else { // might as well be a node without children. But parent node might have children
	    logger.error("Error: called resources-download for  node without children");

	}
        return filesAdded;
    }

    /**
     * method to check accessibility to a node for user
     *
     * @param userid Stirng: user that request the download
     * @param nodeid URI: uri from the node that needs to grant access to the user
     * @return boolean. true if user has access to the specificed node
     * @throws UnknownNodeException
     */
    private boolean checkAccess(String userid, URI nodeId) {
	if (userid == null || userid.equals("") || userid.equals("anonymous")) {
	    return accessInfoProvider.hasReadAccess(nodeId, AccessInfoProvider.EVERYBODY);
	} else {
	    return accessInfoProvider.hasReadAccess(nodeId, userid);
	}
    }

    /**
     *
     * Core method of adding files to the zip.
     *
     * @param zout ZipOutputStream : the zip stream
     * @param childNode CorpusNode: node to be added to the zip
     * @param userid String : user that is requesting access and download action
     * @param itemsAdded int : number of items to be added. should be 1 to process
     * @throws UnknownNodeException
     */
    private int zipFile(ZipOutputStream zout, CorpusNode childNode, String userid, int itemsAdded, int filesAdded) {
	boolean hasaccess;
	try {
	    if (itemsAdded > 0) { // must be minimum 1 to proceed = 1 accessible resource for user
		byte[] buffer = new byte[1024];
		final URL childNodeUrl = nodeResolver.getUrl(childNode);

		if (childNodeUrl != null) {
		    final URI childNodeURI = childNode.getNodeURI();
		    hasaccess = checkAccess(userid, childNode.getNodeURI());// get access rights for the given node
		    logger.debug("resources-download, access for " + childNodeURI + ", " + userid + ", " + hasaccess);
		    if (hasaccess) {// acess granted, add node to zip
			logger.info("resources-download: " + childNodeUrl.toString());

			String fileName = new File(childNodeUrl.getPath()).getName();
			//String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
			final InputStream is = nodeResolver.getInputStream(childNode);
			try {
			    final ZipEntry ze = new ZipEntry(fileName);
			    zout.putNextEntry(ze);

			    int length;
			    while ((length = is.read(buffer)) > 0) {
				zout.write(buffer, 0, length);
			    }
			    zout.closeEntry();
                            filesAdded++;
			    overallSize += ze.getCompressedSize(); // increment zip size for latter limit size check
			    logger.info("Copied resource: " + childNodeUrl + "  to zipFile");
			} catch (NullPointerException e) {
			    logger.error("unvalid type of file. Could not find path for this file : {}", fileName, e);
			} finally {
			    is.close();
			}
		    } else {
			logger.info("User " + userid + " has no access to " + childNodeUrl);
		    }

		} else {
		    logger.error("Error: nodeurl for resourcenode " + childNode + " was not found");
		}
	    }
	} catch (IOException ioe) {
	    logger.error("IOException :" + ioe);
	}
        return filesAdded;
    }
}
