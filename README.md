# 🚀 IoT Veri Boru Hattı (Data Pipeline) Projesi

Bu proje, bir IoT sensöründen çıkan verilerin bulut ortamına taşınmasını, kuyruklanmasını ve analiz edilmesini sağlayan uçtan uca bir veri hattı simülasyonudur. Ankara Üniversitesi Bilgisayar Mühendisliği ders projesi kapsamında geliştirilmiştir.

## 🛠 Kullanılan Teknolojiler
- **Java 21**: Sistem simülasyonu ve veri üretimi.
- **MQTT (HiveMQ)**: Cihazlar arası hafif veri iletim protokolü.
- **AWS Kinesis**: Bulut tabanlı veri akış (stream) yönetimi.
- **InfluxDB Cloud**: Zaman serisi veritabanı (TSDB) ve görselleştirme.
- **Maven**: Bağımlılık yönetimi ve proje yapılandırması.

## 📐 Sistem Mimarisi
Veri yolculuğu şu aşamalardan oluşmaktadır:
1. **Üretim:** Java simülatörü rastgele sıcaklık verisi üretir.
2. **Yayın:** Veri MQTT protokolü ile HiveMQ Broker'a gönderilir.
3. **Akış:** Broker'dan geçen veri AWS Kinesis Data Streams üzerine yazılır.
4. **Depolama:** Nihai veri InfluxDB Cloud üzerinde zaman damgalı olarak kaydedilir.



## 🚀 Kurulum ve Çalıştırma

### 1. Bağımlılıklar
Projenin çalışması için bilgisayarınızda Maven yüklü olmalıdır. Terminalden şu komutu çalıştırarak gerekli kütüphaneleri indirin:
```bash
mvn clean install

### 2. Yapılandırma
IoTDataPipeline.java dosyası içerisindeki şu alanları kendi kimlik bilgilerinizle doldurmanız gerekmektedir:

AWS: Access Key ve Secret Key

InfluxDB: Token, Organization ve Bucket bilgileri

### 3. Çalıştırma Sırası
Doğrulama: Önce MqttReceiver.java sınıfını çalıştırarak broker'ı dinlemeye başlayın.

Ana Akış: Ardından IoTDataPipeline.java sınıfını çalıştırarak veri üretimini ve iletimini başlatın.

### Veri Doğruluğu ve Hassasiyet
Sistem, verilerin analiz edilebilir olması için şu teknik detayları içerir:

Veriler String yerine double tipinde gönderilir.

InfluxDB uyumluluğu için veriler virgülden sonra 2 basamağa yuvarlanır (Math.round).

Her veri paketi cihaz kimliği (tag) ve zaman damgası (timestamp) ile etiketlenir.
