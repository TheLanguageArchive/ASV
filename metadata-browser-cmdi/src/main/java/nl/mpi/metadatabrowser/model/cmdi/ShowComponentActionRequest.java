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
package nl.mpi.metadatabrowser.model.cmdi;

import java.io.Serializable;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ShowComponentActionRequest implements ShowComponentRequest, Serializable {

    public TextArea content;
    public Panel nodeFormPanel;

    public ShowComponentActionRequest(TextArea textArea, Panel nodeFormPanel) {
        this.content = textArea;
        this.nodeFormPanel = nodeFormPanel;
    }

    public void setTextArea(TextArea content) {
        this.content = content;
    }

    @Override
    public Component getComponent(String id) {
        if (content != null) {
            return content;
        } else {
            return nodeFormPanel;
        }
    }
}
