package org.tmjug.java8;

import org.tmjug.java8.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A few samples of some of the new features of Java 8
 *
 * @author bogdan.solga
 */
public class Java8Features {

    public static void main(String[] args) {
        // collectionsOperations();

        // newThread();

        // predicates();
        // consumers();
        // functions();
        // suppliers();

        // unaryOperator();
        // binaryOperator();

        // flatMap();
        // optional();
    }

    private static void collectionsOperations() {
        final List<String> words = new ArrayList<>(Arrays.asList("I want a holiday, not just a weekend".split("\\s")));
        /*
        final Iterator<String> iterator = words.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.length() < 2) {
                iterator.remove();
            }
        }

        for (String word : words) {
            System.out.println(word);
        }
        */

        //words.removeIf(word -> word.length() < 2);
        //words.forEach(System.out::println);
        //words.forEach(item -> System.out.println(item));

        Predicate<String> lessThan2Chars = word -> word.length() < 2;
        words.removeIf(lessThan2Chars);

        /* an example of a Predicate which uses several statements, hence it needs to be wrapped in { }
        Predicate<String> aComplexPredicate = word -> {
            if (word != null && word.length() < 2) {
                return true;
            } else {
                return false;
            }
        };
        */

        // lower-casing every word
        words.stream()
             .map(String::toLowerCase)
             .forEach(System.out::println);

        // sort
        words.stream()
             .sorted(Comparator.comparing(item -> item))
             .forEach(System.out::println);
    }

    private static void newThread() {
        Runnable runnable = () -> System.out.println("something");
        runnable.run();
    }

    private static void predicates() {
        Predicate<Integer> isEven = value -> value % 2 == 0;
        Predicate<Integer> isBiggerThan10 = value -> value > 10;

        int testedValue = 13;
        boolean isEvenAndBiggerThan10 = isEven.and(isBiggerThan10).test(testedValue);
        System.out.println(testedValue + " is even and bigger than 10: " + isEvenAndBiggerThan10);

        Predicate<String> isNotNullOrEmpty = text -> text != null && !text.isEmpty();
        System.out.println(isNotNullOrEmpty.test(""));
    }

    private static void consumers() {
        Consumer<String> displayer = System.out::println;
        displayer.accept("a very simple Consumer");

        List<String> aListOfStrings = Arrays.asList("Welcome", "to", "Java", "8!");
        aListOfStrings.forEach(displayer);

        String nullableString = "a nullable String";
        Optional.ofNullable(nullableString).ifPresent(System.out::println);

        Consumer<String> toLowerCase = (String value) -> System.out.println(value.toLowerCase());
        toLowerCase.accept("A Little Something");
    }

    private static void suppliers() {
        Supplier<Integer> integerSupplier = () -> 20;
        System.out.println(integerSupplier.get());

        Supplier<IllegalArgumentException> aNiceException = () -> new IllegalArgumentException("Nope :)");
        String value = Optional.ofNullable("some string")
                               .orElseThrow(aNiceException);

        Random random = new Random(100);
        Supplier<Product> productSupplier = () -> Product.of(random.nextInt(10), "A precious gift", random.nextDouble());

        // why, JDK, why don't you allow _ for lambda parameters?
        //Consumer<Product> displayer = _ -> System.out.println(_.getName() + " has the price " + _.getPrice());

        Consumer<Product> displayer = it -> System.out.println(it.getName() + " has the ID " + it.getId() + " and the price " + it.getPrice());
        displayer.accept(productSupplier.get()); // mixing a supplier and a consumer
    }

    private static void functions() {
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println(parser.apply("20x"));

        Function<Product, String> converter = product -> "The product '" + product.getName() + "' has the price " + product.getPrice();
        System.out.println(converter.apply(Product.of(23, "iSomething", 600)));

        Function<Integer, Integer> square = value -> value * value;
        Function<Integer, Integer> minus20 = value -> value - 20;
        System.out.println("And then: " + square.andThen(minus20).apply(10));
        System.out.println("Compose: " + square.compose(minus20).apply(25));

        DoubleFunction<String> doubleFunction = input -> "The value is " + input;
        System.out.println(doubleFunction.apply(20d));
    }

    private static void unaryOperator() {
        UnaryOperator<Integer> tenTimes = input -> input * 10;
        System.out.println(tenTimes.apply(5));
    }

    private static void binaryOperator() {
        BinaryOperator<Double> squareRoot = (first, second) -> Math.sqrt(first * second);
        System.out.println(squareRoot.apply(5d, 8d));
    }

    private static void flatMap() {
        List<String> first = Arrays.asList("first", "second");
        List<String> second = Arrays.asList("third", "fourth");

        Stream<List<String>> listStream = Stream.of(first, second);
        final Stream<String> collect = listStream.flatMap(Collection::stream);
        System.out.println(collect.collect(Collectors.toList()));
    }

    private static void optional() {
        // using a map, retrieving an item from it

        Map<String, Integer> map = new HashMap<String, Integer>() {{
            put("January", 1);
            put("February", 2);
        }};

        Integer third = map.get("March");
        if (third != null) {
            System.out.println(third.intValue());
        }

        Integer thirdMonth = Optional.ofNullable(map.get("March")).orElse(3);
        Integer thirdOrThrow = Optional.ofNullable(map.get("March")).orElseThrow(() -> new IllegalArgumentException("Nope, we don't have it"));
        Integer getOrDefault = map.getOrDefault("March", 3);

        Optional<String> optionalValue = Optional.ofNullable("aNullableString");
        int result = optionalValue.map(value -> Integer.parseInt(value)).orElse(0);
        int nonZero = optionalValue.map(value -> Integer.parseInt(value)).orElseThrow(() -> new IllegalArgumentException("Can't be 0"));

        String aString = Optional.ofNullable("something").orElse("else");
        Optional<String> optional = Optional.ofNullable("another");
        if (optional.isPresent()) {
            String wrapped = optional.get();
        }

        optional.ifPresent(it -> System.out.println(it));
    }
}
