package pt.isel;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonParserBenchmark {
    static final String classroomJson = "{students:[{ name: \"John\" }, { name: \"Mary\"}]}";
    static final String dateJson = "{ year: 1999, month: 9, day: 19}";
    static final String date2Json = "{ year: \"1999\", month: \"9\", day: \"19\"}";

    @Benchmark
    public void parseClassroomViaReflection() {
        JsonParserObjectsKt.jsonReflectParse(classroomJson, Classroom.class);
    }

    @Benchmark
    public void parseClassroomViaDynamic() {
        JsonParserObjectsKt.jsonDynamicParse(classroomJson, Classroom.class);
    }

    @Benchmark
    public void parseDateViaReflection() {
        JsonParserObjectsKt.jsonReflectParse(date2Json, Date2.class);
    }

    @Benchmark
    public void parsePrimitiveDateViaReflection() {
        JsonParserObjectsKt.jsonReflectParse(dateJson, Date.class);
    }

    @Benchmark
    public void parseDateViaDynamic() {
        JsonParserObjectsKt.jsonDynamicParse(date2Json, Date2.class);
    }

    @Benchmark
    public void parsePrimitiveDateViaDynamic() {
        JsonParserObjectsKt.jsonDynamicParse(dateJson, Date.class);
    }
}
