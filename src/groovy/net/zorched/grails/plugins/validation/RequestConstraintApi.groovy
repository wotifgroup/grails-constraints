package net.zorched.grails.plugins.validation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.orm.hibernate3.HibernateTemplate
import org.hibernate.SessionFactory

/**
 * The runtime implementation for ConstraintApi, which fetches information from the constraint stored in request attrs.
 */
class RequestConstraintApi implements ConstraintApi, GrailsApplicationAware {
    public static String CONSTRAINT_REQUEST_ATTRIBUTE = "grails-constraints_constraint"
    public static String VETO_REQUEST_ATTRIBUTE = "grails-constraints_veto"

    GrailsApplication grailsApplication

    @Override
    Object getParams(Object instance) {
        return constraint.parameter
    }

    @Override
    Object getHibernateTemplate(Object instance) {
        if(grailsApplication.mainContext.containsBean("sessionFactory")) {
            return new HibernateTemplate(grailsApplication.mainContext.sessionFactory, true)
        }

        return null
    }

    @Override
    Class getConstraintOwningClass(Object instance) {
        return constraint.constraintOwningClass
    }

    @Override
    String getConstraintPropertyName(Object instance) {
        return constraint.propertyName
    }

    @Override
    boolean getVeto(Object instance) {
        return RequestContextHolder.currentRequestAttributes().getAttribute(VETO_REQUEST_ATTRIBUTE, 0) ?: false
    }

    @Override
    void setVeto(Object instance, boolean val) {
        RequestContextHolder.currentRequestAttributes().setAttribute(VETO_REQUEST_ATTRIBUTE, val, 0)
    }

    CustomConstraintFactory.CustomConstraintClass getConstraint() {
        return RequestContextHolder.currentRequestAttributes().getAttribute(CONSTRAINT_REQUEST_ATTRIBUTE, 0)
    }
}
