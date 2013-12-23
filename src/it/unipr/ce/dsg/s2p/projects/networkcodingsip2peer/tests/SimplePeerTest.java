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

package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.assertTrue;
import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.PeerListMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.BootstrapPeer;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.SimplePeer;
import it.unipr.ce.dsg.s2p.sip.Address;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class SimplePeerTest {

	private String configFile1 = "config/simplepeertest1.cfg";
	private String configFile2 = "config/simplepeertest2.cfg";
	private String configFile3 = "config/simplepeertest3.cfg";
	private String configFile4 = "config/simplepeertest4.cfg";
	private String configFile5 = "config/simplepeertest5.cfg";
	private String configFile6 = "config/simplepeertest6.cfg";
	private String configFile7 = "config/simplepeertest7.cfg";
	private String configFile8 = "config/simplepeertest8.cfg";

	private SimplePeer p1, p2, p3, p4;

	private String configFileBootstrap = "config/bootstrappeertest.cfg";

	boolean finishTestSimplePeer = false;
	boolean finishTestReqNumPeer = false;
	boolean finishTestRefillPeerList = false;

	/**
	 * @throws java.lang.Exception
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("bootstrap start...");
		BootstrapPeer p = new BootstrapPeer(configFileBootstrap);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.SimplePeer#SimplePeer(java.lang.String)}
	 * .
	 * 
	 */
	@Test
	public void testSimplePeer() {

		try {
			final String k1 = "3c200eb1d5cf4a4de34a94d984350f84";
			final String k2 = "5d41402abc4b2a76b9719d911017c592";
			final String k3 = "f9fc0ba7be2440dae46455e8ae688f35";
			p1 = new SimplePeer(configFile1, k1) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.
				 * SimplePeer
				 * #onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org.json.JSONObject,
				 * it.unipr.ce.dsg.s2p.sip.Address)
				 */
				/**
				 * @Override
				 */
				protected void onReceivedJSONMsg(JSONObject jsonMsg,
						Address sender) {
					// TODO Auto-generated method stub
					super.onReceivedJSONMsg(jsonMsg, sender);
					System.out.println("[PLM  ]\n" + peerList + "\n[/PLM]");

					try {
						p2 = new SimplePeer(configFile2, k2) {

							/*
							 * (non-Javadoc)
							 * 
							 * @see
							 * it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer
							 * .
							 * peer.SimplePeer#onReceivedJSONMsg(it.unipr.ce.dsg
							 * .s2p.org.json.JSONObject,
							 * it.unipr.ce.dsg.s2p.sip.Address)
							 */
							/**
							 * @Override
							 */
							protected void onReceivedJSONMsg(
									JSONObject jsonMsg, Address sender) {
								// TODO Auto-generated method stub
								super.onReceivedJSONMsg(jsonMsg, sender);
								System.out.println("[PLM  ]\n" + peerList
										+ "\n[/PLM]");

								try {
									p3 = new SimplePeer(configFile3, k3) {

										/*
										 * (non-Javadoc)
										 * 
										 * @see it.unipr.ce.dsg.s2p.projects.
										 * networkcodingsip2peer
										 * .peer.SimplePeer#
										 * onReceivedJSONMsg(it.
										 * unipr.ce.dsg.s2p.org.json.JSONObject,
										 * it.unipr.ce.dsg.s2p.sip.Address)
										 */
										/**
										 * @Override
										 */
										protected void onReceivedJSONMsg(
												JSONObject jsonMsg,
												Address sender) {
											// TODO Auto-generated method stub
											super.onReceivedJSONMsg(jsonMsg,
													sender);
											System.out.println("[PLM  ]\n"
													+ peerList + "\n[/PLM]");
											assertTrue (peerList.containsKey(k1) && peerList
													.containsKey(k2));
											finishTestSimplePeer = true;
										}

									};
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoBootstrapConfiguredException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						};
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoBootstrapConfiguredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBootstrapConfiguredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (!finishTestSimplePeer) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Test method for
	 * {@link it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.SimplePeer#SimplePeer(java.lang.String)}
	 * .
	 */
	@Test
	public void testReqNumPeer() {

		try {
			final String k5 = "e3d704f3542b44a621ebed70dc0efe13";
			final String k6 = "4cfad7076129962ee70c36839a1e3e15";
			final String k7 = "b04083e53e242626595e2b8ea327e525";
			final String k4 = "016023e75bac054cbb95594c0ae6ac1a";

			new SimplePeer(configFile5, k5) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.
				 * SimplePeer
				 * #onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org.json.JSONObject,
				 * it.unipr.ce.dsg.s2p.sip.Address)
				 */
				/**
				 * @Override
				 */
				protected void onReceivedJSONMsg(JSONObject jsonMsg,
						Address sender) {
					// TODO Auto-generated method stub
					super.onReceivedJSONMsg(jsonMsg, sender);
					System.out.println("[PLM  ]\n" + peerList + "\n[/PLM]");

					try {
						new SimplePeer(configFile6, k6) {

							/*
							 * (non-Javadoc)
							 * 
							 * @see
							 * it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer
							 * .
							 * peer.SimplePeer#onReceivedJSONMsg(it.unipr.ce.dsg
							 * .s2p.org.json.JSONObject,
							 * it.unipr.ce.dsg.s2p.sip.Address)
							 */
							/**
							 * @Override
							 */
							protected void onReceivedJSONMsg(
									JSONObject jsonMsg, Address sender) {
								// TODO Auto-generated method stub
								super.onReceivedJSONMsg(jsonMsg, sender);
								System.out.println("[PLM  ]\n" + peerList
										+ "\n[/PLM]");

								try {
									new SimplePeer(configFile7, k7) {

										/*
										 * (non-Javadoc)
										 * 
										 * @see it.unipr.ce.dsg.s2p.projects.
										 * networkcodingsip2peer
										 * .peer.SimplePeer#
										 * onReceivedJSONMsg(it.
										 * unipr.ce.dsg.s2p.org.json.JSONObject,
										 * it.unipr.ce.dsg.s2p.sip.Address)
										 */
										/**
										 * @Override
										 */
										protected void onReceivedJSONMsg(
												JSONObject jsonMsg,
												Address sender) {
											// TODO Auto-generated method stub
											super.onReceivedJSONMsg(jsonMsg,
													sender);
											System.out.println("[PLM  ]\n"
													+ peerList + "\n[/PLM]");
											// assert(peerList.containsKey(k1)
											// && peerList.containsKey(k2));
											try {
												p4 = new SimplePeer(
														configFile4, k4) {

													/**
													 * @Override
													 */
													protected void onReceivedJSONMsg(
															JSONObject jsonMsg,
															Address sender) {
														// TODO Auto-generated
														// method stub
														super.onReceivedJSONMsg(
																jsonMsg, sender);
														System.out
																.println("[PLM p4 ]\n"
																		+ peerList
																		+ "\n[/PLM]");
														// assert(peerList.containsKey(k1)
														// &&
														// peerList.containsKey(k2));
														finishTestReqNumPeer = true;
														assertTrue (this.peerList
																.size() == Integer
																.parseInt(this.configFile
																		.getProperty(SimplePeer.REQ_NPEER)));
													}

												};
											} catch (
													IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (NoBootstrapConfiguredException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}

									};
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoBootstrapConfiguredException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						};
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoBootstrapConfiguredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBootstrapConfiguredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (!finishTestReqNumPeer) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Test method for
	 * {@link it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.SimplePeer#SimplePeer(java.lang.String)}
	 * .
	 */
	@Test
	public void testRefillPeerList() {
		final String k8 = "5e40d09fa0529781afd1254a42913847";
		try {
			SimplePeer p = new SimplePeer(configFile8, k8) {
				private boolean joinExecuted = false;

				/*
				 * (non-Javadoc)
				 * 
				 * @see it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.
				 * SimplePeer
				 * #onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org.json.JSONObject,
				 * it.unipr.ce.dsg.s2p.sip.Address)
				 */
				/**
				 * @Override
				 */
				protected void onReceivedJSONMsg(JSONObject jsonMsg,
						Address sender) {
					// TODO Auto-generated method stub
					super.onReceivedJSONMsg(jsonMsg, sender);

					String type;
					try {
						type = (String) jsonMsg.get("type");
						System.out.println("type: " + type);
						if (type.equals(PeerListMessage.MSG_PEER_LIST)) {
							if (!joinExecuted) {
								getPeerList().remove(
										getPeerList().keys().nextElement());
								// System.out.println(peerList);
								getPeerList().remove(
										getPeerList().keys().nextElement());
								
								try {
									refillPeerList();
								} catch (NoBootstrapConfiguredException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("PeerList size: "
										+ getPeerList().size());
								joinExecuted = true;
							} else {
								System.out.println("PeerList size after: "
										+ getPeerList().size());
								finishTestRefillPeerList = true;
								assertTrue(getPeerList().size() == 3);
							}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			};

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoBootstrapConfiguredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!finishTestRefillPeerList) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	@AfterClass
//	public static void endTest() {
//		// System.out.println("[End Tests] .... ");
//		//
//		// while(true){
//		// try {
//		// Thread.sleep(1000);
//		// } catch (InterruptedException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// }
//	}

	// @Before
	// public void tearsDownAfterClass(){
	// while(true){
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

}
