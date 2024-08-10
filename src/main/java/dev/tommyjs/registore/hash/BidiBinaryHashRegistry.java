//package dev.tommyjs.registore.hash;
//
//import dev.tommyjs.registore.encoder.RegistryEncoder;
//import dev.tommyjs.registore.impl.AbstractBidiRegistry;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class BidiBinaryHashRegistry<K, V> extends AbstractBidiRegistry<K, V> {
//
//    private BidiBinaryHashRegistry(@NotNull LazyLoadedHashRegistry<K, V> forwards, @NotNull LazyLoadedHashRegistry<V, K> backwards) {
//        super(forwards, backwards);
//    }
//
//    public BidiBinaryHashRegistry(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
//                                  byte[] forwardTable, byte[] backwardTable, byte[] presentSet, int realSize) {
//        this(
//            new LazyLoadedHashRegistry<>(keyEncoder, valueEncoder, forwardTable, presentSet, realSize),
//            new LazyLoadedHashRegistry<>(valueEncoder, keyEncoder, backwardTable, presentSet, realSize)
//        );
//    }
//
//    public BidiBinaryHashRegistry(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
//                                  @NotNull Map<K, V> map, float expandFactor) {
//        this(
//            new LazyLoadedHashRegistry<>(keyEncoder, valueEncoder, map, expandFactor),
//            new LazyLoadedHashRegistry<>(valueEncoder, keyEncoder, map.entrySet().stream().collect(
//                Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)
//            ), expandFactor)
//        );
//    }
//
//    public BidiBinaryHashRegistry(@NotNull RegistryEncoder<K> keyEncoder, @NotNull RegistryEncoder<V> valueEncoder,
//                                  @NotNull Map<K, V> map) {
//        this(
//            new LazyLoadedHashRegistry<>(keyEncoder, valueEncoder, map),
//            new LazyLoadedHashRegistry<>(valueEncoder, keyEncoder, map.entrySet().stream().collect(
//                Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)
//            ))
//        );
//    }
//
//}
