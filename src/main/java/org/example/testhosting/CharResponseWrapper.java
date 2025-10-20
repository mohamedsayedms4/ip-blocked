package org.example.testhosting;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.*;

class CharResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream buffer;
    private final ServletOutputStream outputStream;
    private PrintWriter writer;

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        buffer = new ByteArrayOutputStream();
        outputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // Not implemented
            }

            @Override
            public void write(int b) throws IOException {
                buffer.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                buffer.write(b, off, len);
            }
        };
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()));
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        }
        outputStream.flush();
    }

    public byte[] toByteArray() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    @Override
    public String toString() {
        try {
            flushBuffer();
            return buffer.toString(getCharacterEncoding());
        } catch (IOException e) {
            return "";
        }
    }
}