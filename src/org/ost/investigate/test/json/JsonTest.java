package org.ost.investigate.test.json;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) {
        System.out.println("-------------------------------------- Finish " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Get value from json")
    void shouldGetValueFromJSON() {

        String jsonString = "{\"level\":\"test\"}";
        JSONObject json = new JSONObject(jsonString);
        assertEquals("test", json.optString("level", ""));
    }

    @Test
    @DisplayName("Get value from json when optional field is not present")
    void shouldGetNullFromJSONWhenOptionalFieldIsNotPresent() {

        String jsonString = "{}";
        JSONObject json = new JSONObject(jsonString);
        assertNull(json.optString("level", null));
    }

    @Test
    @DisplayName("Get null from json when optional field is not present and replace in string")
    void shouldGetNullFromJSONWhenOptionalFieldIsNotPresentAndReplaceInString() {

        String jsonString = "{}";
        JSONObject json = new JSONObject(jsonString);
        assertNull(ofNullable(json.optString("level", null))
                .filter(s-> !s.isEmpty())
                .map(s -> s.replace("*", ""))
                .orElse(null));
    }

    @Test
    @DisplayName("Get value from json when optional field is present and replace in string")
    void shouldGetValueFromJSONWhenOptionalFieldIsPresentAndReplaceInString() {

        String jsonString = "{\"level\":\"*test*\"}";
        JSONObject json = new JSONObject(jsonString);
        assertEquals("test", ofNullable(json.optString("level", null))
                .map(s -> s.replace("*", ""))
                .orElse(null));
    }
}
