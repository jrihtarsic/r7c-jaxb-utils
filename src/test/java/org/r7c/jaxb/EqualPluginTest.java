package org.r7c.jaxb;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.junit.jupiter.api.Test;
import org.xml.sax.ErrorHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EqualPluginTest {

    @Test
    void testGetOptionName() {
        EqualPlugin plugin = new EqualPlugin();
        assertEquals("jaxb-equals", plugin.getOptionName());
    }

    @Test
    void testGetUsage() {
        EqualPlugin plugin = new EqualPlugin();
        assertNotNull(plugin.getUsage());
    }

    @Test
    void testIsIgnoredFieldOutline() {
        // This test requires mocking FieldOutline and its methods.
        // You can use a mocking framework like Mockito for a full test.
        // Here is a simple structure:
        FieldOutline fieldOutline = mock(FieldOutline.class);
        when(fieldOutline.getRawType()).thenReturn(mock(JType.class));
        when(fieldOutline.getPropertyInfo()).thenReturn(
                mock(com.sun.tools.xjc.model.CPropertyInfo.class)
        );
        EqualPlugin plugin = new EqualPlugin();
        // The actual assertion depends on the mock setup.
        assertFalse(plugin.isIgnored(fieldOutline));
    }

    @Test
    void testHasSuperClass() {
        ClassOutline classOutline = mock(ClassOutline.class);
        when(classOutline.getSuperClass()).thenReturn(null);
        EqualPlugin plugin = new EqualPlugin();
        assertFalse(plugin.hasSuperClass(classOutline));
    }

    @Test
    void testIsIgnoredClassOutline() {
        ClassOutline classOutline = mock(ClassOutline.class);
        when(classOutline.getImplClass()).thenReturn(
                mock(com.sun.codemodel.JDefinedClass.class)
        );
        EqualPlugin plugin = new EqualPlugin();
        assertFalse(plugin.isIgnored(classOutline));
    }

    @Test
    void testRun()  {
        Outline outline = mock(Outline.class);
        ClassOutline classOutline = mock(ClassOutline.class);
        JDefinedClass jDefinedClass = mock(JDefinedClass.class);
        JCodeModel codeModel = mock(JCodeModel.class);

        when(classOutline.getImplClass()).thenReturn(jDefinedClass);
        when(jDefinedClass.owner()).thenReturn(codeModel);
        when(jDefinedClass.method(anyInt(), eq(boolean.class), eq("equals"))).thenReturn(mock(com.sun.codemodel.JMethod.class));

        Options options = mock(Options.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);

        EqualPlugin plugin = new EqualPlugin();
        boolean result = plugin.run(outline, options, errorHandler);

        assertTrue(result);
        verify(outline).getClasses();
    }
}