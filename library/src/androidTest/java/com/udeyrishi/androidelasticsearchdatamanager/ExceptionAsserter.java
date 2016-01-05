/**
 Copyright 2016 Udey Rishi
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.udeyrishi.androidelasticsearchdatamanager;

import junit.framework.AssertionFailedError;

/**
 * Supplementary test methods to the JUnit test library for testing expected exceptions.
 * Created by rishi on 15-10-30.
 */
public class ExceptionAsserter {

    /**
     * Asserts that a {@link Runnable} throws an expected exception. Asserts true if an exception of
     * the provided class, or a child class is thrown.
     *
     * @param runnable              The runnable to be tested.
     * @param expectedExceptionType The {@link Class} for the expected exception.
     * @param checkInnerException   If the inner exception (the {@link Throwable#getCause()} should
     *                              be checked for exception type match recursively.
     * @param <TException>          The generic type for the expected exception.
     */
    public static <TException extends Exception> void assertThrowsException(Runnable runnable,
                                                                            Class<TException> expectedExceptionType,
                                                                            boolean checkInnerException) {
        try {
            runnable.run();
        } catch (Exception exception) {
            if (expectedExceptionType.isInstance(exception)) {
                return;
            }

            if (checkInnerException && checkInnerException(exception, expectedExceptionType)) {
                return;
            }
            throw new AssertionFailedError(
                    String.format(
                            "Expected exception of type '%s', but found '%s'.\nThrown exception's stack trace:\n"
                                    + "-----BEGIN-----\n%s\n-----END-----",
                            expectedExceptionType.getName(), exception.getClass().getName(), ExceptionUtils.getStackTrace(exception)));
        }

        throw new AssertionFailedError(String.format("Expected exception of type '%s', but none thrown.",
                expectedExceptionType.getName()));
    }

    /**
     * Asserts that a {@link Runnable} throws an expected exception. Asserts true if an exception of
     * the provided class, or a child class is thrown. This overloaded version sets the "checkInnerException"
     * value to false (see {@link ExceptionAsserter#assertThrowsException(Runnable, Class, boolean)}.
     *
     * @param runnable              The runnable to be tested.
     * @param expectedExceptionType The {@link Class} for the expected exception.
     * @param <TException>          The generic type for the expected exception.
     */
    public static <TException extends Exception> void assertThrowsException(Runnable runnable,
                                                                            Class<TException> expectedExceptionType) {
        assertThrowsException(runnable, expectedExceptionType, false);
    }

    private static <TException extends Exception> boolean checkInnerException(Throwable exception, Class<TException> expectedExceptionType) {
        Throwable cause = exception.getCause();
        if (cause == null) {
            return false;
        }

        if (expectedExceptionType.isInstance(cause)) {
            return true;
        } else {
            return checkInnerException(cause, expectedExceptionType);
        }
    }
}
