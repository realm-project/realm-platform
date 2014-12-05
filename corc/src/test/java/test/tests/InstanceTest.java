package test.tests;

import java.io.IOException;

import junit.framework.Assert;
import net.objectof.impl.corc.requests.ICollectingRequest;

import org.junit.Test;

import test.components.ServicePoint;

public class InstanceTest
{
  private final ServicePoint services = new ServicePoint("/test");

  @Test
  public void test() throws IOException
  {
    String instance = "test.objectof.org:20140101/ctx/Person-759";
    ICollectingRequest request = (ICollectingRequest) services.perform(
        "retrieve", instance);
    Assert.assertEquals(2, request.getEntries().size());
    for (String key : request.getEntries())
    {
      System.out.println(key + ": " + request.getResponse(key));
    }
  }
}
