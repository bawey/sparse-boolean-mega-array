package com.github.bawey;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class Benchmark {

    private static long getMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static void main(String[] args) {
        log.info("Starting out with {} kB of memory used", getMem() / 1024);
        SparseBooleanMegaArray store = new SparseBooleanMegaArray(Integer.MAX_VALUE, 0);
        log.info("Memory used after allocation: {} kB", getMem() / 1024);

        long nanoTimeStart = System.nanoTime();
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            assert (!store.readValue(i));
            store.setValue(i, true);
            assert (store.readValue(i));
        }
        long nanoTimeEnd = System.nanoTime();
        log.info("Benchmark completed in {} ms", (nanoTimeEnd - nanoTimeStart) / 1000_000);

        Set<Integer> set = new HashSet<>();
        nanoTimeStart = System.nanoTime();
        for (int i = 0; i < Integer.MAX_VALUE >> 5; ++i) {
            assert (!set.contains(i));
            set.add(i);
            assert (set.contains(i));
        }
        nanoTimeEnd = System.nanoTime();
        log.info("Set benchmark completed in {} ms, used memory is now at {} kB",
                (nanoTimeEnd - nanoTimeStart) / 1000_000, getMem() / 1024);
    }
}
