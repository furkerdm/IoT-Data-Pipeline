import org.eclipse.paho.client.mqttv3.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class IoTDataPipeline {
    public static void main(String[] args) throws MqttException {
        // --- AYARLAR ---
        String broker = "tcp://broker.hivemq.com:1883";
        String topic = "proje/sensor";

        // AWS & InfluxDB Bilgilerini Buraya Gir
        String kinesisStream = "IotDataStream";
        String influxToken = "uIG0LsUs4kVv59SQqbPLydHyhEY7qH1_hhaSUqu5pfylyVcxYCMFlSxUWP4etzX4GPvqhXTP4rpSKDPp5gK7WQ==";
        String influxBucket = "project-2";
        String influxOrg = "IotStream";

        // 1. MQTT İstemcisi Kurulumu
        MqttClient mqttClient = new MqttClient(broker, MqttClient.generateClientId());
        mqttClient.connect();

        // 2. AWS Kinesis İstemcisi Kurulumu
        AwsBasicCredentials creds = AwsBasicCredentials.create("ACCESS_KEY_ID", "SECRET_ACCES_KEY");

        KinesisClient kinesisClient = KinesisClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();

        // 3. InfluxDB İstemcisi Kurulumu
        InfluxDBClient influxClient = InfluxDBClientFactory.create("https://eu-central-1-1.aws.cloud2.influxdata.com", influxToken.toCharArray(), influxOrg, influxBucket);

        System.out.println("Sistem Aktif: Veri akışı başlıyor...");

        // SİMÜLASYON DÖNGÜSÜ
        for (int i = 0; i < 10; i++) {
            double temp = Math.round(ThreadLocalRandom.current().nextDouble(23.02, 24.24) * 100.0) / 100.0;
            String payload = String.format("{\"sicaklik\": %.2f, \"cihaz\": \"sensor-1\"}", temp);

            // ADIM A: MQTT'ye Gönder
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));

            // ADIM B: AWS Kinesis'e Gönder
            kinesisClient.putRecord(PutRecordRequest.builder()
                    .streamName(kinesisStream)
                    .partitionKey("partition-1")
                    .data(SdkBytes.fromUtf8String(payload)).build());

            // ADIM C: InfluxDB'ye Yaz
            influxClient.getWriteApiBlocking().writePoint(Point.measurement("sensor_verisi")
                    .addTag("cihaz", "sensor-1").addField("sicaklik", temp)
                    .time(Instant.now(), WritePrecision.NS));

            System.out.println("Gönderildi: " + payload);
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        }

        mqttClient.disconnect();
        kinesisClient.close();
        influxClient.close();
        System.out.println("Akış tamamlandı.");
    }
}