package com.excilys.odataedmprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

public class EdmProvider extends CsdlAbstractEdmProvider {
    public static final String NAMESPACE = "cdb";

    public static final String CONTAINER_NAME = "container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(CONTAINER_NAME);

    public static final String COMPUTER_NAME = "Computer";
    public static final FullQualifiedName FULL_COMPUTER_NAME = new FullQualifiedName(NAMESPACE, COMPUTER_NAME);

    public static final String COMPANY_NAME = "Company";
    public static final FullQualifiedName FULL_COMPANY_NAME = new FullQualifiedName(COMPANY_NAME);

    public static final String ES_COMPUTERS_NAME = "Computers";
    public static final String ES_COMPANIES_NAME = "Companies";

    @Override
    public CsdlEntityType getEntityType(final FullQualifiedName entityTypeName) throws ODataException {
        if (entityTypeName.equals(FULL_COMPUTER_NAME)) {
            CsdlProperty id = new CsdlProperty().setName("id")
                    .setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            CsdlProperty introduced = new CsdlProperty().setName("introduced")
                    .setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
            CsdlProperty discontinued = new CsdlProperty().setName("discontinued")
                    .setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
            CsdlProperty company = new CsdlProperty().setName("company").setType(FULL_COMPANY_NAME);
            CsdlProperty name = new CsdlProperty().setName("name")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlEntityType et = new CsdlEntityType();
            et.setName(COMPUTER_NAME);
            et.setProperties(Arrays.asList(id, introduced, discontinued, company, name));
            et.setKey(Arrays.asList(propertyRef));
            return et;
        }

        if (entityTypeName.equals(FULL_COMPANY_NAME)) {
            CsdlProperty name = new CsdlProperty().setName("name")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty id = new CsdlProperty().setName("id")
                    .setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");
            CsdlEntityType et = new CsdlEntityType();
            et.setName(COMPUTER_NAME);
            et.setProperties(Arrays.asList(id, name));
            et.setKey(Arrays.asList(propertyRef));
            return et;
        }
        return null;
        // return super.getEntityType(entityTypeName);
    }

    @Override
    public CsdlEntitySet getEntitySet(final FullQualifiedName entityContainer, final String entitySetName)
            throws ODataException {
        if (entityContainer.equals(CONTAINER) && entitySetName.equals(ES_COMPUTERS_NAME)) {
            CsdlEntitySet set = new CsdlEntitySet();
            set.setName(ES_COMPUTERS_NAME);
            set.setType(FULL_COMPUTER_NAME);
            return set;
        }
        if (entityContainer.equals(CONTAINER) && entitySetName.equals(ES_COMPANIES_NAME)) {
            CsdlEntitySet set = new CsdlEntitySet();
            set.setName(ES_COMPANIES_NAME);
            set.setType(FULL_COMPANY_NAME);
            return set;
        }
        return null;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataException {

        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
        entitySets.add(getEntitySet(CONTAINER, ES_COMPUTERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_COMPANIES_NAME));

        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {
        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
        entityTypes.add(getEntityType(FULL_COMPUTER_NAME));
        entityTypes.add(getEntityType(FULL_COMPANY_NAME));
        schema.setEntityTypes(entityTypes);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
        schemas.add(schema);

        return schemas;

    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(final FullQualifiedName entityContainerName)
            throws ODataException {
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }
        return null;
    }
}
