package com.prototype.microservice.edge.mvc.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.prototype.microservice.edge.mvc.view.ViewConstants;

/**
 * Helper class for working with ModelAndView objects.
 */
@Component
public class ModelAndViewHelper {

    @Value("${info.component}")
    private String componentName;

    @Value("${eureka.instance.metadataMap.instanceId:UNKNOWN INSTANCE}")
    private String instanceId;

    public ModelAndView buildDefaultMnV(final String viewName, Map<String, Object> model) {
        model.put(ViewConstants.F_COMPONENT_NAME, this.componentName);
        model.put(ViewConstants.F_INSTACNE_ID, instanceId);
        return new ModelAndView(viewName, model);
    }

}
