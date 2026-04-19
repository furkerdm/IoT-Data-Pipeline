import org.eclipse.paho.client.mqttv3.*;

public class MqttReceiver {
    public static void main(String[] args) throws MqttException {
        String broker = "tcp://broker.hivemq.com:1883";
        MqttClient client = new MqttClient(broker, MqttClient.generateClientId());

        client.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) { System.out.println("Bağlantı koptu!"); }

            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("[%s] Gelen Veri: %s".formatted(topic, new String(message.getPayload())));
            }

            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        client.connect();
        client.subscribe("proje/sensor"); // Pipeline kodundaki topic ile aynı olmalı
        System.out.println("Dinleniyor... (Veri bekleniyor)");
    }
}