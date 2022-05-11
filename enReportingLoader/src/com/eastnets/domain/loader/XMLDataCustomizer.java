package com.eastnets.domain.loader;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.xdb.DirectToXMLTypeMapping;

public class XMLDataCustomizer  implements DescriptorCustomizer {

    public void customize(final ClassDescriptor descriptor) throws Exception {
        descriptor.removeMappingForAttributeName("msgDocument");
        DirectToXMLTypeMapping mapping = new DirectToXMLTypeMapping();
        mapping.setAttributeName("msgDocument"); //name of the atribute on the Entity Bean
        mapping.setFieldName("XMLTEXT_DATA"); //name of the data base column
        descriptor.addMapping(mapping);
    }

}