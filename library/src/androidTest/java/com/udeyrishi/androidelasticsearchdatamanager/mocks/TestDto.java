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

package com.udeyrishi.androidelasticsearchdatamanager.mocks;

import com.google.gson.annotations.Expose;

/**
 * Created by rishi on 15-10-30.
 */
public class TestDto {
    @Expose
    private int aNumber;
    @Expose
    private String aString;
    @Expose
    private boolean aBoolean;
    private String aHiddenString;


    public TestDto(int aNumber, String aString, boolean aBoolean, String aHiddenString) {
        this.aNumber = aNumber;
        this.aString = aString;
        this.aBoolean = aBoolean;
        this.aHiddenString = aHiddenString;
    }

    public int getaNumber() {
        return aNumber;
    }

    public void setaNumber(int aNumber) {
        this.aNumber = aNumber;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public String getaHiddenString() {
        return aHiddenString;
    }

    public void setaHiddenString(String aHiddenString) {
        this.aHiddenString = aHiddenString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TestDto)) {
            return false;
        }

        TestDto rhs = (TestDto) obj;

        return this.getaBoolean() == rhs.getaBoolean() &&
                this.getaHiddenString().equals(rhs.getaHiddenString()) &&
                this.getaNumber() == rhs.getaNumber() &&
                this.getaString().equals(rhs.getaString());
    }

    @Override
    public int hashCode() {
        return new Boolean(this.getaBoolean()).hashCode() ^
                new Integer(this.getaNumber()).hashCode() ^
                this.getaString().hashCode() ^
                this.getaHiddenString().hashCode();
    }
}
