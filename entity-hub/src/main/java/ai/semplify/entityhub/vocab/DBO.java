package ai.semplify.entityhub.vocab;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class DBO {
    public static final String NAMESPACE = "http://dbpedia.org/ontology/";
    public static final String PREFIX = "dbo";

    public static final IRI THUMBNAIL;
    public static final IRI ABSTRACT;

    static {
        ValueFactory factory = SimpleValueFactory.getInstance();
        THUMBNAIL = factory.createIRI(NAMESPACE, "thumbnail");
        ABSTRACT = factory.createIRI(NAMESPACE, "abstract");
    }
}
