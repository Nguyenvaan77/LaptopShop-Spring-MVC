package com.basis.anhangda37.service.specification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.Product_;

import jakarta.persistence.criteria.Predicate;

public class ProductSpec {
    public static Specification<Product> nameLike(String name) {
        return (root, query, cb) -> cb.like(root.get(Product_.NAME), "%" + name + "%");
    }

    public static Specification<Product> priceGreaterThan(Double price) {
        return (root, query, cb) -> cb.greaterThan(root.get(Product_.PRICE), price);
    }

    public static Specification<Product> priceLessThan(Double price) {
        return (root, query, cb) -> cb.lessThan(root.get(Product_.PRICE), price);
    }

    public static Specification<Product> belongFactory(String factory) {
        return (root, query, cb) -> cb.like(root.get(Product_.FACTORY), factory);
    }

    public static Specification<Product> belongListFactory(List<String> factorys) {
        return (root, query, cb) -> {
            return root.get(Product_.FACTORY).in(factorys);
        };
    }

    public static Specification<Product> belongListTarget(List<String> targets) {
        return (root, query, cb) -> {
            return root.get(Product_.TARGET).in(targets);
        };
    }

    public static Specification<Product> rangePrice(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            Double realMax = maxPrice;
            Double realMin = minPrice;

            if (minPrice > maxPrice) {
                realMax = minPrice;
                realMin = maxPrice;
            }

            return cb.between(root.get(Product_.PRICE), realMin, realMax);
        };
    }

    public static Specification<Product> listRangePrice(List<Double[]> listPriceThreshold) {
        return (root, query, cb) -> {
            List<Predicate> predicates = listPriceThreshold.stream()
                    .map(item -> cb.between(root.get(Product_.PRICE), item[0], item[1]))
                    .collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
