package com.github.seguri.finalobsession

import com.intellij.ide.actionsOnSave.impl.ActionsOnSaveFileDocumentManagerListener
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

class Obsession : ActionsOnSaveFileDocumentManagerListener.ActionOnSave() {
  private val logger: Logger = Logger.getInstance(Obsession::class.java)

  override fun isEnabledForProject(project: Project): Boolean {
    return true
  }

  override fun processDocuments(project: Project, documents: Array<out Document>) {
    super.processDocuments(project, documents)
    logVersion()
    if (isIndexing(project)) {
      logger.warn("Cannot run while IDE is indexing")
      return
    }
    if (!validateProject(project)) {
      logger.warn("Project is not valid")
      return
    }
    documents.forEach { processDocument(project, it) }
  }

  private fun processDocument(project: Project, document: Document) {
    val psiDocumentManager = PsiDocumentManager.getInstance(project)
    psiDocumentManager.commitDocument(document)
    val file = psiDocumentManager.getPsiFile(document)
    if (!validPsiFile(file)) {
      logger.warn("Invalid file $file")
      return
    }
    writeAction(project) { progressBar(project) { Compulsion(file!!).giveIn() } }
  }

  private fun logVersion() {
    val pluginId = PluginId.getId("com.github.seguri.finalobsession")
    PluginManagerCore.getPlugin(pluginId)?.let {
      logger.info("Running Final Obsession v${it.version}")
    }
  }

  private fun isIndexing(project: Project): Boolean {
    return DumbService.getInstance(project).isDumb
  }

  private fun validateProject(project: Project): Boolean {
    return project.isInitialized && !project.isDisposed
  }

  private fun validPsiFile(file: PsiFile?): Boolean {
    return file != null && file.isValid && isInProject(file)
  }

  private fun isInProject(file: PsiFile): Boolean {
    return ProjectRootManager.getInstance(file.project).fileIndex.isInContent(file.virtualFile)
  }

  private fun writeAction(project: Project, runnable: () -> Unit) {
    WriteCommandAction.runWriteCommandAction(project, runnable)
  }

  private fun progressBar(project: Project, runnable: () -> Unit) {
    ProgressManager.getInstance()
        .runProcessWithProgressSynchronously(
            runnable, "Format with Final Obsession", false, project)
  }
}
