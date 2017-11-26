package org.incha.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hlib on 21.11.17.
 */
public class ProjectNamingUtils {
    public static String getGitProjectNameFromUrl(String remoteUrl) {
        Matcher matcher = Pattern.compile("(.*/)?([0-9A-Za-z]+)(\\.git)?").matcher(remoteUrl);
        return matcher.find() ? matcher.group(2) : "";
    }
}
