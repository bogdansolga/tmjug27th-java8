package org.tmjug.java8.service;

import org.tmjug.java8.model.Product;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A service for processing {@link org.tmjug.java8.model.Product} objects
 *
 * @author bogdan.solga
 */
public class ProductService {

    private static List<Product> products = Arrays.asList(
            Product.of(5, "Tablet", 250d),
            Product.of(2, "iSomething", 500d),
            Product.of(3, "Monitor", 600d),
            Product.of(7, "Keyboard", 50d),
            Product.of(1, "Mouse", 30d)
    );

    private static Random random = new Random(100);

    // imperative version
    public Set<String> getProductsContainingNameImperative(final String searchedProductName) {
        final Set<String> foundProducts = new HashSet<>();
        for (Product product : products) {
            final String productName = product.getName();
            if (productName.contains(searchedProductName)) {
                foundProducts.add(productName);
            }
        }

        return foundProducts;
    }

    // functional version
    public List<String> getProductsContainingNameFunctional(final String searchedProductName) {
        //products.forEach(product -> System.out.println(product));

        //for (int i = 0; i < 10; i++) {}
        //IntStream.range(0, 10).forEach(value -> ...);

        Map<String, Integer> map = new HashMap<String, Integer>() {{
            put("something", 5);
            put("anything", 5);
        }};
        map.forEach((key, value) -> System.out.println(key + " -> " + value));

        return products.stream()
                       .filter(product -> product.getName().contains(searchedProductName))
                       .map(Product::getName)
                       .collect(Collectors.toList());
    }

    public void displayOrderedProducts() {
        // display the product
        Consumer<Product> productConsumer = it -> System.out.println("The product with the ID " + it.getId() + " is "
                + it.getName() + " and has the price " + it.getPrice());

        // if case sensitive comparison is needed --> Comparator is another functional interface
        Comparator<Product> productComparator = (first, second) -> Collator.getInstance().compare(first.getName(), second.getName());

        products.stream()
                .sorted(Comparator.comparing(Product::getName))
                //.sorted(productComparator)
                .forEach(productConsumer);
    }

    // getting the IDs of the products which have a price bigger than a value
    public void parallelProcessing(int numberOfProducts, double biggerThanPrice) {
        List<Product> aLotOfProducts = new ArrayList<>();
        IntStream.range(0, numberOfProducts)
                 .forEach(value -> aLotOfProducts.add(Product.of(value, "The product with the ID " + value, value * random.nextDouble())));

        long now = System.currentTimeMillis();
        Set<String> products = aLotOfProducts.parallelStream()
                                             .filter(item -> item.getPrice() > biggerThanPrice)
                                             .map(Product::getName)
                                             .collect(Collectors.toSet());
        System.out.println("[parallel] Got " + products.size() + " products in " + (System.currentTimeMillis() - now) + " ms");
    }

    // getting the IDs of the products which have a price bigger than a value
    public void sequentialProcessing(int numberOfProducts, double biggerThanPrice) {
        List<Product> aLotOfProducts = new ArrayList<>();
        IntStream.range(0, numberOfProducts)
                 .forEach(value -> aLotOfProducts.add(Product.of(value, "The product with the ID " + value, value * random.nextDouble())));

        long now = System.currentTimeMillis();
        Set<String> products = aLotOfProducts.stream()
                                             .filter(item -> item.getPrice() > biggerThanPrice)
                                             .map(Product::getName)
                                             .collect(Collectors.toSet());
        System.out.println("[sequential] Got " + products.size() + " products in " + (System.currentTimeMillis() - now) + " ms");
    }
}
