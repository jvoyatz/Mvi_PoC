package dev.jvoyatz.newarch.mvipoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description



@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    /*private*/ val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}


object TestUtils {
    /**
     *
     * used to run unit (local) tests
     * specifies the test dispatcher
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun runWithTestDispatcher(
        testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
        test: suspend TestScope.() -> Unit,
    ): TestResult {
        Dispatchers.setMain(testDispatcher)

        return runTest {
            test()
            Dispatchers.resetMain()
        }
    }

}