# r7c-jaxb-utils

A collection of custom JAXB utils for jaxb2 plugins to generate enhanced java
classes from the JAXB schema bindings.   


== Build badges
![Java CI](https://github.com/jrihtarsic/ddlgen-maven-plugin/workflows/Java%20CI/badge.svg)


| Sonar Maintability Rating | Sonar Reliability Rating | Sonar Security Rating | Sonar Bugs Count | Sonar Vulnerabilities Count |
|--------------------------|-------------------------|----------------------|------------------|----------------------------|
| ![Maintability](https://sonarcloud.io/api/project_badges/measure?project=jrihtarsic_r7c-jaxb-utils&metric=sqale_rating) | ![Reliability](https://sonarcloud.io/api/project_badges/measure?project=jrihtarsic_r7c-jaxb-utils&metric=reliability_rating) | ![Security](https://sonarcloud.io/api/project_badges/measure?project=jrihtarsic_r7c-jaxb-utils&metric=security_rating) | ![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jrihtarsic_r7c-jaxb-utils&metric=bugs) | ![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=jrihtarsic_r7c-jaxb-utils&metric=vulnerabilities) |

https://sonarcloud.io/dashboard?id=jrihtarsic_r7c-jaxb-utils





## Features

- `EqualPlugin`: Adds `equals()` method generation to JAXB classes.
- `HashCodePlugin`: Adds `hashCode()` method generation to JAXB classes.
- `PropertyNamesPlugin`: Generates field names to match setters and getters to mach hibernate name
  conventions, e.g. `getFieldName()` will generate a field named `fieldName` instead of `field_name`. Thus the generated classes can be used with Hibernate ORM without additional configuration.

## Requirements

- Java 17 or higher
- Maven 3.6+

## Usage

Add the plugin JAR to your JAXB XJC tool plugins directory or use it as a Maven dependency in your JAXB code generation step.

Example Maven usage:
```xml
<plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>jaxb2-maven-plugin</artifactId>
        <configuration>
            <arguments>
                <argument>-jaxb-equals</argument>
                <argument>-jaxb-hashcode</argument>
                <argument>-jaxb-fieldAsPropertyNames</argument>
            </arguments>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>com.as4mail.jaxb</groupId>
                <artifactId>jaxb-utils</artifactId>
            </dependency>
        </dependencies>
    </plugin>
</plugins>
```


The configuration above will enable the `EqualPlugin`, `HashCodePlugin`, and `PropertyNamesPlugin` during the JAXB code generation process. And the xsd schema below 
```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ns1="http://laurentius.si/property"
targetNamespace="http://laurentius.si/property" elementFormDefault="qualified" >
<xs:element name="SEDProperty" type="ns1:SEDProperty"/>
<xs:complexType name="SEDProperty">
<xs:annotation>
<xs:documentation xml:lang="sl">SED property</xs:documentation>
</xs:annotation>

        <xs:attribute name="key" type="xs:string" use="required">
            <xs:annotation>
                <xs:documentation xml:lang="sl">Property key</xs:documentation>
            </xs:annotation>
        </xs:attribute>
        <xs:attribute name="value" type="xs:string" use="required">
            <xs:annotation>
                <xs:documentation xml:lang="sl">Property value</xs:documentation>
            </xs:annotation>
        </xs:attribute>
        <xs:attribute name="group" type="xs:string" use="required">
            <xs:annotation>
                <xs:documentation xml:lang="sl">Property group</xs:documentation>
            </xs:annotation>
        </xs:attribute>
    </xs:complexType>
</xs:schema>
```

will generate the following Java class below. Please note the class methods `equals()`, `hashCode()`.

```java
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SEDProperty")
@XmlRootElement(name = "SEDProperty")
public class SEDProperty
    implements Serializable
{
    @XmlAttribute(name = "key", required = true)
    protected String key;
    @XmlAttribute(name = "value", required = true)
    protected String value;
    @XmlAttribute(name = "group", required = true)
    protected String group;

    public String getKey() {
        return key;
    }
    
    public void setKey(String value) {
        this.key = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String value) {
        this.group = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null)||(this.getClass()!= object.getClass())) {
            return false;
        }
        SEDProperty thatObject = ((SEDProperty) object);
        return Objects.equals(this.key, thatObject.key) && Objects.equals(this.value, thatObject.value) && Objects.equals(this.group, thatObject.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value, this.group);
    }
}
```