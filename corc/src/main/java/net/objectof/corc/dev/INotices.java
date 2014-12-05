package net.objectof.corc.dev;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class INotices extends INotice
{
  private static final long serialVersionUID = 1L;

  public INotices(Category aCategory, String aMessageId)
  {
    super(aCategory, aMessageId, new ArrayList<Response>());
  }

  @Override
  public long count()
  {
    return getObject().size();
  }

  @Override
  public Collection<Response> getObject()
  {
    @SuppressWarnings("unchecked")
    Collection<Response> ret = (Collection<Response>) super.getObject();
    return ret;
  }

  @Override
  public Iterator<Response> iterator()
  {
    return getObject().iterator();
  }
}
