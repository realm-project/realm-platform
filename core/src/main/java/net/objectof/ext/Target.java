package net.objectof.ext;

import net.objectof.Receiver;

/**
 * In general terms, a <em>target</em> is logical system/model. Targets can be
 * languages, frameworks, etc. Target provides methods via the Receiver
 * interface to convert from canonical models to target models. Targets are
 * currently limited to a serial text model such as a source or configuration
 * file, however they can be more generalized by retaining the generic type in
 * Appender.
 * <p>
 * The methods accessed via Receiver.perform() are expected to be generated
 * through template definitions in the general case. If required, more complex
 * manually coded methods can be defined for special cases. The set of members
 * available through Receiver is loosely defined: There is currently no specific
 * interface and the contract is purely through convention on the member
 * selectors.
 *
 * @author jdh
 *
 */
public interface Target extends Appender<Object>
{
  public String getMediaType();

  public Object getProperty(String aUniqueName);

  public Receiver getTranscoder();
}
