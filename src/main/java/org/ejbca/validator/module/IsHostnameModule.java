/***********************************************************************************************************
 * The MIT License                                                                                         *
 *                                                                                                         *
 * Copyright 2018 Bastian Fredriksson                                                                      *
 *                                                                                                         *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software           *
 * and associated documentation files (the "Software"), to deal in the Software without restriction,       *
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,   *
 * subject to the following conditions:                                                                    *
 *                                                                                                         *
 * The above copyright notice and this permission notice shall be included in all copies or substantial    *
 * portions of the Software.                                                                               *
 *                                                                                                         *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT   *
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.     *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, *
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE     *
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                                                  *
 ***********************************************************************************************************/

package org.ejbca.validator.module;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ejbca.validator.extraction.data.CertificateData;

import com.google.common.net.InternetDomainName;

public class IsHostnameModule implements ValidatorModule {
    private final String moduleName;

    public static class HostnameModuleBuilder implements ModuleExecutorBuilder {
        private boolean okIsFailure;
        private String moduleName;
        private List<CertificateData> dataToValidate;

        @Override
        public ModuleExecutorBuilder okIsFailure(boolean okIsFailure) {
            this.okIsFailure = okIsFailure;
            return this;
        }

        @Override
        public ModuleExecutorBuilder setModuleName(String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        @Override
        public ModuleExecutorBuilder setDataToValidate(final List<CertificateData> dataToValidate) {
            this.dataToValidate = dataToValidate;
            return this;
        }

        @Override
        public ModuleExecutor build() {
            return new ModuleExecutor(new IsHostnameModule(moduleName), dataToValidate, okIsFailure);
        }
    }

    public static HostnameModuleBuilder builder() {
        return new HostnameModuleBuilder();
    }

    public IsHostnameModule(final String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public Map<String, Boolean> validate(final CertificateData certificateData) {
        return certificateData.getDataItems()
                .stream()
                .map(dataItem -> new SimpleEntry<String, Boolean>(dataItem, InternetDomainName.isValid(dataItem)))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
