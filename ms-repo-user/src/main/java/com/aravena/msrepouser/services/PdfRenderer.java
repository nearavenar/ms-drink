package com.aravena.msrepouser.services;

import com.lowagie.text.DocumentException;

import java.io.OutputStream;

public interface PdfRenderer {
    void renderHtmlToPdf(String html, OutputStream outputStream) throws DocumentException;
}
