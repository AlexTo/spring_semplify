import gql from "graphql-tag";

export const entityQueries = {
  depiction: gql`
    query depiction($uri: String!){
      depiction(uri: $uri) {
        depictionUri
      }
    }
  `,
  thumbnail: gql`
    query thumbnail($uri: String!){
      thumbnail(uri: $uri) {
        thumbnailUri
      }
    }
  `,
  summary: gql`
    query summary($uri: String!){
      summary(uri: $uri) {
        thumbnail {
          thumbnailUri
        }
        prefLabel {
          prefLabel
        }
        abstract_ {
          text
        }
      }
    }
  `
};
