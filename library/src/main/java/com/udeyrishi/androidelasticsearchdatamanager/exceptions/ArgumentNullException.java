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

package com.udeyrishi.androidelasticsearchdatamanager.exceptions;

/**
 * {@link IllegalArgumentException} thrown when an argument to a method is null.
 * Created by rishi on 15-10-30.
 */
public class ArgumentNullException extends IllegalArgumentException {

    /**
     * Creates an instance of {@link ArgumentNullException}.
     */
    public ArgumentNullException() {
        super();
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param message The detail message for this exception.
     */
    public ArgumentNullException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param cause The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     *
     * @param message The detail message for this exception.
     * @param cause   The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
