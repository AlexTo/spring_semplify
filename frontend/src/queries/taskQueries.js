import gql from "graphql-tag";

export const FILES_INTEGRATION = "FilesIntegration";
export const FILE_ANNOTATION = "FileAnnotation";
export const URL_ANNOTATION = "UrlAnnotation";
export const URL_CRAWLER = "UrlCrawler";

export const FILE_ID = "fileId";
export const URL = "url";

export const taskQueries = {
  createTask: gql`
    mutation createTask($task: TaskInput!) {
      createTask(task: $task) {
        id
      }
    }
  `,
  tasks: gql`
    query tasks($taskQueryInput: TaskQueryInput!) {
      tasks(taskQueryInput: $taskQueryInput) {
        totalPages
        totalElements
        hasNext
        hasPrevious
        tasks {
          id
          type
          numberOfSubtasks
          numberOfFinishedSubtasks
          scheduled
          taskStatus
          error
        }
      }
    }
  `
};
