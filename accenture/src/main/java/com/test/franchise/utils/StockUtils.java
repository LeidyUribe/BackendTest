package com.test.franchise.utils;

import com.test.franchise.entities.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class StockUtils {

    private StockUtils() {}

    public static Optional<Product> highestStock(List<Product> products) {
        return products.stream().max(Comparator.comparing(Product::getStock));
    }
}