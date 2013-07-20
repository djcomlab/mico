/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import uk.ac.rdg.acet.mico.comms.CommsService;
import java.util.Hashtable;
import java.util.Random;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.platform.ConfigurationFactory;
import net.jxta.util.java.net.URI;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

/**
 *
 * @author David
 */
public class Platform {

    private static Platform  p = new Platform();
    private Hashtable serviceRegistry = new Hashtable();
    private String peerName = null;
    private String peerID = null;
    private String superPeerURL = null;
    private static final Logger log = LoggerFactory.getLogger(Platform.class);
    private String[] startOpts = null;
    private static final Random random = new Random();

    public void startNetwork(long slotTime, int messagingMode, int backoffMode) throws Exception {
        startOpts = new String[3];
        startOpts[0] = Long.toString(slotTime);
        startOpts[1] = Integer.toString(messagingMode);
        startOpts[2] = Integer.toString(backoffMode);
        startNetwork();
    }

    public static Random getRandom() {
        return random;
    }


    public void stopNetwork() {
        SimpleMessagingService simpleMessagingService = (SimpleMessagingService)serviceRegistry.get(SimpleMessagingService.class.getName());
        simpleMessagingService.stopApp();        
    }

    public void startNetwork() throws Exception {
        org.apache.log4j.Logger.setDebug(false);
        SimpleMessagingService simpleMessagingService = new SimpleMessagingService();
        serviceRegistry.put(SimpleMessagingService.class.getName(), simpleMessagingService);
        // FIXME the url has port numbers appended on next line
        HttpConnection httpConnection = (HttpConnection)Connector.open("http://" + this.superPeerURL + ":8080/MicoSuperPeer/SuperPeerServlet");
        String superPeerID = null;
        if (httpConnection.getResponseCode() == HttpConnection.HTTP_OK) {
            InputStream inputStream = httpConnection.openInputStream();
            ByteArrayOutputStream bytestream =
                  new ByteArrayOutputStream();
            int ch;
            while ((ch = inputStream.read()) != -1)
            {
              bytestream.write(ch);
            }
            superPeerID = new String(bytestream.toByteArray()).trim();
            log.debug("Peer ID is " + superPeerID);
        } else {
            log.fatal("Could not instantiate platform; HTTP response not OK from super-peer");
            throw new Exception("Could not instantiate platform; HTTP response not OK from super-peer");
        }
        if (superPeerID!=null) {
            ConfigurationFactory.setName(this.peerName);
            URI relayRendezvousUri = new URI("tcp://" + this.superPeerURL + ":9701"); // default is local
            ConfigurationFactory.addSeedRelay(relayRendezvousUri);
            PeerID rdvPeerID = (PeerID) IDFactory.fromURI(new URI(superPeerID));
            ConfigurationFactory.addSeedRendezvousID(rdvPeerID);
            ConfigurationFactory.addSeedRendezvous(relayRendezvousUri);
            ConfigurationFactory.setTCPPortRange(9000, 9100);
//            ConfigurationFactory.setTCPPortRange(9101, 9200);
            PeerGroup netPeerGroup = PeerGroupFactory.newNetPeerGroup();
            peerID = netPeerGroup.getPeerID().toURI().toString();
            simpleMessagingService.init(netPeerGroup, netPeerGroup.getPeerID(), null);
        } else {
            log.fatal("Could not instantiate platform; no super-peer peer ID");
            throw new Exception("Could not instantiate platform; no super-peer peer ID");
        }
        int i = simpleMessagingService.startApp(startOpts);
        if (i == PeerGroup.START_OK) {
            // add and start other services
            serviceRegistry.put(CommsService.class.getName(), new CommsService());
//            serviceRegistry.put(IdentService.class.getName(), new IdentService());
//            serviceRegistry.put(ContentService.class.getName(), new ContentService());
        } else {
            log.fatal("Could not instantiate platform; could not start JXTA");
            throw new Exception("Could not instantiate platform; could not start JXTA");
        }
        startOpts = null;
    }

    public void setJxtaDebug(boolean debug) {
        org.apache.log4j.Logger.setDebug(debug);
    }

    public static String getPeerID() {
        return p.peerID;
    }

    public static void setPeerName(String peerName) {
        p.peerName = peerName;
    }

    public static void setSuperPeerURL(String superPeerURL) {
        p.superPeerURL = superPeerURL;
    }

    public static Platform getInstance() {
        return p;
    }
    
    public Service getService(String className) {
        return (Service)serviceRegistry.get(className); // returns null if not found
    }

    public void addService(Service service) throws Exception {
        if (serviceRegistry.get(service.getClass().getName())!=null) {
            throw new Exception("Service exists");
        } else {
            serviceRegistry.put(service.getClass().getName(), service);
        }
    }

}
