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

package org.hachreak.projects.networkcodingsip2peer.msg;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;

import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.utils.EncapsulatedEncodedFragment;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class EncodedFragmentPublishMessage extends BasicMessage {

	public static final String MSG_FRAGMENT_PUBLISH = "fragment_publish";

	/**
	 * 
	 * @param fragment
	 */
	public EncodedFragmentPublishMessage(EncodedFragment fragment) {
		super(MSG_FRAGMENT_PUBLISH, new Payload(EncapsulatedEncodedFragment.encapsulate(fragment)));
	}
}
