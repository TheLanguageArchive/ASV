/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.URIFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.video.Html5Video;

/**
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that is responsible for video display in the browser
 */
public final class MediaFilePanel extends Panel {

    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private CorpusStructureProvider csdb;
    @SpringBean
    private URIFilter nodeUriFilter;

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    /**
     * location of the streaming file where %s is the filename without extension
     * of the requested version
     */
    public final static String STREAMING_VERSION_LOCATION = "../Streaming/%s.mp4";
    public final static String STREAMING_FILE_TYPE = "video/mp4";
    public final static String STREAMING_FILE_EXTENSION = ".mp4";

    private IModel<Boolean> allowStreamingModel = Model.of(Boolean.TRUE);

    /**
     * Method that handle the html5 tag generation for video and build the
     * wicket front-end of the panel.
     *
     * @param id
     * @param node TypedCorpusNode, the node to be displayed as video
     */
    public MediaFilePanel(String id, TypedCorpusNode node) {
        super(id);
        final String nodeUrl = getNodeUrl(node);

        add(new Label("viewTitle", "Viewing " + node.getName()));

        final List<MediaSource> html5media = findHtml5alternative(node);

        // add view for case where no HTML5 resources available
        add(new ExternalFramePanel("altView", nodeUrl) {

            @Override
            public boolean isVisible() {
                return html5media == null || !allowStreamingModel.getObject();
            }

        });

        final WebMarkupContainer streaming = new WebMarkupContainer("streaming") {

            @Override
            public boolean isVisible() {
                return html5media != null && allowStreamingModel.getObject();
            }
        };
        add(streaming);

        if (html5media != null) {
            streaming.add(new MediaFileExternalLink("originalFile", nodeUrl));
            streaming.add(new MediaFileExternalLink("streamingFile", html5media.get(0).getSrc()));
        }

        // add view for case where HTML5 resources is available
        final Html5Video video = (new Html5Video("displayVideo", createMediaModel(html5media)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isControls() {
                return true;
            }

            @Override
            protected boolean isAutoPlay() {
                return true;
            }

        });
        streaming.add(video);
    }

    private List<MediaSource> findHtml5alternative(TypedCorpusNode node) {
        final URI nodeParent = csdb.getCanonicalParent(node.getNodeURI());
        final File localFile = resolver.getLocalFile(node);
        final String parentName = getFilename(node, localFile);
        final String parentBaseName = FilenameUtils.removeExtension(parentName);
        final List<CorpusNode> childrenNodes = csdb.getChildNodes(nodeParent);

        final List<MediaSource> mm = new ArrayList<>();
        final URI streamingVersionURI = getStreamingVersionURI(parentBaseName, localFile, node);
        if (streamingVersionURI != null) {
            mm.add(new MediaSource(streamingVersionURI.toString()));
        } else {
            for (CorpusNode childNode : childrenNodes) { // parent has children
                final String childNodeName = getFilename(childNode, resolver.getLocalFile(childNode));
                if (childNodeName.endsWith(STREAMING_FILE_EXTENSION)) { // look up for children with mp4 extension
                    // name comparison. Should be same node if only extension differs within the same session
                    final String childBaseName = FilenameUtils.removeExtension(childNodeName);
                    if (parentBaseName.equals(childBaseName)) {
                        logger.debug("Found streaming alternative for {}: {}", node, childNode);
                        String childNodeURL = getNodeUrl(childNode);
                        mm.add(new MediaSource(childNodeURL, STREAMING_FILE_TYPE));
//                  mm.add(new MediaSource(url in ogg format, "video/ogg.")); // ideally supported but not for now
                    }
                }
            }
        }
        if (mm.isEmpty()) {
            return null;
        } else {
            return mm;
        }
    }

    private URI getStreamingVersionURI(final String parentBaseName, final File localFile, TypedCorpusNode node) {
        if (localFile != null) {
            // try to find the streaming alternative on disk
            final String streamingVersionRelativePath = String.format(STREAMING_VERSION_LOCATION, parentBaseName);
            final URI streamingVersionLocalURI = localFile.toURI().resolve(streamingVersionRelativePath);
            final File streamingVersionFile = new File(streamingVersionLocalURI);
            // constructed a file location, now see if it exists
            if (streamingVersionFile.exists()) {
                try {
                    // bingo, construct the public URL
                    logger.debug("Found streaming alternative for {} at {}", localFile, streamingVersionFile);
                    return resolver.getUrl(node).toURI().resolve(streamingVersionRelativePath);
                } catch (URISyntaxException ex) {
                    logger.error("Invalid URI while looking for streaming version of {}", localFile, ex);
                }
            }
        }
        return null;
    }

    private String getNodeUrl(CorpusNode node) {
        String nodeURL;
        try {
            // allow filter to rewrite, e.g. http->https
            nodeURL = nodeUriFilter.filterURI(resolver.getUrl(node).toURI()).toString();
        } catch (URISyntaxException ex) {
            // highly unlikely
            logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
            nodeURL = resolver.getUrl(node).toString();
        }
        return nodeURL;
    }

    private String getFilename(CorpusNode node, File localFile) {
        if (localFile != null) {
            return localFile.getName();
        } else {
            //fallback, best effort
            return node.getName();
        }
    }

    private IModel<List<MediaSource>> createMediaModel(final List<MediaSource> html5media) {
        IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<MediaSource> getObject() {
                return html5media;
            }
        };
        return mediaSourceList;
    }

    /**
     * External link with a label that shows the filename (bit after last slash)
     * in the label
     */
    private static class MediaFileExternalLink extends ExternalLink {

        public MediaFileExternalLink(String id, String link) {
            super(id, link);

            // add label
            final String fileName = link.replaceAll(".*/", ""); // strip off everything until last slash
            add(new Label("name", fileName));
        }

    }
}
