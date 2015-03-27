//=============================================================================
//===	Copyright (C) 2001-2007 Food and Agriculture Organization of the
//===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===	and United Nations Environment Programme (UNEP)
//===
//===	This program is free software; you can redistribute it and/or modify
//===	it under the terms of the GNU General Public License as published by
//===	the Free Software Foundation; either version 2 of the License, or (at
//===	your option) any later version.
//===
//===	This program is distributed in the hope that it will be useful, but
//===	WITHOUT ANY WARRANTY; without even the implied warranty of
//===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===	General Public License for more details.
//===
//===	You should have received a copy of the GNU General Public License
//===	along with this program; if not, write to the Free Software
//===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
//===
//===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===	Rome - Italy. email: geonetwork@osgeo.org
//==============================================================================

package org.fao.geonet.services.region;

import com.google.common.collect.Lists;
import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.server.dispatchers.ServiceManager;
import org.fao.geonet.Util;

import org.fao.geonet.kernel.region.RegionParams;
import org.fao.geonet.kernel.region.RegionsDAO;
import org.fao.geonet.kernel.region.Request;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import java.nio.file.Path;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

//=============================================================================

/**
 * Returns a specific region and coordinates given its id
 */
@Controller
public class List implements Service {

    public void init(Path appPath, ServiceConfig params) throws Exception {
    }

    // --------------------------------------------------------------------------
    // ---
    // --- Service
    // ---
    // --------------------------------------------------------------------------

    @Autowired
    private ServiceManager serviceManager;

    /**
     *
     * Example Response
     * <pre><code>
     * &amp;regions count="1">
     *   &amp;region id="country.1" hasGeom="true" categoryId="country">
     *     &amp;label>
     *       &amp;eng>France</eng>
     *     &amp;/label>
     *     &amp;category>
     *       &amp;fre>Pays</fre>
     *       &amp;eng>Country</eng>
     *     &amp;/category>
     *     &amp;/region>
     * &amp;/regions>
     * </code></pre>
     * @throws Exception
     *
     * @param categoryId only return labels contained in the given category - optional
     * @param label searches the labels for regions that contain the text in this parameters - optional
     * @param maxRecords limit the number of results returned - optional
     */

    @RequestMapping(value = "/{lang}/regions.list", produces = {
            MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public Element exec(@PathVariable String lang,
                        @RequestParam(required = false) String label,
                        @RequestParam(required = false) String categoryId,
                        @RequestParam(defaultValue = "-1") int maxRecords,
                        NativeWebRequest webRequest) throws Exception {

        final HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ServiceContext context = serviceManager.createServiceContext("regions.list", lang, nativeRequest);
        Collection<RegionsDAO> daos = context.getApplicationContext().getBeansOfType(RegionsDAO.class).values();

        Collection<RegionsDAO> applicableDAOs = Lists.newArrayList();
        for (RegionsDAO dao : daos) {
            if (dao.includeInListing()) {
                dao.
            }
        }

        Element regions = null;
        for (RegionsDAO dao : applicableDAOs) {
            if (dao.includeInListing()) {
                Request request = dao.createSearchRequest(context);
                if (label != null) {
                    request.label(label);
                }
                if (categoryId != null) {
                    request.categoryId(categoryId);
                }
                if (maxRecords > 0) {
                    request.maxRecords(maxRecords);
                }
                Element tmp = request.xmlResult();
                if (regions != null) {
                    @SuppressWarnings("unchecked")
                    java.util.List<Element> children = tmp.getChildren();
                    for (Element element : children) {
                        regions.addContent(element);
                    }
                } else {
                    regions = tmp;
                }
            }
        }

        return regions;
    }

}

// =============================================================================

