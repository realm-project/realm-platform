package net.objectof.model.corc;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objectof.Context;
import net.objectof.corc.Action;
import net.objectof.corc.Service;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.corc.validation.ValidationResult;

public class IRepoHandler extends IHandler<HttpRequest>
{
  private final Package theRepo;
  private final Map<String, String> theSupportedTypes;
  private final List<Service<Resource<?>, ValidationResult>> postValidators;

  public IRepoHandler(Package repository) {
	  this(repository, Collections.<Service<Resource<?>, ValidationResult>>emptyList());
  }
  
  public IRepoHandler(Context<Package> aDatabase, String aRepoName)
  {
    this(aDatabase, aRepoName, Collections.<Service<Resource<?>, ValidationResult>>emptyList());
  }
  
  public IRepoHandler(Context<Package> aDatabase, String aRepoName, List<Service<Resource<?>, ValidationResult>> postValidators) {
	  this(aDatabase.forName(aRepoName), postValidators);
  }
  
  public IRepoHandler(Package repository, List<Service<Resource<?>, ValidationResult>> postValidators)
  {
    theRepo = repository;
    this.postValidators = postValidators;
    theSupportedTypes = new HashMap<>();
    theSupportedTypes.put("json", "application/json");
    theSupportedTypes.put("txt", "application/json");
    theSupportedTypes.put("xml", "application/xml");
  }

  @Override
  public Class<? extends HttpRequest> getArgumentClass()
  {
    return HttpRequest.class;
  }

  protected String extFrom(String aName)
  {
    int start = aName.lastIndexOf('-');
    start = aName.indexOf('.', start);
    if (start < 0)
    {
      return null;
    }
    return aName.substring(++start);
  }

  protected String kindFrom(String aName)
  {
    int start = aName.lastIndexOf('/');
    // If there was no '/', no problem, we just start at 0:
    aName = aName.substring(++start);
    int idx = aName.indexOf('-');
    if (idx < 0)
    {
      return aName;
    }
    return aName.substring(0, idx);
  }

  protected String labelFrom(String aName)
  {
    int start = aName.lastIndexOf('/');
    // If there was no '/', no problem, we just start at 0:
    aName = aName.substring(++start);
    start = aName.indexOf('-');
    if (start < 0)
    {
      return null;
    }
    int end = aName.lastIndexOf('.');
    if (end < 0)
    {
      end = aName.length();
    }
    return aName.substring(++start, end);
  }

  @Override
  protected void onExecute(Action aAction, HttpRequest aRequest)
      throws Exception
  {
	  try {
        if (aRequest.getHttpRequest().getMethod().equals("GET"))
        {
          onGet(aAction, aRequest);
        }
        else
        {
          onPost(aAction, aRequest);
        }
	  } catch (Exception e) {
		  aRequest.getHttpResponse().sendError(400);
		  throw e;
	  }
  }

  protected void onGet(Action aAction, HttpRequest aRequest) throws IOException
  {
    String path = aRequest.getPath();
    String kind = kindFrom(path);
    String label = labelFrom(path);
    String ext = extFrom(path);
    String mediaType = theSupportedTypes.get(ext);
    if (mediaType == null)
    {
      aRequest.getHttpResponse().sendError(404);
      return;
    }
    Transaction tx = theRepo.connect(aAction.getActor());
    Resource<?> person = tx.retrieve(kind, label);
    person.send(mediaType, aRequest.getWriter());
  }

  protected void onPost(Action aAction, HttpRequest aRequest)
      throws IOException
  {
    Transaction tx = theRepo.connect(aAction.getActor());
    Reader r = aRequest.getHttpRequest().getReader();
    Resource<?> res = tx.receive("application/json", r);
    
    boolean isValid = true;
    ValidationResult firstFailure = null;
    for (Service<Resource<?>, ValidationResult> validator : postValidators) {
      ValidationResult result = validator.service(aAction, res);
      if (result.isValid()) { continue; }
      firstFailure = result;
      isValid = false;
      break;
    }
    
    if (isValid) {
      tx.post();
      res.send("application/json", aRequest.getHttpResponse().getWriter());
    } else {
    	aRequest.getHttpResponse().sendError(firstFailure.getCode(), firstFailure.getMessage());
    }
    
    // aRequest.getHttpResponse().sendRedirect(
    // res.id().kind().getName() + '-' + res.id().label() + ".json");
  }
}
