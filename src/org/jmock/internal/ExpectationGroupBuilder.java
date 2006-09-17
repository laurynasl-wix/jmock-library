package org.jmock.internal;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsSame;
import org.jmock.core.Action;
import org.jmock.core.Expectation;
import org.jmock.core.ExpectationGroup;
import org.jmock.lib.InvocationExpectation;
import org.jmock.lib.action.ActionSequence;
import org.jmock.lib.action.DoAllAction;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.ThrowAction;
import org.jmock.syntax.ReceiverClause;

public class ExpectationGroupBuilder implements ExpectationBuilder {
    private final ExpectationGroup group;
    private InvocationExpectation expectation;
    private InvocationExpectationBuilder expectationBuilder;
    
    protected ExpectationGroupBuilder(ExpectationGroup expectationGroup) {
        this.group = expectationGroup;
    }
    
    private void initialiseExpectationCapture(int requiredInvocationCount, int maximumInvocationCount) {
        checkLastExpectationWasFullySpecified();
        
        expectation = new InvocationExpectation();
        expectation.setCardinality(requiredInvocationCount, maximumInvocationCount);
        expectationBuilder = new InvocationExpectationBuilder(expectation, group);
    }

    public Expectation toExpectation() {
        checkLastExpectationWasFullySpecified();
        return group;
    }
    
    private void checkLastExpectationWasFullySpecified() {
        if (expectationBuilder != null) {
            expectationBuilder.checkWasFullySpecified();
        }
    }
    
    /* 
     * Syntactic sugar
     */
    
    public ReceiverClause exactly(int count) {
        initialiseExpectationCapture(count, count);
        return expectationBuilder;
    }
    
    public ReceiverClause atLeast(int count) {
        initialiseExpectationCapture(count, Integer.MAX_VALUE);
        return expectationBuilder;
    }
    
    public ReceiverClause between(int minCount, int maxCount) {
        initialiseExpectationCapture(minCount, maxCount);
        return expectationBuilder;
    }
    
    public ReceiverClause atMost(int count) {
        initialiseExpectationCapture(0, count);
        return expectationBuilder;
    }
    
    public <T> T allowing(T mockObject) {
        return atLeast(0).of(mockObject);
    }
    
    public <T> void ignoring(T mockObject) {
        atLeast(0).of(mockObject);
    }
    
    public <T> T never(T mockObject) {
        return exactly(0).of(mockObject);
    }
    
    private void addParameterMatcher(Matcher<?> matcher) {
        if (expectationBuilder == null) {
            throw new IllegalStateException(UnspecifiedExpectation.ERROR);
        }
        
        expectationBuilder.addParameterMatcher(matcher);
    }
    
    public <T> T with(Matcher<T> matcher) {
        addParameterMatcher(matcher);
        return null;
    }

    public boolean with(Matcher<Boolean> matcher) {
        addParameterMatcher(matcher);
        return Boolean.FALSE;
    }
    
    public byte with(Matcher<Byte> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public short with(Matcher<Short> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public int with(Matcher<Integer> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public long with(Matcher<Long> matcher) {
        addParameterMatcher(matcher);
        return 0;
    }

    public float with(Matcher<Float> matcher) {
        addParameterMatcher(matcher);
        return 0.0f;
    }

    public double with(Matcher<Double> matcher) {
        addParameterMatcher(matcher);
        return 0.0;
    }

    public void will(Action action) {
        expectation.setAction(action);
    }
    
    public void expects(ExpectationGroupBuilder subgroupBuilder) {
        group.add(subgroupBuilder.toExpectation());
    }
    
    /* Common constraints
     */
    
    public <T> Matcher<T> equal(T value) {
        return IsEqual.eq(value);
    }
    
    public <T> Matcher<T> same(T value) {
        return IsSame.same(value);
    }
    
    @SuppressWarnings("unused")
    public <T> Matcher<T> any(Class<T> type) {
        return IsAnything.<T>anything();
    }
    
    public <T> Matcher<T> anything() {
        return IsAnything.anything();
    }
    
    public Matcher<?> a(Class<?> type) {
        return IsInstanceOf.isA(type);
    }
    
    public Matcher<?> an(Class<?> type) {
        return IsInstanceOf.isA(type);
    }
    
    /* Common actions
     */
    
    public Action returnValue(Object result) {
        return new ReturnValueAction(result);
    }
    
    public Action throwException(Throwable throwable) {
        return new ThrowAction(throwable);
    }
    
    public Action doAll(Action...actions) {
        return new DoAllAction(actions);
    }
    
    public Action onConsecutiveCalls(Action...actions) {
        return new ActionSequence(actions);
    }
}
