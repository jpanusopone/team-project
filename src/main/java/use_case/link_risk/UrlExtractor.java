package use_case.link_risk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlExtractor {
    // Pattern 1: URLs with explicit http/https scheme.
    private static final Pattern SCHEME_URL_PATTERN = Pattern.compile(
            "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern 2: "naked" URLs without scheme, e.g. www.example.com, example.com/path
    // This is intentionally conservative to avoid matching random "a.b" text.
    private static final Pattern NAKED_URL_PATTERN = Pattern.compile(
            "\\b(?:www\\.)?[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,}" +           // domain.tld or www.domain.tld
                    "(?:/[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]*)?",              // optional path/query/etc.
            Pattern.CASE_INSENSITIVE
    );

    // Characters we want to trim off the *end* of a detected URL,
    // e.g. "https://example.com)." -> "https://example.com"
    private static final Pattern TRAILING_PUNCTUATION = Pattern.compile("[).,!?;:]+$");

    private UrlExtractor() {
        // Prevent instantiation
    }

    /**
     * Extracts all URLs (with or without http/https) from the given email text.
     *
     * @param text the raw email text
     * @return a list of URLs in the order they appear in the text
     */
    public static List<String> extractUrls(String text) {
        List<String> urls = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return urls;
        }

        // 1) Find all scheme URLs first
        List<Range> schemeRanges = new ArrayList<>();
        Matcher schemeMatcher = SCHEME_URL_PATTERN.matcher(text);
        while (schemeMatcher.find()) {
            String url = cleanUrl(schemeMatcher.group(1));
            if (!url.isEmpty()) {
                urls.add(url);
                schemeRanges.add(new Range(schemeMatcher.start(1), schemeMatcher.end(1)));
            }
        }

        // 2) Find naked URLs that are not inside a scheme URL range
        Matcher nakedMatcher = NAKED_URL_PATTERN.matcher(text);
        Set<String> alreadyAdded = new HashSet<>(urls); // avoid duplicates
        while (nakedMatcher.find()) {
            int start = nakedMatcher.start();
            int end = nakedMatcher.end();

            if (isInsideAnyRange(start, end, schemeRanges)) {
                // This naked match is actually part of an http/https URL we've already captured
                continue;
            }

            String url = cleanUrl(nakedMatcher.group());
            if (!url.isEmpty() && !alreadyAdded.contains(url)) {
                urls.add(url);
                alreadyAdded.add(url);
            }
        }

        return urls;
    }

    /**
     * Remove trailing punctuation that commonly sticks to URLs in text.
     */
    private static String cleanUrl(String raw) {
        if (raw == null) {
            return "";
        }
        String trimmed = raw.trim();
        // Strip trailing punctuation like ),.?!:;
        trimmed = TRAILING_PUNCTUATION.matcher(trimmed).replaceAll("");
        return trimmed;
    }

    private static boolean isInsideAnyRange(int start, int end, List<Range> ranges) {
        for (Range r : ranges) {
            if (start >= r.start && end <= r.end) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper class to track character index ranges.
     */
    private static final class Range {
        final int start;
        final int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
