package com.github.rickardoberg.stuff.usecase;

import java.util.function.Function;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionSource;
import com.github.rickardoberg.stuff.domain.Project;

public class Projects {
  private Project project;

  public void select(Project project) {
    this.project = project;
  }

  public static class NewProject {
    public ChangeDescription changeDescription;
    public Identifier id;
  }

  public static Function<Overview, Function<NewProject, Project>> newProject() {
    return overview -> newProject ->
    {
      Project project = new Project(newProject.id);

      Projects projects = new Projects();
      projects.select(project);
      Projects.changeDescription().apply(projects).apply(newProject.changeDescription);
      return project;
    };
  }

  public static class ChangeDescription {
    public String description;
  }

  public static Function<Projects, Function<ChangeDescription, InteractionSource>> changeDescription() {
    return projects -> changeDescription ->
    {
      projects.project.changeDescription(changeDescription.description);
      return projects.project;
    };
  }

  public static class ProjectDone {
    public boolean done;
  }

  public static Function<Projects, Function<ProjectDone, InteractionSource>> done() {
    return projects -> done ->
    {
      projects.project.setDone(done.done);
      return projects.project;
    };
  }

  public static class DeleteProject {
  }

  public static Function<Projects, Function<DeleteProject, InteractionSource>> deleteProject() {
    return projects -> deleteProject ->
    {
      projects.project.delete();
      return projects.project;
    };
  }
}
