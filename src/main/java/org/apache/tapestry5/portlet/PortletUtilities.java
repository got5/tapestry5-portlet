// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet;

import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.portlet.PortletSession;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the singleton registry that exists between each portlet in the
 * application. Provides initialization and access to that registry
 */
public final class PortletUtilities {

	private static final Logger logger = LoggerFactory.getLogger(PortletUtilities.class);

	private static final URLCodec CODEC = new URLCodec() {

		private BitSet contextSafe = (BitSet) WWW_FORM_URL.clone();

		{
			// Servlet container does not decode '+' in path to ' ',
			// so we encode ' ' to %20, not to '+'.
			contextSafe.clear(' ');
		}

		@Override
		public byte[] encode(byte[] bytes) {
			return encodeUrl(contextSafe, bytes);
		}
	};

	/**
	 * Encodes a string for inclusion in a URL. Slashes and percents are
	 * converted to "%25" and "%2F" respectively, then the entire string is URL
	 * encoded.
	 * 
	 * @param input
	 *            string to include, may not be blank
	 * @return encoded input
	 */
	public static String encodeContext(String input) {
		 assert InternalUtils.isNonBlank("input");

		try {
			return CODEC.encode(escapePercentAndSlash(input));
		} catch (EncoderException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static final String PERCENT = "%";
	private static final Pattern PERCENT_PATTERN = Pattern.compile(PERCENT);
	private static final String ENCODED_PERCENT = "%25";
	private static final Pattern ENCODED_PERCENT_PATTERN = Pattern.compile(ENCODED_PERCENT);

	private static final String SLASH = "/";
	private static final Pattern SLASH_PATTERN = Pattern.compile(SLASH);
	private static final String ENCODED_SLASH = "%2F";
	private static final Pattern ENCODED_SLASH_PATTERN = Pattern.compile(ENCODED_SLASH, Pattern.CASE_INSENSITIVE);

	/**
	 * Encodes percent and slash characters in the string for later decoding via
	 * {@link #unescapePercentAndSlash(String)}.
	 * 
	 * @param input
	 *            string to encode
	 * @return modified string
	 */
	public static String escapePercentAndSlash(String input) {
		return replace(replace(input, PERCENT_PATTERN, ENCODED_PERCENT), SLASH_PATTERN, ENCODED_SLASH);
	}

	/**
	 * Used to decode certain escaped characters that are replaced when using
	 * {@link #encodeContext(String)} .
	 * 
	 * @param input
	 *            a previously encoded string
	 * @return the string with slash and percent characters restored
	 */
	public static String unescapePercentAndSlash(String input) {
		return replace(replace(input, ENCODED_SLASH_PATTERN, SLASH), ENCODED_PERCENT_PATTERN, PERCENT);
	}

	private static String replace(String input, Pattern pattern, String replacement) {
		return pattern.matcher(input).replaceAll(replacement);
	}

	
	/**
	 * Remove the servlet context from the path.
	 * 
	 * @param path
	 *            The tapestry page path.
	 * @param context
	 *            The application context.
	 * @return
	 */
	public static String stripOffContext(String path, String context) {
		if (path.startsWith(context)) {
			return path.substring(context.length());
		}
		return path;
	}

	/**
	 * Remove query string part of the given path.
	 *
	 * @param URI
	 * @return
	 */
    public static String stripQueryString(String URI)
    {
        int qsIdx = URI.indexOf("?");
        return qsIdx > -1 ? URI.substring(0, qsIdx) : URI;
    }

    /**
     * Returns a list of the names of all attributes stored in the session whose name has the provided prefix. The names
     * are returned in alphabetical order.
     * 
     *  @param session
     *  			The Portlet Session
     *  @param prefix
	 *            	The attributeName prefix.
	 * 
	 * 	@return listofAttributesNames
     * 
     */
    public static List<String> getAttributeNames(PortletSession session,int scope,String prefix)
    {
        List<String> result = CollectionFactory.newList();

        Enumeration e = session.getAttributeNames(scope);
        while (e.hasMoreElements())
        {
        	String name = (String) e.nextElement();

        	if (name.startsWith(prefix)) result.add(name);
        }

        Collections.sort(result);

        return result;
    }
    
}
