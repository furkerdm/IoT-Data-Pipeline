import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import java.util.concurrent.ThreadLocalRandom;


public class KinesisPublisher {
    public static void main(String[] args) {
        String streamName = "IotDataStream";
        String partitionKey = "sensor-1";
        double rastgeleSicaklik = ThreadLocalRandom.current().nextDouble(23.02, 24.24);
        String formatliSicaklik = String.format("%.2f", rastgeleSicaklik);

        String data = String.format("{\"sicaklik\": %s, \"cihaz\": \"sensor-1\"}", formatliSicaklik);

        AwsBasicCredentials creds = AwsBasicCredentials.create("ACCESS_KEY", "SECRET_ACCESS_KEY");

        KinesisClient kinesisClient = KinesisClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
        SdkBytes bytes = SdkBytes.fromUtf8String(data);

        PutRecordRequest request = PutRecordRequest.builder()
                .streamName(streamName)
                .partitionKey(partitionKey)
                .data(bytes)
                .build();

        try {
            kinesisClient.putRecord(request);
            System.out.println("Veri Kinesis'e başarıyla gönderildi: " + data);
        } catch (Exception e) {
            System.err.println("Kinesis'e veri gönderilirken hata oluştu: " + e.getMessage());
        } finally {
            kinesisClient.close();
        }
    }
}