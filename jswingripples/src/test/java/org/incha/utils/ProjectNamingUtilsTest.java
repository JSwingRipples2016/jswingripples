package org.incha.utils;

import junit.framework.TestCase;

import static org.incha.utils.ProjectNamingUtils.getGitProjectNameFromUrl;

/**
 * Created by hlib on 21.11.17.
 */
public class ProjectNamingUtilsTest extends TestCase {

    public void testGetGitProjectNameWithDotGit() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("http://path/project.git"));
    }

    public void testGetGitProjectNameWithoutDotGit() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("http://path/project"));
    }

    public void testGetGitProjectNameFromUrlWithoutLeadingPath() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("project.git"));
    }

}