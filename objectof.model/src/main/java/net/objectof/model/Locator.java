package net.objectof.model;

/**
 * A Locator transforms (resolves) an Id to an URI.  The URI may be an ans URI
 * or another URI such as an URL that provides a physical location, such as a web service, for the id.
 * @author jdh
 *
 */
public interface Locator
{
  String locate(Id<?> aId);
}
