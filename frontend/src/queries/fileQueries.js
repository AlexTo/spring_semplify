import gql from "graphql-tag";

export const fileQueries = {
  fileAnnotations: gql`
      query fileAnnotations($fileAnnotationQueryInput: FileAnnotationQueryInput!) {
          fileAnnotations(fileAnnotationQueryInput: $fileAnnotationQueryInput) {
              totalPages
              totalElements
              hasNext
              hasPrevious
              fileAnnotations {
                  id
                  file {
                      id
                      name
                      contentType
                  }
                  status
              }
          }
      }
  `,
  fileAnnotationResources: gql`
      query fileAnnotationResources($fileAnnotationId: Int!) {
          fileAnnotationResources(fileAnnotationId : $fileAnnotationId) {
              id
              uri
              surfaceForm
              prefLabel
              context
              source
          }
      }
  `,
  updateFileAnnotation: gql`
      mutation updateFileAnnotation($fileAnnotationInput: FileAnnotationInput!) {
          updateFileAnnotation(fileAnnotationInput: $fileAnnotationInput) {
              id
              status
          }
      }
  `
};
