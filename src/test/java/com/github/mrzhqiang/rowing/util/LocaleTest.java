package com.github.mrzhqiang.rowing.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

public class LocaleTest {

    @Test
    public void isoCountries() {
        String[] countries = Locale.getISOCountries();
        System.out.println("locale countries: " + Arrays.toString(countries));
    }

    @Test
    public void isoLanguages() {
        String[] languages = Locale.getISOLanguages();
        System.out.println("locale languages: " + Arrays.toString(languages));
    }

    @Test
    public void availableLocales() {
        Locale[] locales = Locale.getAvailableLocales();
        System.out.println("locale available: " + Arrays.toString(locales));
    }
}
