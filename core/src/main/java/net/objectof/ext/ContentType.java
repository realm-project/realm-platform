package net.objectof.ext;

import net.objectof.Type;

public interface ContentType extends Type
{
  public enum Unit
  {
    /**
     * Binary streams (e.g. InputStream, OutputStream)
     */
    OCTET,
    /**
     * Character streams (Reader, Writer, Appendable, ...).
     */
    CHARACTER,
    /**
     * (RESERVED) Variable-length encoded binary values. UTF-8 streams are
     * generally categorized as CHARACTER and not VARYING.
     */
    VARYING,
    /**
     * Other/unknown encoding. Java serialization may be categorized under this.
     */
    OTHER
  }

  public String getCallbackSelector();

  public String getMediaType();

  public Unit getUnit();
}
