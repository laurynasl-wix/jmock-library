/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;

/**
 * Is the value the same object as another value?
 */
public class IsSame implements Constraint {
	private Object object;

	/**
	 * Creates a new instance of IsSame
	 * 
	 * @param o
	 *            The predicate evaluates to true only when the argument is
	 *            this object.
	 */
	public IsSame(Object object) {
		this.object = object;
	}

	public boolean eval(Object arg) {
		return arg == object;
	}

	public String toString() {
        String objectString = (object == null) ? "null" : "<" + object + ">";
		return "same("+objectString+")";
	}
}