package pt.isel.jsonParser.parsers.reflect.setters.property

import pt.isel.jsonParser.ParseException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties

fun getKProp(klass: KClass<*>, kParam: KParameter) =
    klass
        .memberProperties
        .firstOrNull { it.name == kParam.name }
        ?: throw ParseException("Property ${kParam.name} doesn't exist")
