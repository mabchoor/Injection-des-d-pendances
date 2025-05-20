package framework.xml;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ConstructorArg {
    private String type;
    private String ref;

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
} 