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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class PanelShowComponent extends Panel {

    private final static Logger logger = LoggerFactory.getLogger(PanelShowComponent.class);

    public PanelShowComponent(String id, TypedCorpusNode node, CmdiCorpusStructureDB csdb) {
        super(id);
        final Form form = new Form("nodeInfoForm");
        String nodeName = node.getName();
        String nodeId = Integer.toString(node.getNodeId());
        URI nodeUri = node.getUri();
        String title = "Resource \"" + node.getName() + "\" + from \"" + node.getParent() + "\"";

        Timestamp objectFileTime = csdb.getObjectFileTime(node);
        String lastModified = "";
        if (objectFileTime != null) {
            lastModified = new Date(objectFileTime.getTime()).toString();
        }

        String resolver = csdb.getHandleResolverURI().toString();
        String archive_name = "archive"; //TODO enable csdb.getArchiveRoots().getArchiveName();
        if (archive_name == null) {
            archive_name = "unknown";
        }

        String handle = csdb.getHandle(node.getNodeId());

        final String archiveName = archive_name;
        final String resolvedHandle = resolver.concat(handle);

        String url = null;
        try {
            url = nodeUri.toURL().toString();
        } catch (MalformedURLException ex) {
            logger.error("url error while getting URL", ex);
        }

        // create citations to be displayed
        StringBuilder sb = new StringBuilder();
        if (handle == null || handle.equals("")) {
            sb.append("<br/>Some browsers support direct use of \"handle URIs\", if that is the case you can bookmark the following link:<br/>");
            sb.append("Handle link to resource: <a href=\"");
            sb.append("hdl:");
            sb.append(handle);
            sb.append("\">");
            sb.append(nodeName).append("</a><br/>");
            sb.append("<br/>To save a link to the resource using the central handle resolver, bookmark the following link:<br/>");
            sb.append("Handle link via central resolver to resource: <a href=\"");
            sb.append(resolvedHandle);
            sb.append("\">");
            sb.append(nodeName);
            sb.append("</a><br/>");
            sb.append("<br/>To save the URL for this resource which is NOT guaranteed to be persistent, bookmark the following link:<br/>");
            sb.append("URL link to resource: <a href=\"");
            sb.append(url);
            sb.append("\">");
            sb.append(nodeName);
            sb.append("</a><br/>");
        } else {
            sb.append("<br/>To save the URL for this resource which is NOT guaranteed to be persistent, bookmark the following link:<br/>");
            sb.append("URL link to resource: <a href='");
            sb.append(url);
            sb.append("'>");
            sb.append(nodeName);
            sb.append("</a><br/>");
        }


        // HANDLE bookmark action here
        form.add(new Label("nodeId", nodeId));
        form.add(new Label("name", nodeName));
        form.add(new Label("handle", handle));
        form.add(new Label("url", url));
        form.add(new Label("title", title));
        Label bookmarkLabel = new Label("bookmark", sb.toString());
        bookmarkLabel.setEscapeModelStrings(false);
        form.add(bookmarkLabel);

        //embeded citation down the page
        form.add(new Label("cite_title", title));
        form.add(new Label("author", "unknwon"));
        form.add(new Label("archive_name", archiveName));
        form.add(new Label("format", node.getNodeType().getName()));
        form.add(new Label("last_modified", lastModified));
        form.add(new Label("cite_handle", handle));
        form.add(new Label("resolvedHandle", resolvedHandle));

        // Put details/submit form in container for refresh through AJAX
        final MarkupContainer formContainer = new WebMarkupContainer("formContainer");
        formContainer.add(form);
        // Add container to page
        add(formContainer);
    }
}
