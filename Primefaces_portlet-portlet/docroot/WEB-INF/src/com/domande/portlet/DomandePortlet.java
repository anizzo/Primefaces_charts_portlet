package com.domande.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.portletfaces.bridge.GenericFacesPortlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class DomandePortlet extends GenericFacesPortlet {

	protected String viewTemplate;
	private static Log _log = LogFactoryUtil.getLog(DomandePortlet.class);

	public void init() {
		 viewTemplate = getInitParameter("javax.portlet.faces.defaultViewId.view");
	}

	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		 include(viewTemplate, renderRequest, renderResponse);
	}

	protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}
}
