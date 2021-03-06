package diarg;

import diarg.enums.ResolutionType;
import diarg.enums.SemanticsType;
import diarg.enums.SequenceType;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AFSequenceTest {
    TestFrameworks testFrameworks;
    DungTheory oneArgFramework = new DungTheory();
    AFSequence standardSequence, expandingSequence, eriSequence, rriSequence, ecmSequence, rcmSequence,
            shkopSequence1, shkopSequence2, shkopSequence3;
    Argument a, b, c;
    Semantics nsaCF2Semantics;

    @BeforeAll
    public void init() {
        testFrameworks = new TestFrameworks();
        nsaCF2Semantics = new Semantics(SemanticsType.NSACF2);
        a = new Argument("a");
        b = new Argument("b");
        c = new Argument("c");
        oneArgFramework.add(a);
    }

    @BeforeEach
    public void empty() {
        standardSequence = new AFSequence(SequenceType.STANDARD, ResolutionType.STANDARD, nsaCF2Semantics, true);
        expandingSequence = new AFSequence(SequenceType.EXPANDING, ResolutionType.STANDARD, nsaCF2Semantics, true);
        eriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                nsaCF2Semantics, true);
        rriSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_REFERENCE_INDEPENDENT,
                nsaCF2Semantics, true);
        ecmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.EXPANSIONIST_CAUTIOUSLY_MONOTONIC,
                nsaCF2Semantics, true);
        rcmSequence = new AFSequence(SequenceType.NORMALLY_EXPANDING, ResolutionType.REDUCTIONIST_CAUTIOUSLY_MONOTONIC,
                nsaCF2Semantics, true);
        shkopSequence1 = new AFSequence(SequenceType.SHKOP, ResolutionType.SHKOP, new Semantics(SemanticsType.SHKOP),
                true);
        shkopSequence2 = new AFSequence(SequenceType.SHKOP, ResolutionType.SHKOP, new Semantics(SemanticsType.SHKOP),
                true);
        shkopSequence3 = new AFSequence(SequenceType.SHKOP, ResolutionType.SHKOP, new Semantics(SemanticsType.SHKOP),
                true);
    }

    @Test
    void addFramework() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        assertEquals(2, standardSequence.getFrameworks().size());

        expandingSequence.addFramework(testFrameworks.framework3);
        expandingSequence.addFramework(testFrameworks.framework4);
        expandingSequence.addFramework(testFrameworks.framework2);
        assertEquals(2, expandingSequence.getFrameworks().size());

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework2);
        assertEquals(2, eriSequence.getFrameworks().size());

        shkopSequence1.addFramework(testFrameworks.framework5);
        assertEquals(0, shkopSequence1.getFrameworks().size());
        shkopSequence1.addFramework(oneArgFramework);
        shkopSequence1.addFramework(testFrameworks.framework5);
        shkopSequence1.addFramework(testFrameworks.framework3);
        assertEquals(2, shkopSequence1.getFrameworks().size());
        shkopSequence1.addFramework(testFrameworks.framework2);
        assertEquals(3, shkopSequence1.getFrameworks().size());
    }

    @Test
    void removeFramework() {
        expandingSequence.addFramework(testFrameworks.framework4);
        expandingSequence.addFramework(testFrameworks.framework2);
        expandingSequence.removeFramework(1);
        assertEquals(1, expandingSequence.getFrameworks().size());
        expandingSequence.removeFramework(0);
        assertEquals(0, expandingSequence.getFrameworks().size());

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        eriSequence.removeFramework(1);
        assertEquals(2, eriSequence.getFrameworks().size());
        eriSequence.removeFramework(0);
        assertEquals(1, eriSequence.getFrameworks().size());
    }

    @Test
    void resolveFramework() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        Extension resolutionIs1a = standardSequence.resolveFramework(1);
        assertNull(resolutionIs1a);
        Extension resolutionIs1b = standardSequence.resolveFramework(0);
        Extension resolutionShould1b = new Extension();
        resolutionShould1b.add(a);
        assertEquals(1, resolutionIs1b.size());
        assertTrue(resolutionIs1b.containsAll(resolutionShould1b));

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs2a = eriSequence.resolveFramework(0);
        Extension resolutionShould2a = new Extension();
        resolutionShould2a.add(a);
        assertTrue(resolutionIs2a.containsAll(resolutionShould2a));
        Extension resolutionShould2b = new Extension();
        resolutionShould2b.add(c);
        Extension resolutionIs2b = eriSequence.resolveFramework(1);
        assertTrue(resolutionIs2b.containsAll(resolutionShould2b));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs3a = rriSequence.resolveFramework(0);
        Extension resolutionShould3 = new Extension();
        resolutionShould3.add(a);
        assertTrue(resolutionIs3a.containsAll(resolutionShould3));
        Extension resolutionShould3b = new Extension();
        resolutionShould3b.add(c);
        Extension resolutionIs3b = rriSequence.resolveFramework(1);
        assertTrue(resolutionIs3b.containsAll(resolutionShould3b));

        ecmSequence.addFramework(testFrameworks.framework4);
        ecmSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs4a = ecmSequence.resolveFramework(0);
        Extension resolutionShould4a = new Extension();
        resolutionShould4a.add(a);
        assertTrue(resolutionIs4a.containsAll(resolutionShould4a));
        Extension resolutionShould4b = new Extension();
        Extension resolutionIs4b = ecmSequence.resolveFramework(1);
        assertTrue(resolutionIs4b.containsAll(resolutionShould4b));

        rcmSequence.addFramework(testFrameworks.framework4);
        rcmSequence.addFramework(testFrameworks.framework3);
        Extension resolutionIs5a = rcmSequence.resolveFramework(0);
        Extension resolutionShould5a = new Extension();
        resolutionIs5a.add(a);
        assertTrue(resolutionIs5a.containsAll(resolutionShould5a));
        Extension resolutionShould5b = new Extension();
        Extension resolutionIs5b = rcmSequence.resolveFramework(1);
        assertTrue(resolutionIs5b.containsAll(resolutionShould5b));

        shkopSequence3.addFramework(oneArgFramework);
        shkopSequence3.addFramework(testFrameworks.framework5);
        shkopSequence3.addFramework(testFrameworks.framework2);
        Extension resolutionIs6a = shkopSequence3.resolveFramework(0);
        Extension resolutionShould6a = new Extension();
        resolutionShould6a.add(a);
        assertTrue(resolutionIs6a.containsAll(resolutionShould6a));
        Extension resolutionIs6b = shkopSequence3.resolveFramework(1);
        assertTrue(resolutionIs6b.containsAll(resolutionShould6a));
        Extension resolutionIs6c = shkopSequence3.resolveFramework(2);
        Extension resolutionShould6c = new Extension();
        resolutionShould6c.add(a);
        assertTrue(resolutionIs6c.containsAll(resolutionShould6c));

        shkopSequence2.addFramework(oneArgFramework);
        DungTheory bAttacksAAF = new DungTheory();
        bAttacksAAF.add(a);
        bAttacksAAF.add(b);
        bAttacksAAF.add(new Attack(b, a));
        shkopSequence2.addFramework(bAttacksAAF);
        Extension resolutionIs7a = shkopSequence2.resolveFramework(0);
        Extension resolutionShould7a = new Extension();
        resolutionShould7a.add(a);
        assertEquals(1, resolutionIs7a.size());
        assertTrue(resolutionIs7a.containsAll(resolutionShould7a));
        Extension resolutionIs7b = shkopSequence2.resolveFramework(1);
        Extension resolutionShould7b = new Extension();
        resolutionShould7b.add(b);
        assertEquals(1, resolutionIs7b.size());
        assertTrue(resolutionIs7b.containsAll(resolutionShould7b));

    }

    @Test
    void resolveFrameworks() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        Extension resolutionShoulda = new Extension();
        resolutionShoulda.add(a);
        Collection<Extension> resolutionsShould1 = new LinkedList<>();
        resolutionsShould1.add(resolutionShoulda);
        resolutionsShould1.add(resolutionShoulda);
        Collection<Extension> resolutionsIs1 = standardSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs1.size());
        assertTrue(resolutionsIs1.containsAll(resolutionsShould1));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs2 = rriSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs2.size());
        assertTrue(resolutionsIs2.containsAll(resolutionsShould1));

        eriSequence.addFramework(testFrameworks.framework4);
        eriSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs3 = eriSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs3.size());
        assertTrue(resolutionsIs3.containsAll(resolutionsShould1));

        rcmSequence.addFramework(testFrameworks.framework4);
        rcmSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs4 = rcmSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs4.size());
        assertTrue(resolutionsIs4.containsAll(resolutionsShould1));

        ecmSequence.addFramework(testFrameworks.framework4);
        ecmSequence.addFramework(testFrameworks.framework3);
        Collection<Extension> resolutionsIs5 = ecmSequence.resolveFrameworks();
        assertEquals(2, resolutionsIs5.size());
        assertTrue(resolutionsIs5.containsAll(resolutionsShould1));

        shkopSequence1.addFramework(oneArgFramework);
        shkopSequence1.addFramework(testFrameworks.framework5);
        shkopSequence1.addFramework(testFrameworks.framework2);
        Collection<Extension> resolutionsIs6 = shkopSequence1.resolveFrameworks();
        Collection<Extension> resolutionsShould6 = new LinkedList<>();
        Extension aExtension = new Extension();
        aExtension.add(a);
        resolutionsShould6.add(aExtension);
        assertTrue(resolutionsIs6.containsAll(resolutionsShould6));
        assertTrue(resolutionsShould6.containsAll(resolutionsIs6));
    }

    @Test
    void determineExtensions() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        standardSequence.resolveFramework(0);
        Extension resolutionShould1a = new Extension();
        resolutionShould1a.add(a);
        Extension resolutionShould1b = new Extension();
        resolutionShould1b.add(b);
        Extension resolutionShould1c = new Extension();
        resolutionShould1c.add(c);
        Collection<Extension> extensionsShould1a = new LinkedList<>();
        extensionsShould1a.add(resolutionShould1a);
        extensionsShould1a.add(resolutionShould1b);
        extensionsShould1a.add(resolutionShould1c);
        Collection<Extension> extensionsIs1a = standardSequence.determineExtensions(0);
        assertEquals(3, extensionsIs1a.size());
        assertTrue(extensionsIs1a.containsAll(extensionsShould1a));
        Collection<Extension> extensionsShould1b = new LinkedList<>();
        extensionsShould1b.add(resolutionShould1a);
        Collection<Extension> extensionsIs1b = standardSequence.determineExtensions(1);
        assertEquals(1, extensionsIs1b.size());
        assertTrue(extensionsIs1b.containsAll(extensionsShould1b));

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        assertTrue(rriSequence.resolveFramework(1) == null);
    }

    @Test
    void determineResolvableFrameworks() {
        standardSequence.addFramework(testFrameworks.framework3);
        standardSequence.addFramework(testFrameworks.framework4);
        assertTrue(standardSequence.determineResolvableFrameworks(0) == null);

        rriSequence.addFramework(testFrameworks.framework4);
        rriSequence.addFramework(testFrameworks.framework3);
        Collection<DungTheory> resolvableFrameworks1 = rriSequence.determineResolvableFrameworks(1);
        assertTrue(resolvableFrameworks1 == null);

        Collection<DungTheory> resolvableFrameworks2 = rriSequence.determineResolvableFrameworks(0);
        Collection<DungTheory> resolvableFrameworksShould2 = new LinkedList<>();
        resolvableFrameworksShould2.add(testFrameworks.framework4);
        assertEquals(1, resolvableFrameworks2.size());
        assertTrue(resolvableFrameworks2.containsAll(resolvableFrameworksShould2));

        rriSequence.resolveFramework(0);
        Collection<DungTheory> resolvableFrameworks3 = rriSequence.determineResolvableFrameworks(1);
        DungTheory resolvableFrameworkShould3b = new DungTheory();
        resolvableFrameworkShould3b.add(a);
        resolvableFrameworkShould3b.add(c);
        resolvableFrameworkShould3b.add(new Attack(c, a));
        assertEquals(2, resolvableFrameworks3.size());
        Iterator<DungTheory> iterator1 = resolvableFrameworks3.iterator();
        DungTheory resolvableFrameworkIs3a = iterator1.next();
        DungTheory resolvableFrameworkIs3b = iterator1.next();
        assertTrue(resolvableFrameworkIs3a.prettyPrint().equals(resolvableFrameworkShould3b.prettyPrint()));
        assertTrue(resolvableFrameworkIs3b.prettyPrint().equals(testFrameworks.framework4.prettyPrint()));

        Extension resolutionShould3a = nsaCF2Semantics.getModel(resolvableFrameworkIs3a);
        Extension resolutionIs3a = rriSequence.resolveFramework(1, resolutionShould3a);
        Extension resolutionShould3b = nsaCF2Semantics.getModel(resolvableFrameworkIs3b);
        Extension resolutionIs3b = rriSequence.resolveFramework(1, resolutionShould3b);
        assertEquals(resolutionShould3a.size(), resolutionIs3a.size());
        assertTrue(resolutionIs3a.containsAll(resolutionShould3a));
        assertEquals(resolutionShould3b.size(), resolutionIs3b.size());
        assertTrue(resolutionIs3b.containsAll(resolutionShould3b));
    }

    @Test
    // Note: this is more like integration test; we test if AFSequences manages contexts correctly.
    public void testContext() {
        AFSequence sequence = new AFSequence(
                SequenceType.NORMALLY_EXPANDING,
                ResolutionType.EXPANSIONIST_REFERENCE_INDEPENDENT,
                nsaCF2Semantics, true
        );
        DungTheory framework0 = testFrameworks.framework3;
        sequence.addFramework(framework0);
        Extension resolution0 = sequence.resolveFramework(0);
        DungTheory framework1 = new DungTheory();
        framework1.add(framework0);
        Argument d = new Argument("d");
        framework1.add(d);
        framework1.add(new Attack(d, c));
        sequence.addFramework(framework1);
        Extension resolution1 = sequence.resolveFramework(1);
        Extension resolutionShould1 = new Extension();
        resolutionShould1.add(a);
        resolutionShould1.add(d);
        assertEquals(2, resolution1.size());
        assertTrue(resolution1.containsAll(resolutionShould1));
        Extension contextArguments = new Extension();
        contextArguments.add(d);
        Context context = new Context("weekend", contextArguments);
        Collection<Context> contexts = new LinkedList();
        contexts.add(context);
        sequence.addFramework(framework1, contexts);
        Extension resolution2 = sequence.resolveFramework(2);
        assertEquals(1, resolution2.size());
        assertTrue(resolution2.equals(resolution0));
    }

}
