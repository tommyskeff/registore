package dev.tommyjs.registore;

import dev.tommyjs.registore.compat.MapCompat;
import dev.tommyjs.registore.hash.ConcurrentHashRegistry;
import dev.tommyjs.registore.hash.HashRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static dev.tommyjs.registore.util.TestUtil.*;

public class RegistryTest {

    @Test
    public void EqualityTest() {
        assert Objects.equals(createSample(), createSample());
    }

    @Test
    public void MapEqualityTest() {
        assert Objects.equals(new HashSet<>(createSample().entrySet()), new HashSet<>(MapCompat.convert(createSample()).entrySet()));
    }

    @Test
    public void CompatEqualityTest() {
        assert Objects.equals(createSample(), MapCompat.convert(MapCompat.convert(createSample())));
    }

    @Test
    public void SizeTest() {
        assert createSample().size() == 45;
    }

    @Test
    public void HasTest() {
        Registry<Integer, Integer> registry = createSample();
        assert registry.has(278);
        assert !registry.has(279);
    }

    @Test
    public void GetTest() {
        Registry<Integer, Integer> registry = createSample();
        assert Objects.equals(registry.get(278), 4);
        assert Objects.equals(registry.get(279), null);
    }

    @ParameterizedTest
    @MethodSource("basicTypes")
    public void SetTest(MutableRegistry<Integer, Integer> registry) {
        registry.set(5, 106);

        assert Objects.equals(registry.size(), 1);
        assert registry.has(5);
        assert Objects.equals(registry.get(5), 106);
    }

    @ParameterizedTest
    @MethodSource("basicTypes")
    public void ClearTest(MutableRegistry<Integer, Integer> registry) {
        registry.set(5, 106);
        registry.clear();

        assert registry.size() == 0;
        assert !registry.has(5);
    }

    @ParameterizedTest
    @MethodSource("allTypes")
    public void IntegrityTest(Registry<Integer, Integer> registry) {
        for (Map.Entry<Integer, Integer> entry : registry.entrySet()) {
            assert registry.has(entry.getKey());
            if (!Objects.equals(entry.getValue(), registry.get(entry.getKey()))) {
                System.out.println(registry);
                System.out.println(entry.getKey());
                System.out.println(entry.getValue() + " " + registry.get(entry.getKey()));
                System.out.println();
                return;
            }
        }
    }

    @Test
    public void KeySetTest() {
        assert Objects.equals(new HashSet<>(createSampleData().keySet()), new HashSet<>(createSample().keySet()));
    }

    @Test
    public void ValuesTest() {
        assert Objects.equals(new HashSet<>(createSampleData().values()), new HashSet<>(createSample().values()));
    }

    @Test
    public void EntrySetTest() {
        assert Objects.equals(new HashSet<>(createSampleData().entrySet()), new HashSet<>(createSample().entrySet()));
    }

    private static Stream<Arguments> basicTypes() {
        return Stream.of(
            new HashRegistry<>(),
            new ConcurrentHashRegistry<>()
        ).map(Arguments::of);
    }

    private static Stream<Arguments> allTypes() {
        return Stream.of(
            new HashRegistry<>(),
            new ConcurrentHashRegistry<>(),
            loadSample(false),
            loadSample(true)
        ).map(Arguments::of);
    }

}
