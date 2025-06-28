package com.aravena.msrepouser.services;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;

public class ITextPdfRenderer implements PdfRenderer {
    @Override
    public void renderHtmlToPdf(String html, OutputStream outputStream) throws DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
    }
}