import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;


public class InfluxDBPublisher {
    public static void main(String[] args) {
        String token = "uIG0LsUs4kVv59SQqbPLydHyhEY7qH1_hhaSUqu5pfylyVcxYCMFlSxUWP4etzX4GPvqhXTP4rpSKDPp5gK7WQ==";
        String bucket = "project-2";
        String org = "IotStream";
        String url = "https://eu-central-1-1.aws.cloud2.influxdata.com"; // Kendi URL'inle kontrol et

        try (InfluxDBClient client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket)) {
            double rastgeleSicaklik = ThreadLocalRandom.current().nextDouble(23.02, 24.24);
            double yuvarlanmisSicaklik = Math.round(rastgeleSicaklik * 100.0) / 100.0;

            // Örnek bir veri noktası (Point) oluşturuyoruz
            Point point = Point.measurement("sensor_verisi")
                    .addTag("cihaz", "sensor-1")
                    .addField("sicaklik", yuvarlanmisSicaklik)
                    .time(Instant.now(), WritePrecision.NS);

            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writePoint(point);

            System.out.println("Veri InfluxDB Cloud'a başarıyla yazıldı!");
        } catch (Exception e) {
            System.err.println("InfluxDB hatası: " + e.getMessage());
        }
    }
}