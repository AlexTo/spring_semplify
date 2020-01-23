import request from 'superagent';

const serviceUri = '/entity-hub';

export const nerService = {
  annotateFileAsync
};

async function annotateFileAsync(file, token) {
  const url = `${serviceUri}/ner/file`;
  try {
    return await request
      .post(url)
      .set('Authorization', `Bearer ${token}`)
      .attach('file', file);
  } catch (err) {
    return err
  }
}
