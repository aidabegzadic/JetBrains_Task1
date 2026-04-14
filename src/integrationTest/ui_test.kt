package com.example.changelists

import com.intellij.driver.sdk.ui.components.common.dialog
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class ChangelistsSettingsUiTest {

    @Test
    fun createChangelistsAutomaticallyCanBeEnabled() {
        Starter.newContext(
            testName = "createChangelistsAutomaticallyCanBeEnabled",
            testCase = TestCase(
                IdeProductProvider.IC,
                GitHubProject.fromGithub(
                    branchName = "master",
                    repoRelativeUrl = "JetBrains/ij-perf-report-aggregator"
                )
            ).withVersion("2024.3")
        ).runIdeWithDriver().useDriverAndCloseIde {
            waitForProjectOpen()
            waitForIndicators(5.minutes)

            ideFrame {
                invokeAction("ShowSettings")
                dialog(title = "Settings") {
                    x(xQuery { byVisibleText("Version Control") }).click()
                    x(xQuery { byVisibleText("Changelists") }).click()

                    val checkbox = x(
                        xQuery { byVisibleText("Create changelists automatically") }
                    )

                    if (!checkbox.isSelected()) {
                        checkbox.click()
                    }

                    check(checkbox.isSelected()) {
                        "Create changelists automatically should be selected"
                    }

                    button("OK").click()
                }
            }
        }
    }
}