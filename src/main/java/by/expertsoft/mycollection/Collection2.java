package by.expertsoft.mycollection;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Collection2<E> extends Collection<E> {

    default void forEachIf(Consumer<E> action, Predicate<E> filter) {
        for(E obj : this) {
            if(filter.test(obj)) {
                action.accept(obj);
            }
        }
    }
}
