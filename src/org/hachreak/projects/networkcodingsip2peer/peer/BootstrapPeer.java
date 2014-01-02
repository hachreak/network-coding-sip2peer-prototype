/**
 * Copyright (C) 2013 Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 * This source code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.hachreak.projects.networkcodingsip2peer.peer;

import java.io.IOException;

import org.hachreak.projects.networkcodingsip2peer.behaviour.BootstrapServerBehaviour;
import org.hachreak.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class BootstrapPeer extends SimplePeer {

	public BootstrapPeer(String pathConfig/* , String key */) throws IOException, NoBootstrapConfiguredException {
		// echo -n
		// it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.BootstrapPeer|md5sum
		super(pathConfig, "bootstrap");// "b7d513f84ad6813570082c2e8971e894");
		
		// add Behaviour
		new BootstrapServerBehaviour(this);
	}
}
