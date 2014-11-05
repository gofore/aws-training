package com.gofore.aws.workshop.common.properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;

public class PropertyInterpolatorTest {

    @Test
    public void testPropertyInterpolation() {
        PropertyLoader loader = mock(PropertyLoader.class);
        when(loader.lookupOptional("prop")).thenReturn(Optional.of("{first}-second-{third}"));
        when(loader.lookupOptional("first")).thenReturn(Optional.of("F"));
        when(loader.lookupOptional("third")).thenReturn(Optional.of("T"));
        
        String value = new PropertyInterpolator(loader).lookup("prop");
        assertEquals("F-second-T", value);
    }
    
    @Test
    public void testMissingInterpolation() {
        PropertyLoader loader = mock(PropertyLoader.class);
        when(loader.lookupOptional("prop")).thenReturn(Optional.of("{first}-second-{third}"));
        when(loader.lookupOptional("first")).thenReturn(Optional.empty());
        when(loader.lookupOptional("third")).thenReturn(Optional.empty());

        String value = new PropertyInterpolator(loader).lookup("prop");
        assertEquals("{first}-second-{third}", value);
    }
    
    @Test
    public void testRecursiveInterpolation() {
        PropertyLoader loader = mock(PropertyLoader.class);
        when(loader.lookupOptional("first")).thenReturn(Optional.of("F-{second}"));
        when(loader.lookupOptional("second")).thenReturn(Optional.of("S-{third}"));
        when(loader.lookupOptional("third")).thenReturn(Optional.of("T"));

        String value = new PropertyInterpolator(loader).lookup("first");
        assertEquals("F-S-T", value);
    }
}