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
 * A {@link RuntimeException} thrown when a service is not available.
 * Created by rishi on 15-11-01.
 */
public class ServiceNotAvailableException extends Exception {

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     */
    public ServiceNotAvailableException() {
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     */
    public ServiceNotAvailableException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     * @param cause         The cause of this exception.
     */
    public ServiceNotAvailableException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param cause The cause of this exception.
     */
    public ServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
