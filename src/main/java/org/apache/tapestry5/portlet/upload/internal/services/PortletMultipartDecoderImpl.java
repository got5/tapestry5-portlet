// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry5.portlet.upload.internal.services;

import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.portlet.upload.services.PortletMultipartDecoder;
import org.apache.tapestry5.upload.internal.services.MultipartDecoderImpl;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;
import org.apache.tapestry5.upload.services.UploadSymbols;

/**
 * @author Raphael Jean
 */
public class PortletMultipartDecoderImpl extends MultipartDecoderImpl implements
        PortletMultipartDecoder
{
    private FileUploadException uploadException;

    private final FileItemFactory fileItemFactory;

    private final long maxRequestSize;

    private final long maxFileSize;

    public PortletMultipartDecoderImpl(FileItemFactory fileItemFactory,
            @Symbol(UploadSymbols.REQUESTSIZE_MAX)
            long maxRequestSize,

            @Symbol(UploadSymbols.FILESIZE_MAX)
            long maxFileSize,

            @Inject
            @Symbol(SymbolConstants.CHARSET)
            String requestEncoding)
    {
        super(fileItemFactory, maxRequestSize, maxFileSize, requestEncoding);
        this.fileItemFactory = fileItemFactory;
        this.maxRequestSize = maxRequestSize;
        this.maxFileSize = maxFileSize;
    }

    public ActionRequest decode(ActionRequest request)
    {
        List<FileItem> fileItems = parseRequest(request);
        return processFileItems(request, fileItems);
    }

    @SuppressWarnings("unchecked")
    protected List<FileItem> parseRequest(ActionRequest request)
    {
        try
        {
            return createPortletFileUpload().parseRequest(request);
        }
        catch (FileUploadException e)
        {
            uploadException = e;

            return Collections.emptyList();
        }
    }

    private PortletFileUpload createPortletFileUpload()
    {

        PortletFileUpload upload = new PortletFileUpload(fileItemFactory);

        // set maximum file upload size
        upload.setSizeMax(maxRequestSize);
        upload.setFileSizeMax(maxFileSize);

        return upload;
    }

    protected ActionRequest processFileItems(ActionRequest request, List<FileItem> fileItems)
    {
        if (uploadException == null && (fileItems == null || fileItems.isEmpty())) { return request; }

        ParametersPortletRequestWrapper wrapper = new ParametersPortletRequestWrapper(request);

        for (FileItem item : fileItems)
        {
            if (item.isFormField())
            {
                wrapper.addParameter(item.getFieldName(), item.getString());
            }
            else
            {
                wrapper.addParameter(item.getFieldName(), item.getName());
                addUploadedFile(item.getFieldName(), new UploadedFileItem(item));
            }
        }

        return wrapper;
    }

}
