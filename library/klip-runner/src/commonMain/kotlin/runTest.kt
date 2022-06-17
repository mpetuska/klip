package dev.petuska.klip.runner

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @see [kotlinx.coroutines.test.TestScope]
 */
public expect sealed interface TestScope

/**
 * @see [kotlinx.coroutines.test.TestResult]
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class TestResult

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
public expect inline fun runTest(
  context: CoroutineContext = EmptyCoroutineContext,
  dispatchTimeoutMs: Long = 60_000L,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
public expect inline fun TestScope.runTest(
  dispatchTimeoutMs: Long = 60_000L,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult
