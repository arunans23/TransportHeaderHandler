package com.wso2.apim.transport.headers.handler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.nhttp.NhttpConstants;

import java.util.*;

public class TransportHeaderUtil {
    private static final Log log = LogFactory.getLog(TransportHeaderUtil.class);

    public static final String PASSTHROUGH_SOURCE_CONNECTION = "pass-through.Source-Connection";
    public static final String RESPONSE_INFLOW_INVOKED = "RESPONSE_INFLOW_INVOKED";

    /**
     * Populates standard request headers from comma separate string.
     *
     * @param standardHeadersStr Comma separated preserve enable http headers
     * @return list of parsed headers from the given header object
     */
    public static List<String> populateStandardHeaders(String standardHeadersStr) {

        List<String> standardHeaders = new ArrayList<String>();
        if (StringUtils.isNotEmpty(standardHeadersStr)) {
            String[] headerList = standardHeadersStr.trim().split(",");
            if (headerList.length > 0) {
                standardHeaders.addAll(Arrays.asList(headerList));
            }
        }
        return standardHeaders;
    }

    /**
     * Removes the provided headers from the Transport Headers list in synCtx.
     *
     * @param synCtx            Synapse Message Context
     * @param removableHeaders  Header list that needs to be removed
     */
    public static void removeTransportHeadersFromList(MessageContext synCtx, List<String> removableHeaders) {
        Map<String, String> transportHeaders = getTransportHeaders(synCtx);
        for (String header : removableHeaders) {
            if (transportHeaders.containsKey(header)) {
                if (log.isDebugEnabled()) {
                    log.debug("'" + header + "' is removed from the Transport header list");
                }
                transportHeaders.remove(header);
            }
        }
    }

    /**
     * Removes the provided headers from the Excess Transport Headers list in synCtx.
     *
     * @param synCtx            Synapse Message Context
     * @param removableHeaders  Header list that needs to be removed
     */
    public static void removeExcessTransportHeadersFromList(MessageContext synCtx, List<String> removableHeaders) {
        Map<String, String> excessTransportHeaders = getExcessTransportHeaders(synCtx);
        Iterator<String> headerIter = excessTransportHeaders.keySet().iterator();
        while (headerIter.hasNext()) {
            String headerName = headerIter.next();
            for (String removableHeader : removableHeaders) {
                if (removableHeader.equalsIgnoreCase(headerName)) {
                    headerIter.remove();
                    if (log.isDebugEnabled()) {
                        log.debug("'" + headerName + "' is removed from the Excess Transport header list");
                    }
                }
            }
        }
    }

    /**
     * Removes the header set from one map from another.
     *
     * @param requestHeaders           Headers present in request
     * @param responseHeaders   Headers present in the response
     * @param preserveHeaders   Headers to be preserved
     */
    public static void removeRequestHeadersFromResponseHeaders(Map requestHeaders, Map responseHeaders,
                                                               List<String> preserveHeaders) {
        for (Object headerObj : requestHeaders.keySet()) {
            String headerName = (String) headerObj;
            if (!preserveHeaders.contains(headerName) && responseHeaders.containsKey(headerName)) {
                if (log.isDebugEnabled()) {
                    log.debug("Request header '" + headerName + "' is removed from the Response");
                }
                responseHeaders.remove(headerName);
            }
        }
    }

    /**
     * Axis2 Message Context has two lists of transport headers: TransportHeaders and ExcessTransportHeaders
     * This method returns TransportHeaders.
     *
     * @param synCtx Synapse Message Context
     * @return map of Transport headers present in Axis2 Message Context
     */
    public static Map getTransportHeaders(MessageContext synCtx) {
        return (Map)((Axis2MessageContext) synCtx).getAxis2MessageContext().
                getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
    }

    /**
     * Axis2 Message Context has two lists of transport headers: TransportHeaders and ExcessTransportHeaders
     * This method returns ExcessTransportHeaders
     *
     * @param synCtx Synapse Message Context
     * @return map of Excess Transport headers present in Axis2 Message Context
     */
    public static Map getExcessTransportHeaders(MessageContext synCtx) {
        return (Map)((Axis2MessageContext) synCtx).getAxis2MessageContext().
                getProperty(NhttpConstants.EXCESS_TRANSPORT_HEADERS);
    }
}
