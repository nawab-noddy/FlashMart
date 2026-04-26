package com.inventory.FlashMart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class FlashMartApplicationTests {

    @Autowired
    private MockMvc mockMvc; // This acts like a programmatic Postman

    @Test
    void testFlashSaleConcurrency() throws InterruptedException {
        int totalRequests = 10000;

        // Create a thread pool of 500 workers to simulate massive concurrent traffic
        ExecutorService executorService = Executors.newFixedThreadPool(500);

        // Latches to synchronize our threads
        CountDownLatch readyLatch = new CountDownLatch(totalRequests);
        CountDownLatch startLatch = new CountDownLatch(1); // The starting pistol
        CountDownLatch doneLatch = new CountDownLatch(totalRequests);

        // Thread-safe counters
        AtomicInteger successfulPurchases = new AtomicInteger(0);
        AtomicInteger failedPurchases = new AtomicInteger(0);

        System.out.println("Loading 10,000 concurrent users at the starting line...");

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await(); // Wait here until the starting pistol is fired

                    // Hit your actual API endpoint
                    int statusCode = mockMvc.perform(post("/api/v1/flash-sale/buy"))
                            .andReturn().getResponse().getStatus();

                    if (statusCode == 200) {
                        successfulPurchases.incrementAndGet();
                    } else {
                        failedPurchases.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // Wait until all 10,000 tasks are loaded into the thread pool and waiting
        readyLatch.await();
        System.out.println("All users ready. Firing the starting pistol!");

        long startTime = System.currentTimeMillis();
        startLatch.countDown(); // RELEASE THE HOUNDS!

        doneLatch.await(); // Wait for all 10,000 requests to finish processing
        long endTime = System.currentTimeMillis();

        System.out.println("=========================================");
        System.out.println("Total Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("Successful Purchases: " + successfulPurchases.get());
        System.out.println("Failed Purchases: " + failedPurchases.get());
        System.out.println("=========================================");

        // The ultimate mathematical proof:
        Assertions.assertEquals(1000, successfulPurchases.get(), "Exactly 1000 users should get the phone");
        Assertions.assertEquals(9000, failedPurchases.get(), "Exactly 9000 users should be rejected");
    }
}