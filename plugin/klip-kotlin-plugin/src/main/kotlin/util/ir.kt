package dev.petuska.klip.plugin.util

import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.defaultType

@JvmName("hasAllIrValueParameter")
fun Collection<IrValueParameter>.hasAll(actual: Collection<IrValueParameter>): Boolean {
  return actual.all { a ->
    any { e ->
      a.name == e.name && a.type.classFqName == e.type.classFqName
    }
  }
}

@JvmName("hasAllIrTypeParameter")
fun Collection<IrTypeParameter>.hasAll(actual: Collection<IrTypeParameter>): Boolean {
  return actual.all { a ->
    any { e ->
      a.name == e.name && a.defaultType.classFqName == e.defaultType.classFqName
    }
  }
}
