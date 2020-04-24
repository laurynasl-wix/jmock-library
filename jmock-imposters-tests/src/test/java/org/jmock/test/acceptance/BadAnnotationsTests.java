package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.testdata.scalaexample.BadlyAnnotated;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class BadAnnotationsTests {
    Mockery context = new Mockery();

    @Test public void shouldMockScalaCaseClassWithFieldAnnotationOnParameter() {
        context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);

        final BadlyAnnotated mock = context.mock(BadlyAnnotated.class);
        final String result = "a mock result";
        context.checking(new Expectations() {
            {
                oneOf(mock).badlyAnnotated();
                will(returnValue(result));
            }
        });

        assertSame(mock.badlyAnnotated(), result);
    }
}
