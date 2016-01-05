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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility methods for {@link Exception}.
 * Created by rishi on 15-10-30.
 */
public class ExceptionUtils {

    // Source: http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
    // Date: 30 Oct, 2015

    /**
     * Extracts the stack trace from an {@link Exception} object to a formatted {@link String}.
     *
     * @param exception The {@link Exception} whose stack trace has to be extracted out.
     * @return The extracted stack trace {@link String}.
     */
    public static String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        pw.close();
        return stackTrace;
    }
}
