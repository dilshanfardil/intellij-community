package com.jetbrains.edu.coursecreator.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.edu.coursecreator.CCSubtaskEditorNotificationProvider;
import com.jetbrains.edu.coursecreator.CCUtils;
import com.jetbrains.edu.learning.StudyUtils;
import com.jetbrains.edu.learning.core.EduNames;
import com.jetbrains.edu.learning.courseFormat.Task;
import org.jetbrains.annotations.NotNull;

public class CCSwitchSubtask extends DumbAwareAction {
  public CCSwitchSubtask() {
    super("Switch Subtask", "Switches to selected subtask", null);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Task task = getTask(e);
    if (task == null) {
      return;
    }
    Project project = e.getProject();
    if (project == null) {
      return;
    }
    CCSubtaskEditorNotificationProvider.createPopup(task, project).showCenteredInCurrentWindow(project);
  }

  @Override
  public void update(AnActionEvent e) {
    Presentation presentation = e.getPresentation();
    presentation.setEnabledAndVisible(false);
    Task task = getTask(e);
    if (task == null || !task.hasSubtasks()) {
      return;
    }
    presentation.setEnabledAndVisible(true);
  }

  private static Task getTask(@NotNull AnActionEvent e) {
    Project project = e.getProject();
    if (project == null || !CCUtils.isCourseCreator(project)) {
      return null;
    }
    VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
    if (virtualFile == null) {
      return null;
    }
    while (virtualFile.getName().equals(EduNames.SRC) || !virtualFile.isDirectory()) {
      VirtualFile parent = virtualFile.getParent();
      if (parent != null) {
        virtualFile = parent;
      }
    }
    return StudyUtils.getTask(project, virtualFile);
  }
}
