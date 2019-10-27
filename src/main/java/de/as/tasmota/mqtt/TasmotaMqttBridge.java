package de.as.tasmota.mqtt;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TasmotaMqttBridge {

    private MqttClient sampleClient;

    public TasmotaMqttBridge(String broker) {
	// tcp://192.168.0.15:1883
	this.init(broker);
    }

    private void init(String broker) {
	String clientId = "TasmotaHelper_" + (new Random(System.nanoTime())).nextInt(100);
	try {
	    sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
	    connect();
	} catch (MqttException e) {
	    System.out.println("reason " + e.getReasonCode());
	    System.out.println("msg " + e.getMessage());
	    System.out.println("loc " + e.getLocalizedMessage());
	    System.out.println("cause " + e.getCause());
	    System.out.println("excep " + e);
	    throw new RuntimeException(e); // TODO
	}
    }

    private void connect() throws MqttSecurityException, MqttException {
	MqttConnectOptions connOpts = new MqttConnectOptions();
	connOpts.setCleanSession(true);
	sampleClient.connect(connOpts);
    }

    public void destroy() {
	try {
	    sampleClient.disconnect();
	} catch (MqttException e) {
	    System.out.println("reason " + e.getReasonCode());
	    System.out.println("msg " + e.getMessage());
	    System.out.println("loc " + e.getLocalizedMessage());
	    System.out.println("cause " + e.getCause());
	    System.out.println("excep " + e);
	    throw new RuntimeException(e); // TODO
	}
    }

    public void publish(String topic, String message, int qos) {
	publish(topic, message.getBytes(), qos);
    }

    public void publish(String topic, byte[] content, int qos) {
	MqttMessage message = new MqttMessage(content);
	message.setQos(qos);
	try {
	    sampleClient.publish(topic, message);
//	} catch (MqttPersistenceException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
	} catch (MqttException e) {
	    System.out.println("reason " + e.getReasonCode());
	    System.out.println("msg " + e.getMessage());
	    System.out.println("loc " + e.getLocalizedMessage());
	    System.out.println("cause " + e.getCause());
	    System.out.println("excep " + e);
	    throw new RuntimeException(e); // TODO
	}
    }

    public static void main(String[] a) {
	TasmotaMqttBridge inst = new TasmotaMqttBridge("tcp://192.168.0.15:1883");
	inst.publish("dev/tasmota/sonoff_H801_nn01/cmnd/power1", "toggle", 2);
    }
}
