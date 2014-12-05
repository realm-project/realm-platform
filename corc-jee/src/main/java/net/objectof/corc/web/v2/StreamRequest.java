package net.objectof.corc.web.v2;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface StreamRequest
{
  public String getPath();

  public Reader getReader() throws IOException;

  public String getRequestType();

  public String getResponseType();

  public Writer getWriter() throws IOException;
}
