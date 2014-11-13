package com.gofore.aws.workshop.common.functional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lists {
    
    public static <F, T> List<T> map(List<F> list, Function<F, T> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }
    
    private Lists() {
        
    }
    
}
