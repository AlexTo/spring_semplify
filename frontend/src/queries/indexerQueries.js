import gql from "graphql-tag";

export const indexerQueries = {

  suggest: gql`
    query suggest($query: SuggestInput!) {
      suggest(query: $query) {
        suggestions {
          uri
          text
          prefLabel
          thumbnailUri
        }
      }
    }
  `,

  search: gql`
    query search($query: QueryInput!) {
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
