package masterthesis.tpc;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
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
    
    public static <T, U> Stream<ImmutablePair<T, U>> innerJoinHashmaps(
            Collection<T> left,
            Function<T, Object> leftKeyExtractor,
            Collection<U> right,
            Function<U, Object> rightKeyExtractor
    ) {
        HashMap<Object, T> leftMap = new HashMap<>();
        for (T leftElement : left) {
            leftMap.put(leftKeyExtractor.apply(leftElement), leftElement);
        }
        
        List<ImmutablePair<T, U>> result = new ArrayList<>();
        
        for (U rightElement : right) {
            T leftElement = leftMap.get(rightKeyExtractor.apply(rightElement));
            if (leftElement != null) {
                result.add(new ImmutablePair<>(leftElement, rightElement));
            }
        }
        
        return result.stream();
    }
}
