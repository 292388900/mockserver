package org.mockserver.matchers;

import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import static org.skyscreamer.jsonassert.JSONCompare.compareJSON;

/**
 * @author jamesdbloom
 */
public class JsonStringMatcher extends BodyMatcher<String> implements Matcher<String> {
    private final String matcher;

    public JsonStringMatcher(String matcher) {
        this.matcher = matcher;
    }

    public boolean matches(String matched) {
        boolean result = false;

        JSONCompareResult jsonCompareResult;
        try {
            jsonCompareResult = compareJSON(matcher, matched, JSONCompareMode.LENIENT);

            if (jsonCompareResult.passed()) {
                PropertiesMatched.increment();
                result = true;
            }

            if (!result) {
                logger.trace("Failed to perform JSON match [{}] with [{}] because {}", matched, this.matcher, jsonCompareResult.getMessage());
            }
        } catch (Exception e) {
            logger.trace("Failed to perform JSON match [{}] with [{}] because {}", matched, this.matcher, e.getMessage());
        }

        return result;
    }
}
