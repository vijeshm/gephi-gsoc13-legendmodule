package org.gephi.legend.inplaceeditor;

/**
 * an interface that enables call backs in the implementation of function
 * elements.
 *
 * When a complex logic needs to be executed on clicking a button on the inplace
 * editor, it can take the type FUNCTION. For implementing callbacks, the
 * anonymous click responder objects are expected implement this interface and
 * define it when registering. See ElementFunction for more details.
 *
 * @author mvvijesh
 */
public interface InplaceClickResponse {

    void performAction(InplaceEditor ipeditor);
}
