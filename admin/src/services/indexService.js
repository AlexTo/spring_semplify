import request from "superagent";

const serviceUri = "/indexer";


export const indexService = {
  indexFileAsync
};

async function indexFileAsync(file, doc, token) {
  const url = `${serviceUri}/index/files/`;
  try {
    return await request
      .post(url)
      .set('Authorization', `Bearer ${token}`)
      .attach('file', file)
      .field('doc', JSON.stringify(doc));
  } catch (err) {
    return err
  }
}
