package org.r7c.jaxb;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Plugin to set property public name as its private name.
 * <p>
 * This plugin is used to decrease property/field mismatch which causes the warnings.
 * It sets the public name of a property to its private name, if the public name does not start with an underscore.
 */
public class PropertyNamesPlugin extends Plugin {
    @Override
    public String getOptionName() {
        return "jaxb-fieldAsPropertyNames";
    }

    @Override
    public String getUsage() {
        return "  jaxb-fieldAsPropertyNames :  set property public name as its private name";
    }

    @Override
    public void postProcessModel(Model model, ErrorHandler errorHandler) {
        for (CClassInfo c : model.beans().values()) {
            for (CPropertyInfo prop : c.getProperties()) {
                if (prop.getName(false).startsWith("_")) {
                    continue;
                }
                String privateName = decapitalize(prop.getName(true));
                prop.setName(false, privateName);
            }
        }
    }

    /**
     *
     * Hibernate decapitalize to decrease property/field mismatch which causes the warnings.
     * Utility method to take a string and convert it to normal Java variable
     * name capitalization.  This normally means converting the first
     * character from upper case to lower case, but in the (unusual) special
     * case when there is more than one character and both the first and
     * second characters are upper case, we leave it alone.
     * <p>
     * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays
     * as "URL".
     *
     * @param  name The string to be decapitalized.
     * @return  The decapitalized version of the string.
     */
    public static String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    @Override
    public boolean run(Outline arg0, Options arg1, ErrorHandler arg2) {
        return true;
    }
}