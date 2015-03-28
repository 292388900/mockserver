package org.mockserver.matchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author jamesdbloom
 */
public class BinaryMatcher extends BodyMatcher<byte[]> implements Matcher<byte[]> {
    private static Logger logger = LoggerFactory.getLogger(BinaryMatcher.class);
    private final byte[] matcher;

    public BinaryMatcher(byte[] matcher) {
        this.matcher = matcher;
    }

    public boolean matches(byte[] matched) {
        boolean result = false;

        if (matcher == null || matcher.length == 0) {
            result = true;
        } else if (Arrays.equals(matcher, matched)) {
            PropertiesMatched.increment();
            result = true;
        }

        if (!result) {
            logger.trace("Failed to perform binary match [{}] with [{}] because {}", matched);
        }

        return result;
    }

    @Override
    public String[] fieldsExcludedFromEqualsAndHashCode() {
        return new String[]{"logger"};
    }
}
