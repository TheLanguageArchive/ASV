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
import java.util.Map;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ShowComponentActionRequest implements ShowComponentRequest, Serializable {

    public  TextArea content;
    public  Form nodeForm;
    
      public ShowComponentActionRequest(TextArea textArea) {
        this.content =textArea;
    }

    ShowComponentActionRequest(Form nodeForm) {
        this.nodeForm = nodeForm;
    }
    
    public void setTextArea(TextArea content) {
        this.content = content;
    }


    @Override
    public Component getComponent() {
        return content;
    }
}
