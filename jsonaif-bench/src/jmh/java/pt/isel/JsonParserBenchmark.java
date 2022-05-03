package pt.isel;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import pt.isel.jsonParser.parsers.dynamic.JsonParserDynamic;
import pt.isel.jsonParser.parsers.reflect.JsonParserReflect;

import static pt.isel.JsonParserObjectsKt.parseDate;
import static pt.isel.JsonParserObjectsKt.parsePerson;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonParserBenchmark {

    static final String personJson = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";
    static final String dateJson = "{ name: \"Ze Manel\", birth: { year: 1999, month: 9, day: 19}}";

    @Benchmark
    public void parsePersonViaReflection() {
        parsePerson(personJson, JsonParserReflect.INSTANCE);
    }

    @Benchmark
    public void parsePersonViaDynamic() {
        parsePerson(personJson, JsonParserDynamic.INSTANCE);
    }

    @Benchmark
    public void parseDateViaReflection() {
        parseDate(dateJson, JsonParserReflect.INSTANCE);
    }

    @Benchmark
    public void parseDateViaDynamic() {
        parseDate(dateJson, JsonParserDynamic.INSTANCE);
    }
}
