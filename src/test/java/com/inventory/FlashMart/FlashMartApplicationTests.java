package com.inventory.FlashMart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FlashMartApplicationTests {

    @Test
    void testFlashSaleConcurrency() throws InterruptedException {
        int totalRequests = 10000;

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        CountDownLatch readyLatch = new CountDownLatch(totalRequests);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalRequests);

        AtomicInteger successfulPurchases = new AtomicInteger(0);
        AtomicInteger failedPurchases = new AtomicInteger(0);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/flash-sale/buy"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        System.out.println("Loading 10,000 concurrent users at the starting line...");

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();


                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        successfulPurchases.incrementAndGet();
                    } else {
                        failedPurchases.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedPurchases.incrementAndGet();

                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        System.out.println("All users ready. Firing the starting pistol!");

        long startTime = System.currentTimeMillis();
        startLatch.countDown(); // RELEASE THE HOUNDS!

        doneLatch.await();
        long endTime = System.currentTimeMillis();

        System.out.println("=========================================");
        System.out.println("Total Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("Successful Purchases: " + successfulPurchases.get());
        System.out.println("Failed Purchases: " + failedPurchases.get());
        System.out.println("=========================================");

        // The Ultimate Proof:
        Assertions.assertEquals(1000, successfulPurchases.get(), "Exactly 1000 users should get the phone");
        Assertions.assertEquals(9000, failedPurchases.get(), "Exactly 9000 users should be rejected");
    }
}