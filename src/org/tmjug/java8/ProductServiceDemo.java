package org.tmjug.java8;

import org.tmjug.java8.service.ProductService;

/**
 * Some tests for the {@link org.tmjug.java8.service.ProductService} class
 */
public class ProductServiceDemo {

    private static ProductService productService = new ProductService();

    public static void main(String[] args) {
        productService.getProductsContainingNameImperative("Tablet");
        productService.getProductsContainingNameFunctional("Tablet");

        //productService.displayOrderedProducts();

        final int productsNumber = 250;//0000;
        final double minPrice = 50;
        //productService.parallelProcessing(productsNumber, minPrice);
        //productService.sequentialProcessing(productsNumber, minPrice);

        // avoid parallel operations on shared / blocking resources
    }
}
