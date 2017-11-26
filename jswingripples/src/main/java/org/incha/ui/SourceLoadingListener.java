package org.incha.ui;

import org.incha.core.JavaProject;

/**
 * Created by hlib on 18.11.17.
 */
interface SourceLoadingListener {
    void onSourcesLoaded(JavaProject project);
}
