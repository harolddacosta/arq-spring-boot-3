/* AssentSoftware (C)2023 */
package com.decathlon.rest.context.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.decathlon.rest.RestServicesApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestServicesApplication.class})
class TranslatorTest {

    @Autowired private Translator translator;

    @BeforeEach
    void init() {
        Locale.setDefault(new Locale("es", "ES"));
    }

    @Test
    void when_translate_ok() {
        assertEquals(
                "Deber\u00edas ver este mensaje, con acentos, si no es asi, deber\u00edas chequear el encoding del archivo de messages.properties",
                translator.toLocale("test.string", new Locale("es")));

        assertEquals(
                "You should see this message",
                translator.toLocale("test.string", new Locale("en")));
    }

    @Test
    void when_translate_only_msg_ok() {
        assertEquals(
                "Deber\u00edas ver este mensaje, con acentos, si no es asi, deber\u00edas chequear el encoding del archivo de messages.properties",
                translator.toLocale("test.string"));
    }

    @Test
    void when_translate_with_args() {
        assertEquals(
                "Prueba con un argumento Prueba",
                translator.toLocale("test.string.args", new String[] {"Prueba"}, new Locale("es")));

        assertEquals(
                "Test with argument Prueba",
                translator.toLocale("test.string.args", new String[] {"Prueba"}, new Locale("en")));
    }

    @Test
    void when_translate_with_args_no_locale() {
        assertEquals(
                "Prueba con un argumento Prueba",
                translator.toLocale("test.string.args", new String[] {"Prueba"}));
    }

    @Test
    void when_translate_not_ok() {
        assertThrows(NoSuchMessageException.class, () -> translator.toLocale("test.string.bad"));
    }
}
