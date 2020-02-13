import request from "superagent";

const serviceUri = "/indexer";


export const searchService = {
  indexFileAsync,
  query
};

async function indexFileAsync(file, documentMetadata, token) {
  const url = `${serviceUri}/index/files/`;
  return await request
    .post(url)
    .set('Authorization', `Bearer ${token}`)
    .attach('file', file)
    .field('documentMetadata', JSON.stringify(documentMetadata));
}

function query(q) {
  const url = `${serviceUri}/search`;
  return request.get(url)
    .query({q: q});
}
