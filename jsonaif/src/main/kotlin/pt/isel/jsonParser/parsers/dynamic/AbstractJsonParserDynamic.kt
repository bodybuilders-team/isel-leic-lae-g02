package pt.isel.jsonParser.parsers.dynamic

import pt.isel.jsonParser.AbstractJsonParser
import java.lang.reflect.GenericSignatureFormatError
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Represents and Abstract Dynamic JSON parser.
 */
abstract class AbstractJsonParserDynamic : AbstractJsonParser() {

    /**
     * Gets the type of the list object.
     *
     * @param propertyKType JSON property KType
     * @param propertyKlass JSON property KClass
     *
     * @return representation of the list object type
     */
    protected fun getListObjectType(propertyKType: KType, propertyKlass: KClass<*>): KClass<*>? =
        if (propertyKlass == List::class) {
            val type = propertyKType.arguments.first().type
                ?: throw GenericSignatureFormatError("List generics cannot have star projection types")

            type.classifier as KClass<*>
        } else null
}
