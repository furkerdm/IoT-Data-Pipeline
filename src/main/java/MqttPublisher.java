import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.ThreadLocalRandom;

public class MqttPublisher {
    public static void main(String[] args) {
        double rastgeleSicaklik = ThreadLocalRandom.current().nextDouble(23.02, 24.24);
        String formatliSicaklik = String.format("%.2f", rastgeleSicaklik);
        String topic        = "proje/sensor";
        String content      = String.format("{\"sicaklik\": %s, \"cihaz\": \"sensor-1\"}", formatliSicaklik);
        int qos             = 1;
        String broker       = "tcp://broker.hivemq.com:1883";
        String clientId     = "Java_IoT_Istemcisi";

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Broker'a bağlanılıyor: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Bağlantı başarılı!");

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Mesaj iletildi: " + content);

            sampleClient.disconnect();
            System.out.println("Bağlantı kesildi.");
        } catch(MqttException me) {
            System.out.println("Hata Nedeni: " + me.getReasonCode());
            me.printStackTrace();
        }
    }
}