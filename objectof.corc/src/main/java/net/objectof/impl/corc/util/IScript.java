package net.objectof.impl.corc.util;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.IUtilHandler;

public class IScript extends IUtilHandler
{
  public static final ScriptEngine ENGINE = new ScriptEngineManager()
      .getEngineByName("JavaScript");
  private final String theScript;

  public IScript(Handler<Object> aForwarder, String aScript)
  {
    super(aForwarder);
    theScript = aScript;
  }

  @Override
  public boolean record()
  {
    return true;
  }

  @Override
  protected void onExecute(Action aRequest, Object aObject) throws Exception
  {
    Bindings b = ENGINE.createBindings();
    b.put("a", aObject);
    Object result = ENGINE.eval(theScript, b);
    chain(aRequest, result);
  }
}
