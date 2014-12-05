package net.objectof.util;

import java.io.IOException;
import java.io.Writer;

public interface Streamer<T>
{
  public void stream(Writer aWriter, T aObject) throws IOException;
}
