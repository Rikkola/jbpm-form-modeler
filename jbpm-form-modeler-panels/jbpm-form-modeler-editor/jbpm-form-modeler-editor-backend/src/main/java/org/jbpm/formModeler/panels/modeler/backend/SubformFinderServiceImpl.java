package org.jbpm.formModeler.panels.modeler.backend;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jbpm.formModeler.api.model.Form;
import org.jbpm.formModeler.core.rendering.FormFinder;
import org.jbpm.formModeler.core.rendering.SubformFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class SubformFinderServiceImpl implements SubformFinderService {

    private Logger log = LoggerFactory.getLogger( SubformFinderService.class );

    @Inject
    private Instance<FormFinder> findersInstances;

    protected Set<FormFinder> finders;

    @PostConstruct
    protected void init() {
        finders = new TreeSet<FormFinder>(new Comparator<FormFinder>() {

            @Override
            public int compare(FormFinder o1, FormFinder o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        for (FormFinder p : findersInstances) {
            finders.add(p);
        }
    }

    @Override
    public Form getForm( String ctxUID ) {
        for (FormFinder finder : finders) {
            Form form = finder.getForm( ctxUID );
            if (form != null) return form;
        }
        return null;
    }

    @Override
    public Form getFormByPath( String formPath, String ctxUID ) {
        for (FormFinder finder : finders) {
            Form form = finder.getFormByPath( ctxUID, formPath );
            if (form != null) return form;
        }
        return null;
    }

    @Override
    public Form getFormById( long formId,
                             String ctxUID ) {

        for (FormFinder finder : finders) {
            Form form = finder.getFormById( ctxUID, formId );
            if (form != null) return form;
        }
        return null;
    }
}