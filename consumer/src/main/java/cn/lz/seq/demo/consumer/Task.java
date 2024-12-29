package cn.lz.seq.demo.consumer;

import cn.lz.seq.api.SeqService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

@Component
public class Task implements CommandLineRunner {

    @DubboReference
    private SeqService seqService;

    @Override
    public void run(String... args) throws Exception {
        /*
        dubbo
         */
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(new Date() + " Dubbo: Receive result ======> " + seqService.getSeq("myshop"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
        /*
        http
         */
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    HttpClient httpClient = HttpClient.newHttpClient();
                    // 创建HttpRequest
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:11111/seq?token=video")) // 请求的URL
                            .GET() // HTTP GET 方法
                            .build();
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    String res = response.body();
                    System.out.println(new Date() + " Http: Receive result ======> " + res);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}
