package net.objectof.corc.dev;

public class INotice extends IResponse
{
  private static final long serialVersionUID = 1L;
  private final String theId;
  private final Category theCategory;

  public INotice(Category aCategory, String aId)
  {
    super(null);
    theCategory = aCategory;
    theId = aId;
  }

  public INotice(Category aCategory, String aId, Object aObject)
  {
    super(aObject);
    theCategory = aCategory;
    theId = aId;
  }

  @Override
  public Category getCategory()
  {
    return theCategory;
  }

  @Override
  public String getMessageId()
  {
    return theId;
  }
}
