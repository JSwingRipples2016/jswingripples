package org.incha.core.jswingripples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by stefano on 19-10-16.
 */
public class JRipplesModuleRunner {
    public interface ModuleRunnerListener {
        void runSuccessful();
        void runFailure();
    }

    private ModuleRunnerListener listener;
    private int modulesFinished;
    private int targetModulesRunning;

    public JRipplesModuleRunner(ModuleRunnerListener listener) {
        this.listener = listener;
    }

    public synchronized void moduleFinished() {
        // this method is called by each running module
        if (++modulesFinished == targetModulesRunning) {
            // a module runner run is successful only if all
            // modules were successful.
            listener.runSuccessful();
        }
    }

    public void runModulesWithPriority(Collection<JRipplesModule> modules) {
        runModules(JRipplesModule.sortByPriority(modules));
    }

    private void runModules(final LinkedList<Collection<JRipplesModule>> modules) {
        Collection<JRipplesModule> samePriorityModules;
        if ( (samePriorityModules = modules.pollFirst()) != null) {
            new JRipplesModuleRunner(new ModuleRunnerListener() {
                @Override
                public void runSuccessful() {
                    // if the current list of equal priority modules produces a successful run,
                    // then go ahead and run the rest
                    if ( modules.isEmpty()) {
                        // if all modules were succesful, then notify the main listener
                        listener.runSuccessful();
                    } else {
                        // else, keep going!
                        runModules(modules);
                    }
                }

                @Override
                public void runFailure() {
                    // if the run was a failure, then stop here
                    listener.runFailure();
                }
            }).runModules(samePriorityModules);
        }
    }

    private void runModules(Collection<JRipplesModule> modules) {
        if (modules.isEmpty()) {
            listener.runSuccessful();
            return;
        }
        modulesFinished = 0;
        targetModulesRunning = modules.size();
        for (JRipplesModule module : modules) {
            try {
                module.runModuleWithinRunner(this);
            } catch (Exception e) {
                listener.runFailure();
            }
        }
    }
}
