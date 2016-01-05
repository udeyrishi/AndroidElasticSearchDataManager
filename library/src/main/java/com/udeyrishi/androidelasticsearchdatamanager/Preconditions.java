package com.udeyrishi.androidelasticsearchdatamanager;

import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ArgumentNullException;

import java.util.Collection;

/**
 * A helper class containing static utility methods for asserting certain preconditions.
 * Created by rishi on 15-10-30.
 */
public class Preconditions {

    /**
     * Checks if the object passed is null or not. If null, then throws {@link ArgumentNullException}.
     * This should be ideally used in the beginning of a method.
     *
     * @param obj          The object to be checked.
     * @param argumentName The name of the argument (of the original method) that this object corresponds
     *                     to.
     * @param <T>          The generic type of the object being checked.
     * @return The passed object itself.
     * @throws ArgumentNullException Thrown, if the object is null.
     */
    public static <T> T checkNotNull(T obj, String argumentName) throws ArgumentNullException {
        if (obj == null) {
            throw new ArgumentNullException(String.format("Object '%s' can't be null.", argumentName));
        }
        return obj;
    }

    /**
     * Checks if the collection passed is null or not, and if it is not null, then if it is empty.
     * If null, then throws {@link ArgumentNullException}. If empty, throws {@link IllegalArgumentException}.
     * This should be ideally used in the beginning of a method.
     *
     * @param obj          The collection object to be checked.
     * @param argumentName The name of the argument (of the original method) that this object corresponds
     *                     to.
     * @param <T>          The generic type of the object being checked.
     * @return The passed object itself.
     * @throws ArgumentNullException    Thrown, if the collection is null.
     * @throws IllegalArgumentException Thrown, if the collection is empty.
     */
    public static <T extends Collection<?>> T checkNotNullOrEmpty(T obj, String argumentName) throws IllegalArgumentException {
        checkNotNull(obj, argumentName);
        if (obj.size() == 0) {
            throw new IllegalArgumentException(String.format("Object '%s' can't be empty.", argumentName));
        }
        return obj;
    }

    /**
     * Checks if the passed string is null and/or whitespace. If it's null, then throws
     * {@link ArgumentNullException}. if it's whitespace, then throws {@link IllegalArgumentException}.
     *
     * @param str          The string to be checked.
     * @param argumentName The name of the argument (of the original method) that this object corresponds
     *                     to.
     * @return The passed string.
     * @throws ArgumentNullException    Thrown, if the string is null.
     * @throws IllegalArgumentException Thrown, if the string is whitespace.
     */
    public static String checkNotNullOrWhitespace(String str, String argumentName) throws IllegalArgumentException {
        checkNotNull(str, argumentName);
        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("String '%s' can't be whitespace.", argumentName));
        }
        return str;
    }
}
