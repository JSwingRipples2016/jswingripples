/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.incha.core.jswingripples.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class LRUMap<A, B> extends LinkedHashMap<A, B> {

	private static final long serialVersionUID= 1L;
	private final int fMaxSize;

	public LRUMap(final int maxSize) {
		super(maxSize, 0.75f, true);
		fMaxSize= maxSize;
	}

	@Override
    protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
		return size() > fMaxSize;
	}
}
