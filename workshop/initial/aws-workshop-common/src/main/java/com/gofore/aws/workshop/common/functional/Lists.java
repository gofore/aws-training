package com.gofore.aws.workshop.common.functional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Lists {
    
    public static <F, T> List<T> map(List<F> list, Function<F, T> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }
    
    public static <T> Optional<T> findFirst(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).findFirst();
    }
    
    public static <T> Function<List<T>, Optional<T>> findFirst(Predicate<T> predicate) {
        return l -> findFirst(l, predicate);
    }
    
    private Lists() {
        
    }
    
}
