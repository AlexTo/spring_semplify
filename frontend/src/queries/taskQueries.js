import gql from "graphql-tag/src";

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
    query tasks($taskQuery: TaskQueryInput!) {
      tasks(taskQuery: $taskQuery) {
        totalPages
        totalElements
        hasNext
        hasPrevious
        tasks {
          id
          type
          numberOfSubTasks
          numberOfFinishedSubTasks
          scheduled
          taskStatus
          error
        }
      }
    }
  `
};
