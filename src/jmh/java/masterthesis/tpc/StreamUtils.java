package masterthesis.tpc;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamUtils {
    
    public static <T, U> Stream<ImmutablePair<T, U>> crossJoin(List<T> first, List<U> second) {
        return first.stream().flatMap(
                firstItem -> second.stream().map(secondItem -> new ImmutablePair<>(firstItem, secondItem))
        );
    }
    
    public static <T, U> Stream<ImmutablePair<T, U>> innerJoin(
            Collection<T> first,
            Collection<U> second,
            Predicate<ImmutablePair<T,U>> condition
    ) {
        return first.stream().flatMap(
                firstItem -> second.stream().map(secondItem -> new ImmutablePair<>(firstItem, secondItem))
        ).filter(condition);
    }
}
