@file:Suppress("NOTHING_TO_INLINE")

package dev.petuska.klip.runner

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

/**
 * @see [kotlinx.coroutines.test.TestResult]
 */
@ExperimentalCoroutinesApi
@Suppress("ACTUAL_WITHOUT_EXPECT", "ACTUAL_TYPE_ALIAS_TO_CLASS_WITH_DECLARATION_SITE_VARIANCE")
public actual typealias TestResult = Promise<Unit>

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
@ExperimentalCoroutinesApi
public actual inline fun runTest(
  context: CoroutineContext,
  dispatchTimeoutMs: Long,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult = runTest(
  context = context,
  dispatchTimeoutMs = dispatchTimeoutMs,
  testBody = testBody
)

/**
 * @see [kotlinx.coroutines.test.runTest]
 */
@ExperimentalCoroutinesApi
public actual inline fun TestScope.runTest(
  dispatchTimeoutMs: Long,
  noinline testBody: suspend TestScope.() -> Unit
): TestResult = runTest(
  dispatchTimeoutMs = dispatchTimeoutMs,
  testBody = testBody
)
