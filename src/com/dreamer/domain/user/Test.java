package com.dreamer.domain.user;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangfei on 16/6/18.
 */
public class Test {

    ExecutorService executor = Executors.newCachedThreadPool();

    public void doTask() {
//        Path path = Paths.get("E:/dreamer/imgs/product");
        Path path = Paths.get("/usr/dreamer/imgs/product");
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
            for (Path file : paths) {
                for (int index = 0; index < 100; index++) {
                    executor.submit(() -> {
                        Instant startTime = Instant.now();
                        try {
                            byte[] bts = Files.readAllBytes(file);
                            Long readMillis = Duration.between(startTime, Instant.now()).toMillis();
                            System.out.println("线程:{} 读取图片{}字节,耗时:{}毫秒"+Thread.currentThread()+"--"+bts.length+"--"+readMillis);
                            bts = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            executor.shutdown();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    public void tee(){
        try {
            System.out.println("执行了");
            System.out.println(1/0);
        } catch (Exception e) {
            System.out.println("异常了");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//       new Test().doTask();

        new Test().tee();
    }

}
