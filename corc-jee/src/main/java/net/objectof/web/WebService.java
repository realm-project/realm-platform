package net.objectof.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebService
{
  public void service(HttpServletRequest aRequest, HttpServletResponse aResponse)
      throws ServletException, IOException;
}
