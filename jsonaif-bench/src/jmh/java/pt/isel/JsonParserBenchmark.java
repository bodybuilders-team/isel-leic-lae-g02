package pt.isel;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

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
    public void parseClassroomViaDynamicAndUnsafe() {
        JsonParserObjectsKt.jsonDynamicAndUnsafeParse(classroomJson, Classroom.class);
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

    @Benchmark
    public void parseDateViaDynamicAndUnsafe() {
        JsonParserObjectsKt.jsonDynamicAndUnsafeParse(date2Json, Date2.class);
    }

    @Benchmark
    public void parsePrimitiveDateViaDynamicAndUnsafe() {
        JsonParserObjectsKt.jsonDynamicAndUnsafeParse(dateJson, Date.class);
    }

    @Benchmark
    public void parsePrimitiveConstantDateViaReflection() {
        JsonParserObjectsKt.jsonReflectParse(dateJson, ConstantDate.class);
    }

    @Benchmark
    public void parsePrimitiveConstantDateViaDynamicAndUnsafe() {
        JsonParserObjectsKt.jsonDynamicAndUnsafeParse(dateJson, ConstantDate.class);
    }

    @Benchmark
    public void parseConstantDateViaReflection() {
        JsonParserObjectsKt.jsonReflectParse(date2Json, ConstantDate2.class);
    }

    @Benchmark
    public void parseConstantDateViaDynamicAndUnsafe() {
        JsonParserObjectsKt.jsonDynamicAndUnsafeParse(date2Json, ConstantDate2.class);
    }

}
