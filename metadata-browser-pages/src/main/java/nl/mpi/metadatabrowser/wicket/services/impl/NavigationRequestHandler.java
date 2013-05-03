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
package nl.mpi.metadatabrowser.wicket.services.impl;

import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget.RRS;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NavigationRequestHandler implements ControllerActionRequestHandler<NavigationRequest> {

    private final static Logger logger = LoggerFactory.getLogger(NavigationRequestHandler.class);
    private String rrsUrl;

    @Override
    public void handleActionRequest(RequestCycle requestCycle, NavigationRequest actionRequest, Page originatingPage) throws RequestHandlerException {
	switch (actionRequest.getTarget()) {
	    case RRS:
		logger.debug("Received request to navigate to RRS with parameters {}", actionRequest.getParameters());
		// Navigate to RRS
		// TODO: Parameters?
		requestCycle.scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(rrsUrl));
		break;
	    default:
		// Other, cannot handle
		throw new RequestHandlerException("Don't know how to handle navigation request target " + actionRequest.getTarget());
	}
    }

    /**
     *
     * @param rrsUrl Base URL of Resource Request System
     * @see NavigationRequest.NavigationTarget#RRS
     */
    public void setRrsUrl(String rrsUrl) {
	this.rrsUrl = rrsUrl;
    }
}