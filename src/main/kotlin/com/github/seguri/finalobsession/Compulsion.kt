package com.github.seguri.finalobsession

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal
import com.intellij.psi.PsiFile
import com.intellij.util.containers.stream

class Compulsion(private val file: PsiFile) {
  private val inspection = LocalCanBeFinal()
  private val inspectionManager = InspectionManager.getInstance(file.project)

  fun giveIn() {
    canBeFinalProblems().forEach { problem ->
      makeFinalFix(problem)?.ifPresent { it.applyFix(file.project, problem) }
    }
  }

  private fun problems() = inspection.processFile(file, inspectionManager).stream()

  private fun canBeFinalProblems() =
      problems().filter { it.descriptionTemplate.contains("can have <code>final</code> modifier") }

  private fun makeFinalFix(problem: ProblemDescriptor) =
      problem.fixes?.stream()?.filter { it.name == "Make final" }?.findFirst()
}
