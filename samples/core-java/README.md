# hotel-koans

Modern Java (21+) koans built around a Hotel Services domain model. Each koan
is a JUnit test with a blank (`null`, a wrong value, or a placeholder) that you
fill in until the test passes. Modeled on
[java-stream-koans](https://github.com/MrKloan/java-stream-koans).

## Requirements

- JDK 21+ (no preview flags needed)
- Maven 3.9+

## Running

```bash
mvn test            # the whole suite — expect all koans red at first
mvn -q -Dtest=_04_StreamsFilterMapTest test   # one topic at a time
```

A freshly checked-out project shows **every koan red**. As you fill in a blank,
its test turns green. You are done when `mvn test` reports **86 tests, 0
failures**.

## Lesson order

| File | Topic | Koans |
|------|-------|-------|
| `_01_LambdaBasicsTest` | lambdas, capturing, the four method reference forms | 8 |
| `_02_FunctionalInterfacesTest` | custom `@FunctionalInterface`, default methods, `BiFunction`, `UnaryOperator` | 6 |
| `_03_PredicateFunctionConsumerSupplierTest` | `Predicate`/`Function`/`Consumer`/`Supplier` + combinators | 9 |
| `_04_StreamsFilterMapTest` | `filter`, `map`, `distinct`, `anyMatch`/`allMatch`/`noneMatch` | 7 |
| `_05_StreamsReduceCollectTest` | `reduce`, `collect` (`toList`, `joining`, `summingInt`, `averagingDouble`, `toMap`) | 8 |
| `_06_StreamsFlatMapSortedTest` | `flatMap`, `sorted` + `thenComparing`, `distinct` | 6 |
| `_07_StreamsGroupingByTest` | `groupingBy`, downstream collectors, `partitioningBy` | 6 |
| `_08_OptionalTest` | factories, `map`/`flatMap`/`filter`, `orElse`/`orElseGet`/`orElseThrow`, `ifPresentOrElse` | 6 |
| `_09_RecordsTest` | accessors, value equality, compact constructors, records in streams, record patterns | 6 |
| `_10_SealedClassesTest` | sealed hierarchy, switch exhaustiveness without `default`, record deconstruction | 5 |
| `_11_PatternMatchingSwitchTest` | switch expressions, arrow rules, `yield`, type patterns, guards, `case null` | 6 |
| `_12_MiscModernJavaTest` | text blocks (incl. indentation stripping, line continuation), `String.formatted`, `List.copyOf`, stream `toList()`, `yield` | 8 |
| `_13_VarTest` | `var` with the diamond operator, for-each, stream pipelines, try-with-resources, readability guideline | 5 |

## How a koan works

```java
/**
 * Optional.ofNullable wraps a value that may be null.
 */
@Test
void optional_of_nullable() {
    // TODO: koan — wrap the maybe-null assignee
    Optional<Staff> assignee = null;        // <-- your blank

    assertThat(assignee).isPresent();       // must turn green
}
```

The comment above each blank explains the concept and hints at the shape of
the answer. The assertions are the spec — no need to edit them.

## The sample data

All koans draw from `HotelFixtures` (`src/test/java/com/hotel/koans/fixtures/`),
whose Javadoc documents every value you might assert against: 8 rooms
(101..302, statuses, tags, rates), 3 facilities, 2 departments, 5 staff
(Maria, Jonas, Raj, Lena, Tom), 4 guests (Alice, Bob, Carol, Dave), 5 stays,
7 cleaning tasks, 4 maintenance requests, 3 inspections. Read that doc block
before solving — koans reference it (e.g. "the room with the highest rate is
302").

## Solutions

Per-topic unified diffs live in `src/test/resources/solutions/` (e.g.
`src/test/resources/solutions/_04_StreamsFilterMapTest.java.patch`). They show
the blank -> solved change for one file, so you can peek at one topic without
spoiling others.

**Recommended: read the patch, don't apply it.** It is plain text — open it
to see exactly what the blank should have been. Applying it overwrites your
own work.

If you want an apply/experiment/reset loop, do it once up front:

```bash
git init && git add .          # snapshot the pristine state
git apply src/test/resources/solutions/_04_StreamsFilterMapTest.java.patch   # see a solved topic
git checkout -- src/test       # back to blanks
```

The patches are verified: applied to a pristine copy, the whole suite is
green; reverted, it is fully red again.

## Layout

```
├── pom.xml                              Java 21, JUnit 5, AssertJ
├── README.md
├── hotel-koans-implementation-plan.md   (authoring plan, kept for reference)
├── hotel-services-domain-model.md       (domain model notes)
└── src/
    ├── main/java/com/hotel/domain/      the hotel domain model (real logic)
    └── test/
        ├── java/com/hotel/koans/
        │   ├── fixtures/HotelFixtures.java  shared sample data
        │   └── _01.._13  koan files (this is where you work)
        └── resources/solutions/         13 patches — one per topic file
```
