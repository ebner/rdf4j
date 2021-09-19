/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.helpers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailConnectionListener;

/**
 * Abstract Class offering base functionality for SailConnection implementations.
 *
 * @author Arjohn Kampman
 * @author jeen
 */
public abstract class NotifyingSailConnectionBase extends AbstractSailConnection implements NotifyingSailConnection {

	// Use of a CopyOnWriteArrayList allows us to remove a listener from within a call to `listener.statementAdded(st)`
	// or `listener.statementRemoved(st)`
	private final List<SailConnectionListener> listeners = new CopyOnWriteArrayList<>();

	public NotifyingSailConnectionBase(AbstractSail sailBase) {
		super(sailBase);
	}

	@Override
	public void addConnectionListener(SailConnectionListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeConnectionListener(SailConnectionListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected boolean hasConnectionListeners() {
		synchronized (listeners) {
			return !listeners.isEmpty();
		}
	}

	protected void notifyStatementAdded(Statement st) {
		synchronized (listeners) {
			for (SailConnectionListener listener : listeners) {
				listener.statementAdded(st);
			}
		}
	}

	protected void notifyStatementRemoved(Statement st) {
		synchronized (listeners) {
			for (SailConnectionListener listener : listeners) {
				listener.statementRemoved(st);
			}
		}
	}
}
