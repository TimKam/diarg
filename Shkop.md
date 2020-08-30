# The Shkop approach
DiArg supports the Shkop approach to abstract argumentation.
This tutorial explains the approach *hands-on*.

A key question in abstract argumentation is how to determine the extensions of an argumentation framework.
To answer this question, the Shkop approach first simplifies the problem by assuming that any argumentation process is
sequential, *i.e.*, that instead of one argumentation framework, we resolve a *sequence of argumentation frameworks* and
that given an argumentation framework sequence AFS = <AF<sub>0</sub>, ..., AF<sub>n</sub>>, AF<sub>0</sub> contains
exactly one argument and each AF<sub>i</sub>, 0 < i &le; n, has the following properties:

* AF<sub>i</sub> is a normal expansion of AF<sub>i-1</sub>;
* AF<sub>i</sub> contains exactly one more argument than AF<sub>i-1</sub>.

We call any argumentation framework sequence with these properties a *Shkop sequence*, which we can resolve as follows,
starting with i=0:

1. Check if the "new" argument in AF<sub>i</sub> closes a loop.
2. If so, remove the argument from all argumentation frameworks in the Shop sequence.
3. Determine the unique grounded extension of the argumentation framework AF<sub>i</sub>.
   Note that after the deletion of all arguments that close loops, AF<sub>i</sub> is necessarily non-cyclic.
4. If i < n, set i := i+1 and go to 1.

We call this approach *sequential Shkop*.
With DiArg, we can create and resolve Shkop sequences as follows, for example:

```java
AFSequence shkopSequence = new AFSequence(SequenceType.SHKOP, ResolutionType.SHKOP, new Semantics(SemanticsType.SHKOP),
                           true);

DungTheory framework0 = new DungTheory();
DungTheory framework1 = new DungTheory();
DungTheory framework2 = new DungTheory();

Argument a = new Argument("a");
Argument b = new Argument("b");
Argument c = new Argument("c");

framework0.add(a);

framework1.add(a);
framework1.add(b);
framework1.add(new Attack(a, b));
framework1.add(new Attack(b, a));

framework2.add(framework1);
framework2.add(c);
framework2.add(new Attack(b,c));
framework2.add(new Attack(c,a));

shkopSequence.addFramework(framework0);
shkopSequence.addFramework(framework1);
shkopSequence.addFramework(framework2);

shkopSequence.resolveFrameworks();

```

To turn the Shkop approach into an abstract argumentation semantics, we proceed as follows, given any argumentation
framework AF = (AR, AT):

1. Determine all permutation sequences of the set of arguments AR.
2. For every permutation sequence <a<sub>0</sub>, ..., a<sub>m</sub>>, proceed as follows:  
    2.1.  Generate a Shkop sequence <AF<sub>0</sub>, ..., AF<sub>m</sub>>, where for any AF<sub>j</sub>,
          0 &le; j &le; m, AF<sub>j</sub> is the restriction of AF to the set of arguments in
          <a<sub>0</sub>, ..., a<sub>j</sub>>.  
    2.2.  Determine the *Shkop resolution* of the Shkop sequence by applying the sequential Shkop approach.
3. Return the set of all *Shkop resolutions* as the argumentation framework's extensions.

With DiArg, the Shkop extensions of an argumentation framework can be determine as follows, for example:

```java
SimpleShkopReasoner shkopReasoner = new SimpleShkopReasoner();

DungTheory framework = new DungTheory();

framework.add(a);
framework.add(b);
framework.add(c);
framework.add(new Attack(a, b));
framework.add(new Attack(b, a));
framework.add(new Attack(b, c));
framework.add(new Attack(c, c));

Collection<Extension> extensions = shkopReasoner.getModels(framework);
```
    


