import gql from "graphql-tag";

export const indexerQueries = {

  search: gql`
    query search($query: CriteriaQuery!) {
      search(query: $query) {
        totalHits
        searchHits {
          id
          content {
            uri
            label
          }
          highlightFields {
            fieldName
            highlights
          }
        }
        buckets {
          uri
          name
          docCount
          buckets {
            uri
            name
            docCount
          }
        }
      }
    }
  `
};
