package com.github.bawey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SparseBooleanMegaArrayTest {

    @Test
    void minimalTest() {
        var store = new SparseBooleanMegaArray(1, 0);
        assertThat(store.readValue(0)).isFalse();
        store.setValue(0, true);
        assertThat(store.readValue(0)).isTrue();
    }

    @Test
    void smallTest() {
        var store = new SparseBooleanMegaArray(0x80, 0x80);
        Stream.iterate(0x80, i -> i + 1).limit(0x80).forEach(i -> {
            assertThat(store.readValue(i)).isFalse();
            store.setValue(i, true);
        });
        Stream.iterate(0xff, i -> i - 1).limit(0x80).forEach(i -> {
            assertThat(store.readValue(i)).isTrue();
        });
    }

    @Test
    void minimalRewriteTest() {
        var store = new SparseBooleanMegaArray(2, 0);
        store.setValue(1, true);
        store.setValue(0, true);
        assertThat(store.readValue(1)).isTrue();
        store.setValue(1, false);
        assertThat(store.readValue(0)).isTrue();
    }

    @Test
    void simpleRiggedTest() {
        var store = new SparseBooleanMegaArray(1_000_000, 0);

        store.setValue(999_999, true);
        store.setValue(999_998, false);
        store.setValue(100_000, true);

        Assertions.assertThat(store.readValue(100_000)).isTrue();
        Assertions.assertThat(store.readValue(999_998)).isFalse();
        Assertions.assertThat(store.readValue(999_999)).isTrue();

        store.setValue(999_999, false);
        Assertions.assertThat(store.readValue(999_999)).isFalse();

    }

    @RepeatedTest(0x40)
    void largeRandomRewriteTest() {
        final int startingKey = 789;
        final int capacity = 4096;
        var store = new SparseBooleanMegaArray(capacity, startingKey);
        List<Integer> cons = new ArrayList<>(Stream.iterate(startingKey, i -> i + 1).limit(capacity).collect(Collectors.toList()));
        Collections.shuffle(cons);

        for (int con : cons) {
            Assertions.assertThat(store.readValue(con)).isFalse();
            store.setValue(con, true);
        }
        Collections.shuffle(cons);

        for (int con : cons) {
            Assertions.assertThat(store.readValue(con)).withFailMessage("Wrong value for " + con).isTrue();
            store.setValue(con, false);
        }
        Collections.shuffle(cons);

        for (int con : cons) {
            Assertions.assertThat(store.readValue(con)).isFalse();
        }
    }

    @Test
    @Disabled
    void testIllegalInputs() {
        //TODO: test some wrong inputs and such
    }

}