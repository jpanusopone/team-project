package use_case.link_risk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlExtractor {

    /**
     * Pattern 1: URLs with explicit http/https scheme.
     */
    private static final Pattern SCHEME_URL_PATTERN = Pattern.compile(
            "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Pattern 2: "naked" URLs without scheme, e.g. www.example.com, example.com/path.
     * This is intentionally conservative to avoid matching random "a.b" text.
     */
    private static final Pattern NAKED_URL_PATTERN = Pattern.compile(
            "\\b(?:www\\.)?[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,}"
                    + "(?:/[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]*)?",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Characters we want to trim off the end of a detected URL,
     * for example: "https://example.com)." should become "https://example.com".
     */
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
        final List<String> urls = new ArrayList<>();

        if (text != null && !text.isEmpty()) {
            final List<Range> schemeRanges = new ArrayList<>();
            final Matcher schemeMatcher = SCHEME_URL_PATTERN.matcher(text);

            while (schemeMatcher.find()) {
                final String url = cleanUrl(schemeMatcher.group(1));
                if (!url.isEmpty()) {
                    urls.add(url);
                    schemeRanges.add(new Range(schemeMatcher.start(1), schemeMatcher.end(1)));
                }
            }

            final Matcher nakedMatcher = NAKED_URL_PATTERN.matcher(text);
            final Set<String> alreadyAdded = new HashSet<>(urls);

            while (nakedMatcher.find()) {
                final int start = nakedMatcher.start();
                final int end = nakedMatcher.end();

                // Skip if this naked URL is part of a scheme URL we already recorded.
                if (isInsideAnyRange(start, end, schemeRanges)) {
                    continue;
                }

                final String url = cleanUrl(nakedMatcher.group());
                if (!url.isEmpty() && !alreadyAdded.contains(url)) {
                    urls.add(url);
                    alreadyAdded.add(url);
                }
            }
        }

        return urls;
    }

    /**
     * Removes trailing punctuation that commonly sticks to URLs in text.
     *
     * @param raw the raw URL string
     * @return the cleaned URL string without trailing punctuation; empty string if raw is null
     */
    private static String cleanUrl(String raw) {
        String trimmed = "";
        if (raw != null) {
            trimmed = raw.trim();
            trimmed = TRAILING_PUNCTUATION.matcher(trimmed).replaceAll("");
        }
        return trimmed;
    }

    /**
     * Determines whether the given [start, end] range lies completely inside
     * any of the provided ranges.
     *
     * @param start  the start index of the candidate range
     * @param end    the end index of the candidate range
     * @param ranges the list of existing ranges
     * @return true if the candidate range is inside any of the ranges; false otherwise
     */
    private static boolean isInsideAnyRange(int start, int end, List<Range> ranges) {
        boolean inside = false;
        for (Range range : ranges) {
            if (start >= range.getStart() && end <= range.getEnd()) {
                inside = true;
                break;
            }
        }
        return inside;
    }

    /**
     * Helper class to track character index ranges.
     */
    private static final class Range {
        private final int start;
        private final int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int getStart() {
            return start;
        }

        int getEnd() {
            return end;
        }
    }
}
