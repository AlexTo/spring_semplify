import request from "superagent";

const serviceUri = "/indexer";


export const searchService = {
  indexFileAsync,
  query
};

async function indexFileAsync(file, doc, token) {
  const url = `${serviceUri}/index/files/`;
  return await request
    .post(url)
    .set('Authorization', `Bearer ${token}`)
    .attach('file', file)
    .field('doc', JSON.stringify(doc));
}

function query(q) {
  const url = `${serviceUri}/search`;
  return request.get(url)
    .query({q: q});
}
