package dev.petuska.klip.runner

import dev.petuska.klip.core.stub
import kotlin.coroutines.CoroutineContext

/**
 * @see [kotlinx.coroutines.test.TestScope]
 */
public actual sealed interface TestScope

/**
 * @see [kotlinx.coroutines.test.TestResult]
 */
public actual class TestResult private constructor()

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
public actual inline fun runTest(
  context: CoroutineContext,
  dispatchTimeoutMs: Long,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult = stub

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
public actual inline fun TestScope.runTest(
  dispatchTimeoutMs: Long,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult = stub
