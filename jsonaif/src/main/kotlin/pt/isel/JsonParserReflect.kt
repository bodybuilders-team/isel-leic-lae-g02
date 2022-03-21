package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.*

fun <T : Any> KClass<T>.hasOptionalConstructor(): Boolean =
    constructors.any { constructor ->
        constructor.parameters.all(KParameter::isOptional)
    }


/**
 * JSON parser using reflection.
 */
object JsonParserReflect : AbstractJsonParser() {

    /**
     * For each domain class we keep a Map<String, Setter> relating properties names with their setters.
     * This is for Part 2 of Jsonaif workout.
     */
    private val setters = mutableMapOf<KClass<*>, Map<String, Setter>>()

    private val params = mutableMapOf<KClass<*>, Map<String, Params>>()


    override fun parsePrimitive(tokens: JsonTokens, klass: KClass<*>): Any? {
        val unparsedValue = tokens.popWordPrimitive().trim()  // TODO: 20/03/2022 Check this "trim" in the future
        if (unparsedValue == "null") return null

        val parseException = ParseException.unexpectedClass(unparsedValue, klass.qualifiedName!!)
        val parser = basicParser[klass] ?: throw parseException

        try {
            return parser(unparsedValue)
        } catch (err: NumberFormatException) {
            throw parseException
        } catch (err: IllegalArgumentException) {
            throw parseException
        }
    }


    override fun parseObject(tokens: JsonTokens, klass: KClass<*>): Any? { // TODO: 20/03/2022 Ask why can be null
        val optional = klass.hasOptionalConstructor()

        tokens.pop(OBJECT_OPEN)

        val instance = if (optional) klass.createInstance() else null

        val constructorParams = if (!optional) mutableMapOf<KParameter, Any?>() else null

        if (setters[klass] == null)
            setters[klass] = getSetters(klass)

        if (params[klass] == null)
            params[klass] = getParams(klass)

        while (tokens.current != OBJECT_END) {
            val propName = tokens.popWordFinishedWith(COLON).trim()

            if (optional) {
                val setter = setters[klass]!![propName] ?: throw ParseException("Parameter $propName doesn't exist")
                setter.apply(target = instance!!, tokens)
            } else {
                val params = params[klass]!![propName] ?: throw ParseException("Parameter $propName doesn't exist")
                params.add(params = constructorParams!!, tokens)
            }

            if (tokens.current == COMMA)
                tokens.pop(COMMA)
        }

        tokens.pop(OBJECT_END)


        return if (!optional)
            klass.primaryConstructor?.callBy(constructorParams!!)

        else instance
    }

    private fun getParams(klass: KClass<*>): Map<String, Params> =
        klass.primaryConstructor!!.parameters.associate { kParam ->
            Pair(kParam.name!!,
                object : Params {
                    override fun add(params: MutableMap<KParameter, Any?>, tokens: JsonTokens) {
                        val propValue = parse(tokens, kParam.type)

                        params[kParam] = propValue
                    }

                })
        }

    /**
     * Gets the [klass] properties setters.
     * @param klass representation of a class
     * @return map with pairs PropertyName : PropertySetter
     */
    private fun getSetters(klass: KClass<*>): Map<String, Setter> =
        klass.memberProperties.associate { kProp ->
            Pair(
                kProp.name,
                object : Setter {
                    override fun apply(target: Any, tokens: JsonTokens) {
                        val propValue = parse(tokens, kProp.returnType)

                        (kProp as KMutableProperty<*>).setter.call(target, propValue)
                    }
                }
            )
        }

    private fun parse(tokens: JsonTokens, type: KType): Any? {
        val klass = type.classifier as KClass<*>
        val propValue =
            if (type.classifier == List::class)
                parse(tokens, type.arguments[0].type!!.classifier!! as KClass<*>)
            else
                parse(tokens, type.classifier as KClass<*>)

        if (propValue == null && !type.isMarkedNullable)
            throw ParseException("Value of property with type ${klass.qualifiedName} should not be null")

        return propValue
    }

}
