/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.common.util;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormattingTest {

    @Test
    public void testFormattingInEnglishLocale() {
        testFormattingInLocale(new Locale("en"), ',', '.');
    }

    @Test
    public void testFormattingInUkraineLocale() {
        // unicode 160 is non-breaking space
        testFormattingInLocale(new Locale("uk"), (char) 160, ',');
    }

    @Test
    public void testFormattingBytes() {
        assertThat(Formatting.formatBytes(0)).isEqualTo("0");
        assertThat(Formatting.formatBytes(1)).isEqualTo("1 byte");
        assertThat(Formatting.formatBytes(2)).isEqualTo("2 bytes");
        assertThat(Formatting.formatBytes(10)).isEqualTo("10 bytes");
        assertThat(Formatting.formatBytes(100)).isEqualTo("100 bytes");
        assertThat(Formatting.formatBytes(1023)).isEqualTo("1023 bytes");
        assertThat(Formatting.formatBytes(1024)).isEqualTo("1.0 KB");
        assertThat(Formatting.formatBytes(1500)).isEqualTo("1.5 KB");
        assertThat(Formatting.formatBytes(2047)).isEqualTo("2.0 KB");
        assertThat(Formatting.formatBytes(1024 * 1024)).isEqualTo("1.0 MB");
        assertThat(Formatting.formatBytes(1024 * 1024 * 1024)).isEqualTo("1.0 GB");
    }

    private void testFormattingInLocale(Locale locale, char ts, char ds) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        assertThat(Formatting.displaySixDigitsOfPrecision(3, nf))
                .isEqualTo("3");
        assertThat(Formatting.displaySixDigitsOfPrecision(333333, nf))
                .isEqualTo("333" + ts + "333");
        assertThat(Formatting.displaySixDigitsOfPrecision(3333333, nf))
                .isEqualTo("3" + ts + "333" + ts + "333");

        assertThat(Formatting.displaySixDigitsOfPrecision(3.3, nf))
                .isEqualTo("3" + ds + "3");
        assertThat(Formatting.displaySixDigitsOfPrecision(3333.3, nf))
                .isEqualTo("3" + ts + "333" + ds + "3");
        assertThat(Formatting.displaySixDigitsOfPrecision(3333333.3, nf))
                .isEqualTo("3" + ts + "333" + ts + "333");

        assertThat(Formatting.displaySixDigitsOfPrecision(3.33333, nf))
                .isEqualTo("3" + ds + "33333");
        assertThat(Formatting.displaySixDigitsOfPrecision(3.333333, nf))
                .isEqualTo("3" + ds + "33333");
        assertThat(Formatting.displaySixDigitsOfPrecision(3.3333333, nf))
                .isEqualTo("3" + ds + "33333");

        assertThat(Formatting.displaySixDigitsOfPrecision(0.333333, nf))
                .isEqualTo("0" + ds + "333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.3333333, nf))
                .isEqualTo("0" + ds + "333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.33333333, nf))
                .isEqualTo("0" + ds + "333333");

        assertThat(Formatting.displaySixDigitsOfPrecision(0.0333333, nf))
                .isEqualTo("0" + ds + "0333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.03333333, nf))
                .isEqualTo("0" + ds + "0333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.033333333, nf))
                .isEqualTo("0" + ds + "0333333");

        assertThat(Formatting.displaySixDigitsOfPrecision(0.000000333333, nf))
                .isEqualTo("0" + ds + "000000333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.0000003333333, nf))
                .isEqualTo("0" + ds + "000000333333");
        assertThat(Formatting.displaySixDigitsOfPrecision(0.00000033333333, nf))
                .isEqualTo("0" + ds + "000000333333");
    }
}
